package app.girin.trn.example

import app.girin.trn.NetworkName
import app.girin.trn.ROOT_ID
import app.girin.trn.evm.lib.ERC20_PRECOMPILE
import app.girin.trn.evm.lib.FEE_PROXY_PRECOMPILE
import app.girin.trn.evm.lib.assetIdToERC20Address
import app.girin.trn.evm.lib.transfer.getFeeProxyPricePair
import app.girin.trn.getPublicProviderInfo
import io.ethers.abi.AbiFunction
import io.ethers.core.types.Address
import io.ethers.core.types.BlockId
import io.ethers.core.types.CallRequest
import io.ethers.core.types.transaction.TxDynamicFee
import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import io.ethers.signers.PrivateKeySigner
import io.ethers.signers.sign
import org.junit.Ignore
import org.junit.Test
import java.math.BigInteger

@Ignore("Example") class TransferTest {
    companion object{

        private const val RECEIVER = "0xE2640ae2A8DFeCB460C1062425b5FD314B6E60D5"
        private const val PK_HEX = "0xf28c395640d7cf3a8b415d12f741a0299b34cb0c7af7d2ba6440d9f2d3880d65"
    }
    @Test
    fun transferNative() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, false)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val privateKeyHex = PK_HEX
        val signer = PrivateKeySigner(privateKeyHex)

        val receiver = Address(RECEIVER)

        val nonce = provider.getTransactionCount(signer.address, BlockId.LATEST).sendAwait().unwrap()

        val baseFee = provider.getBlockWithHashes(BlockId.LATEST).sendAwait().unwrap().get().baseFeePerGas!!

        val tx = TxDynamicFee(
            to = receiver,
            value = "1000000000000000000".toBigInteger(), // 1 XRP
            nonce = nonce,
            gas = 21000,
            gasFeeCap = baseFee,
            gasTipCap = BigInteger.ZERO,
            data = null,
            accessList = emptyList(),
            chainId = provider.chainId,
        )

        val signedTx = tx.sign(signer)

        val res = provider.sendRawTransaction(signedTx).sendAwait().unwrap()
        println(res.toString())
    }

    @Test
    fun transferERC20() {
        val providerInfo = getPublicProviderInfo(NetworkName.ROOT, false, false)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val privateKeyHex = PK_HEX
        val signer = PrivateKeySigner(privateKeyHex)

        val receiver = Address(RECEIVER)

        //RLUSD
        val erc20Contract = assetIdToERC20Address(0x26864)

        val decimalFunction = AbiFunction.parseSignature(ERC20_PRECOMPILE.getAbi(ERC20_PRECOMPILE.Index.FUNCTION_DECIMALS))
        val decimalRes = provider.call(
            CallRequest().apply {
                to = erc20Contract
                data = decimalFunction.encodeCall(emptyArray())
            },
            BlockId.LATEST
        ).sendAwait().unwrap()

        val decimal = BigInteger(decimalRes.asByteArray())

        val amount = BigInteger.TEN.pow(decimal.toInt())

        val transferFunction =
            AbiFunction.parseSignature(ERC20_PRECOMPILE.getAbi(ERC20_PRECOMPILE.Index.FUNCTION_TRANSFER))
        val params = arrayOf(
            receiver,
            amount
        )
        val encoded = transferFunction.encodeCall(params)

        val gas = provider.estimateGas(
            CallRequest().apply {
                from = signer.address
                to = erc20Contract
                data = encoded
            },
            BlockId.LATEST
        ).sendAwait().unwrap()

        val nonce =
            provider.getTransactionCount(signer.address, BlockId.LATEST).sendAwait().unwrap()

        val baseFee =
            provider.getBlockWithHashes(BlockId.LATEST).sendAwait().unwrap().get().baseFeePerGas!!

        val tx = TxDynamicFee(
            to = erc20Contract,
            value = BigInteger.ZERO,
            nonce = nonce,
            gas = gas,
            gasFeeCap = baseFee,
            gasTipCap = BigInteger.ZERO,
            data = encoded,
            accessList = emptyList(),
            chainId = provider.chainId,
        )

        val signedTx = tx.sign(signer)

        val res = provider.sendRawTransaction(signedTx).sendAwait().unwrap()
        println(res.toString())

    }

    @Test
    fun transferERC20WithFeeProxy() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, false)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val privateKeyHex = PK_HEX
        val signer = PrivateKeySigner(privateKeyHex)

        val receiver = Address(RECEIVER)

        val rootContract = assetIdToERC20Address(ROOT_ID)

        val decimalFunction = AbiFunction.parseSignature(ERC20_PRECOMPILE.getAbi(ERC20_PRECOMPILE.Index.FUNCTION_DECIMALS))
        val decimalRes = provider.call(
            CallRequest().apply {
                to = rootContract
                data = decimalFunction.encodeCall(emptyArray())
            },
            BlockId.LATEST
        ).sendAwait().unwrap()

        val decimal = BigInteger(decimalRes.asByteArray())

        val amount = BigInteger.TEN.pow(decimal.toInt())

        val transferFunction =
            AbiFunction.parseSignature(ERC20_PRECOMPILE.getAbi(ERC20_PRECOMPILE.Index.FUNCTION_TRANSFER))
        val transferParams = arrayOf(
            receiver,
            amount
        )
        val transferEncoded = transferFunction.encodeCall(transferParams)

        val gas = provider.estimateGas(
            CallRequest().apply {
                from = signer.address
                to = rootContract
                data = transferEncoded
            },
            BlockId.LATEST
        ).sendAwait().unwrap()

        val res = getFeeProxyPricePair(
            provider = provider,
            gasEstimate = gas.toBigInteger(),
            feeAssetId = ROOT_ID,
            slippage = 0.05
        )

        val callWithFeePreferencesFunction = AbiFunction.parseSignature(FEE_PROXY_PRECOMPILE.getAbi(FEE_PROXY_PRECOMPILE.Index.FUNCTION_CALL_WITH_FEE_PREFERENCES))
        val callWithFeePreferencesParams = arrayOf(
            rootContract,
            res.maxPayment,
            rootContract,
            transferEncoded
        )
        val callWithFeePreferencesEncoded = callWithFeePreferencesFunction.encodeCall(callWithFeePreferencesParams)

        val nonce =
            provider.getTransactionCount(signer.address, BlockId.LATEST).sendAwait().unwrap()



        val tx = TxDynamicFee(
            to = FEE_PROXY_PRECOMPILE.address,
            value = BigInteger.ZERO,
            nonce = nonce,
            gas = gas.toLong(),
            gasFeeCap = res.maxFeePerGas,
            gasTipCap = BigInteger.ZERO,
            data = callWithFeePreferencesEncoded,
            accessList = emptyList(),
            chainId = provider.chainId,
        )

        val signedTx = tx.sign(signer)

        val txhash = provider.sendRawTransaction(signedTx).sendAwait().unwrap()
        println(txhash.toString())

    }
}