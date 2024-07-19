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
}