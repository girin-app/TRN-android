package app.girin.trn.example

import app.girin.trn.NetworkName
import app.girin.trn.ROOT_ID
import app.girin.trn.api.lib.account.nextIndex
import app.girin.trn.api.lib.chain.getBlock
import app.girin.trn.api.lib.chain.getFinalizedHead
import app.girin.trn.api.lib.feeproxy.FeeProxy
import app.girin.trn.api.lib.rpc.submitExtrinsic
import app.girin.trn.api.lib.state.callStateTransactionPayment
import app.girin.trn.api.lib.state.getRuntimeVersion
import app.girin.trn.api.lib.types.Mortal
import app.girin.trn.api.lib.types.Signature
import app.girin.trn.api.lib.types.SubmittableExtrinsic
import app.girin.trn.api.lib.xrplbridge.Withdraw
import app.girin.trn.api.lib.xrplbridge.WithdrawXrp
import app.girin.trn.evm.lib.dex.getAmountIn
import app.girin.trn.getPublicProviderInfo
import io.ethers.core.types.Address
import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import io.ethers.signers.PrivateKeySigner
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import java.math.BigInteger
import kotlin.math.roundToInt

@Ignore("Example")
class BridgeTest {
    companion object {
        private const val TEST_PK_HEX =
            "0xf28c395640d7cf3a8b415d12f741a0299b34cb0c7af7d2ba6440d9f2d3880d65"
        private const val TEST_XRP_DESTINATION = "0x72ee785458b89d5ec64bec8410c958602e6f7673"
    }

    @Test
    fun bridge() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, false)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val privateKeyHex = TEST_PK_HEX
        val signer = PrivateKeySigner(privateKeyHex)

        // 1. initial bridge call method
        val destination = Address(TEST_XRP_DESTINATION)
        val amount = BigInteger("1000000") // 1XRP
        val methodWithdrawXrp = WithdrawXrp.create(amount, destination)

        // 2. create Extrinsic
        // 2.1 nonce
        val nonce = provider.nextIndex(signer.address).sendAwait().unwrap()

        // 2.2 era
        val blockHash = provider.getFinalizedHead().sendAwait().unwrap()
        val block = provider.getBlock(blockHash).sendAwait().unwrap()
        val mortal = Mortal(current = block.block.header.number.toLong())

        val tip = BigInteger.ZERO
        val extrinsic = SubmittableExtrinsic(
            Signature(
                signer = signer.address,
                era = mortal.toMortalEra(),
                nonce = nonce.toBigInteger(),
                tip = tip
            ),
            methodWithdrawXrp
        )

        // 3. get sign info
        val runtimeVersion = provider.getRuntimeVersion().sendAwait().unwrap()

        // 4. sign
        extrinsic.sign(signer, runtimeVersion, providerInfo.genesisHash, blockHash)

        // 5. broadcast
        val extrinsicHash = provider.submitExtrinsic(extrinsic.toHex()).sendAwait().unwrap()

        println(extrinsicHash)
        Assert.assertEquals(33, extrinsicHash.rlpSize())
    }

    @Test
    fun bridgeWithAssetId() {
        val providerInfo = getPublicProviderInfo(NetworkName.ROOT, false, false)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val privateKeyHex = TEST_PK_HEX
        val signer = PrivateKeySigner(privateKeyHex)
        println(signer.address.toChecksumString())
        // 1. initial bridge call method
        val destination = Address(TEST_XRP_DESTINATION)

        val methodWithdraw = Withdraw.create(
            //1 ROOT
            assetId = 1U,
            amount = BigInteger("5000000"),
            destination = destination,
        )

        // 2. create Extrinsic
        // 2.1 nonce
        val nonce = provider.nextIndex(signer.address).sendAwait().unwrap()

        // 2.2 era
        val blockHash = provider.getFinalizedHead().sendAwait().unwrap()
        val block = provider.getBlock(blockHash).sendAwait().unwrap()
        val mortal = Mortal(current = block.block.header.number.toLong())

        val tip = BigInteger.ZERO
        val extrinsic = SubmittableExtrinsic(
            Signature(
                signer = signer.address,
                era = mortal.toMortalEra(),
                nonce = nonce.toBigInteger(),
                tip = tip
            ),
            methodWithdraw
        )

        // 3. get sign info
        val runtimeVersion = provider.getRuntimeVersion().sendAwait().unwrap()

        // 4. sign
        extrinsic.sign(signer, runtimeVersion, providerInfo.genesisHash, blockHash)

        // 5. broadcast
        val extrinsicHash = provider.submitExtrinsic(extrinsic.toHex()).sendAwait().unwrap()

        println(extrinsicHash)
        Assert.assertEquals(33, extrinsicHash.rlpSize())
    }


    @Test
    fun bridgeWithFeeProxy() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, false)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val privateKeyHex = TEST_PK_HEX
        val signer = PrivateKeySigner(privateKeyHex)

        // 1. initial fee proxy bridge call method
        val destination = Address(TEST_XRP_DESTINATION)

        val methodFeeProxy = FeeProxy(
            args = FeeProxy.FeeProxyArgs(
                paymentAsset = BigInteger(ROOT_ID.toString()),
                maxPayment = BigInteger.ZERO,
                call = Withdraw.create(
                    assetId = 1U,
                    amount = BigInteger("200"),
                    destination = destination,
                )
            )
        )

        // 2. create Extrinsic
        // 2.1 nonce
        val nonce = provider.nextIndex(signer.address).sendAwait().unwrap()

        // 2.2 era
        val blockHash = provider.getFinalizedHead().sendAwait().unwrap()
        val block = provider.getBlock(blockHash).sendAwait().unwrap()
        val mortal = Mortal(current = block.block.header.number.toLong())

        // 2.3. tip
        val tip = BigInteger.ZERO

        val extrinsic = SubmittableExtrinsic(
            signature = Signature(
                signer = signer.address,
                era = mortal.toMortalEra(),
                nonce = nonce.toBigInteger(),
                tip = tip
            ),
            method = methodFeeProxy
        )

        // 3. fee calculate
        // 3.1 gas simulate
        val runtimeDispatchInfo =
            provider.callStateTransactionPayment(extrinsic).sendAwait().unwrap()

        // 3.2 fee calculate XRP > TRN
        val maxPaymentInt =
            provider.getAmountIn(runtimeDispatchInfo.partialFee, ROOT_ID).sendAwait().unwrap()
        methodFeeProxy.args.maxPayment = (maxPaymentInt * 1.05).roundToInt().toBigInteger()
        extrinsic.method = methodFeeProxy

        // 4. get sign info
        val runtimeVersion = provider.getRuntimeVersion().sendAwait().unwrap()


        // 5. sign
        extrinsic.sign(signer, runtimeVersion, providerInfo.genesisHash, blockHash)

        // 6. broadcast
        val extrinsicHash = provider.submitExtrinsic(extrinsic.toHex()).sendAwait().unwrap()

        println(extrinsicHash)
        Assert.assertEquals(33, extrinsicHash.rlpSize())
    }

}