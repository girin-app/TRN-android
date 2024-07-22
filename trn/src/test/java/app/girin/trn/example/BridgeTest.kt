package app.girin.trn.example

import app.girin.trn.NetworkName
import app.girin.trn.api.lib.account.nextIndex
import app.girin.trn.api.lib.author.submitExtrinsic
import app.girin.trn.api.lib.chain.getBlock
import app.girin.trn.api.lib.chain.getFinalizedHead
import app.girin.trn.api.lib.state.getRuntimeVersion
import app.girin.trn.api.lib.types.Method
import app.girin.trn.api.lib.types.Mortal
import app.girin.trn.api.lib.types.WithdrawXrpArgs
import app.girin.trn.getPublicProviderInfo
import io.ethers.core.types.Address
import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import io.ethers.signers.PrivateKeySigner
import org.junit.Assert
import org.junit.Test
import java.math.BigInteger

class BridgeTest {
    @Test
    fun bridge() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, false)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val privateKeyHex = "0xf28c395640d7cf3a8b415d12f741a0299b34cb0c7af7d2ba6440d9f2d3880d65"
        val signer = PrivateKeySigner(privateKeyHex)

        // 1. initial bridge call method
        val destination = Address("0x72ee785458b89d5ec64bec8410c958602e6f7673")
        val amount = BigInteger("1000000") // 1XRP
        val method = Method(
            args = WithdrawXrpArgs(
                amount,
                destination,
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
        var extrinsic = method.createExtrinsic(nonce.toBigInteger(), mortal.toMortalEra(), tip)

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