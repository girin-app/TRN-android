package app.girin.trn.example

import io.ethers.signers.MnemonicKeySource
import io.ethers.signers.PrivateKeySigner
import okio.ByteString.Companion.toByteString
import org.junit.Test
import org.junit.Assert.*

class WalletTest {
    @Test
    fun generateMnemonic() {
        val mnemonic = "model vanish nest share talk duck promote useful base wrong veteran pink"

        val account  = MnemonicKeySource(mnemonic).getAccount(0)
        assertEquals("0x55D77A60Fd951117f531D2277a5BB4aFbE3fB292", account.address.toChecksumString())
        assertEquals("f28c395640d7cf3a8b415d12f741a0299b34cb0c7af7d2ba6440d9f2d3880d65", account.signingKey.privateKey.toByteString().hex())
    }

    @Test
    fun generatePrivatekey() {
        val privateKeyHex = "0xf28c395640d7cf3a8b415d12f741a0299b34cb0c7af7d2ba6440d9f2d3880d65"
        val signer = PrivateKeySigner(privateKeyHex)
        assertEquals("0x55D77A60Fd951117f531D2277a5BB4aFbE3fB292", signer.address.toChecksumString())
    }

    @Test
    fun signMessage() {
        val privateKeyHex = "0xf28c395640d7cf3a8b415d12f741a0299b34cb0c7af7d2ba6440d9f2d3880d65"
        val signer = PrivateKeySigner(privateKeyHex)

        // pubkey
        assertEquals("0404635da77dfab2308a512fb7ba7d4aa02d33cd208c71fe91e985a91dbc12fe6e7d9178f6f66263434b25084b027605a46e57694687ff0eab3d56493f69e21c36", signer.signingKey.publicKey.toByteString().hex())

        val message = "eyJpZCI6ImUwYjAyM2Y3LWU0OTktNGM1Yi04OTI5LTJlYjM5ZGIzYTMwNiIsInRzIjoxNzM1MjgyNDE2fQ"
        
        // signature
        val signature = signer.signMessage(message.toByteArray())
        assertEquals("d48cde00d1f203a37ee9b5556412bcf04d0d5fbd520810b1c82a7379df68804128e165fe7936868c743f4e126a3b4f80da1eeb260b5e4060dd934e01ec6d2a041c", signature.toByteArray().toByteString().hex())
    }
}