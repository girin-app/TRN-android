package app.girin.trn.example

import app.girin.trn.NetworkName
import app.girin.trn.ROOT_ID
import app.girin.trn.XRP_ID
import app.girin.trn.api.lib.assets.QueryMetadata
import app.girin.trn.api.lib.assets.getMetadata
import app.girin.trn.api.lib.chain.getBlock
import app.girin.trn.api.lib.chain.getFinalizedHead
import app.girin.trn.api.lib.feeproxy.FeeProxy
import app.girin.trn.api.lib.transactionpayment.callStateTransactionPayment
import app.girin.trn.api.lib.transactionpayment.getRuntimeVersion
import app.girin.trn.api.lib.types.Mortal
import app.girin.trn.api.lib.types.Signature
import app.girin.trn.api.lib.types.SubmittableExtrinsic
import app.girin.trn.evm.lib.dex.getAmountIn
import app.girin.trn.getPublicProviderInfo
import app.girin.trn.rpc.nextIndex
import app.girin.trn.rpc.submitExtrinsic
import io.ethers.core.types.Address
import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import io.ethers.signers.PrivateKeySigner
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.roundToInt
import app.girin.trn.api.lib.assets.Transfer as TokenTransfer
import app.girin.trn.api.lib.balances.Transfer as BalanceTransfer

@Ignore("example")
class NativeTransferTest {
    companion object {
        private const val RECEIVER = "0x784c245295885e8bf48711d431a880a09246da35"
        private const val PK_HEX =
            "0xf28c395640d7cf3a8b415d12f741a0299b34cb0c7af7d2ba6440d9f2d3880d65"
    }

    @Test
    fun transferRoot() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, false)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val signer = PrivateKeySigner(PK_HEX)

        // 1. initial bridge call method
        val destination = Address(RECEIVER)
        val amount = BigInteger("1000000") // 1Root
        val call = BalanceTransfer.create(target = destination, amount = amount)

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
            call
        )

        val runtimeDispatchInfo =
            provider.callStateTransactionPayment(extrinsic).sendAwait().unwrap()
        println(runtimeDispatchInfo)

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
    fun transferBalanceWithFeeProxy() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, false)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val signer = PrivateKeySigner(PK_HEX)

        // 1. initial bridge call method


        val destination = Address(RECEIVER)
        val amount = BigInteger("1000000") // 1Root
        val call = FeeProxy.create(
            paymentAsset = ROOT_ID.toUInt(),
            maxPayment = BigInteger.ZERO,
            call = BalanceTransfer.create(
                target = destination,
                amount = amount
            )
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
            call
        )

        val runtimeDispatchInfo =
            provider.callStateTransactionPayment(extrinsic).sendAwait().unwrap()
        println(runtimeDispatchInfo)

        // 3.2 fee calculate XRP -> TRN
        val maxPaymentInt =
            provider.getAmountIn(runtimeDispatchInfo.partialFee, ROOT_ID).sendAwait().unwrap()

        extrinsic.call = call.withMaxPayment((maxPaymentInt * 1.05).roundToInt().toBigInteger())

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
    fun transferToken() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, false)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val signer = PrivateKeySigner(PK_HEX)

        // 1. initial bridge call method
        val metadata =
            provider.getMetadata(QueryMetadata.create(XRP_ID.toUInt())).sendAwait().unwrap()


        val destination = Address(RECEIVER)
        val amount = BigDecimal("1").movePointRight(metadata.decimals).toBigInteger() // 1XRP
        val call =
            TokenTransfer.create(assetId = XRP_ID.toUInt(), target = destination, amount = amount)

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
            call
        )

        val runtimeDispatchInfo =
            provider.callStateTransactionPayment(extrinsic).sendAwait().unwrap()
        println(runtimeDispatchInfo)

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
    fun transferTokenWithFeeProxy() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, false)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val signer = PrivateKeySigner(PK_HEX)

        // 1. initial bridge call method
        val metadata =
            provider
                .getMetadata(QueryMetadata.create(XRP_ID.toUInt()))
                .sendAwait().unwrap()


        val destination = Address(RECEIVER)
        val amount = BigDecimal("1").movePointRight(metadata.decimals).toBigInteger() // 1XRP
        val call = FeeProxy.create(
            paymentAsset = ROOT_ID.toUInt(),
            maxPayment = BigInteger.ZERO,
            call = TokenTransfer.create(
                assetId = XRP_ID.toUInt(),
                target = destination,
                amount = amount
            )
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
            call
        )

        val runtimeDispatchInfo =
            provider.callStateTransactionPayment(extrinsic).sendAwait().unwrap()
        println(runtimeDispatchInfo)

        // 3.2 fee calculate XRP -> TRN
        val maxPaymentInt =
            provider.getAmountIn(runtimeDispatchInfo.partialFee, ROOT_ID).sendAwait().unwrap()

        extrinsic.call = call.withMaxPayment((maxPaymentInt * 1.05).roundToInt().toBigInteger())

        // 3. get sign info
        val runtimeVersion = provider.getRuntimeVersion().sendAwait().unwrap()

        // 4. sign
        extrinsic.sign(signer, runtimeVersion, providerInfo.genesisHash, blockHash)

        // 5. broadcast
        val extrinsicHash = provider.submitExtrinsic(extrinsic.toHex()).sendAwait().unwrap()
        println(extrinsicHash)
        Assert.assertEquals(33, extrinsicHash.rlpSize())
    }
}