package app.girin.trn.api.lib.types

import app.girin.trn.api.lib.state.RuntimeVersion
import io.ethers.core.FastHex
import io.ethers.core.types.Address
import io.ethers.core.types.Hash
import io.ethers.signers.PrivateKeySigner
import okio.ByteString.Companion.toByteString
import org.junit.Assert
import org.junit.Test
import java.math.BigInteger

class SubmittableExtrinsicTest {
    @Test
    fun testSubmittableExtrinsic() {
        val privateKeyHex = "0xf28c395640d7cf3a8b415d12f741a0299b34cb0c7af7d2ba6440d9f2d3880d65"
        val signer = PrivateKeySigner(privateKeyHex)

        val signature = FastHex.decode("b25593b528be5d02feca601b9c71416f7d32eddbd89a4e5ba4e0c20931f4993852d786d5ff70615218ede9ee1605692bca3311d89a1d55a9c88f7d0f9978bfd400")

        val mortalEra = MortalEra(FastHex.decode("7605"))
        val nonce = BigInteger("51")
        val tip = BigInteger.ZERO

        val callIndex = FastHex.decode("1203")

        val destination = Address("0x72ee785458b89d5ec64bec8410c958602e6f7673")

        val extrinsic = SubmittableExtrinsic(
            Signature(
                signer.address,
                signature,
                mortalEra,
                nonce,
                tip
            ),
            Method(
                callIndex,
                WithdrawXrpArgs(
                    BigInteger("1000000"),
                    destination,
                )
            )
        )

        Assert.assertEquals("0x01028455d77a60fd951117f531d2277a5bb4afbe3fb292b25593b528be5d02feca601b9c71416f7d32eddbd89a4e5ba4e0c20931f4993852d786d5ff70615218ede9ee1605692bca3311d89a1d55a9c88f7d0f9978bfd4007605cc00120340420f0000000000000000000000000072ee785458b89d5ec64bec8410c958602e6f7673", extrinsic.toHex())
    }

    @Test
    fun testSign() {
        val callIndex = FastHex.decode("1203")

        val destination = Address("0x72ee785458b89d5ec64bec8410c958602e6f7673")

        val method = Method(
            callIndex,
            WithdrawXrpArgs(
                BigInteger("1000000"),
                destination,
            )
        )
        val mortalEra = MortalEra(FastHex.decode("1603"))
        val nonce = BigInteger("52")
        val tip = BigInteger.ZERO

        val extrinsic = method.createExtrinsic(nonce, mortalEra, tip)

        val runtimeVersion = RuntimeVersion("root", "root", 1, 54, 0, arrayListOf(), 9, 0)
        val genesisHash = Hash("83959f7f4262762f7599c2fa48b418b7e102f92c81fab9e6ef22ab379abdb72f")
        val blockHash = Hash("229c525b91f1b6b29c56d57697ba8df4bf4fa6d15aee295e0d611a85f53dde31")

        val signer = PrivateKeySigner("0xf28c395640d7cf3a8b415d12f741a0299b34cb0c7af7d2ba6440d9f2d3880d65")

        extrinsic.sign(signer, runtimeVersion, genesisHash, blockHash)
        Assert.assertEquals("b25593b528be5d02feca601b9c71416f7d32eddbd89a4e5ba4e0c20931f4993852d786d5ff70615218ede9ee1605692bca3311d89a1d55a9c88f7d0f9978bfd41b", extrinsic.signature.signature!!.toByteString().hex())
        Assert.assertEquals("0x55D77A60Fd951117f531D2277a5BB4aFbE3fB292", extrinsic.signature.signer!!.toChecksumString())
    }

    @Test
    fun testPayload() {
        val callIndex = FastHex.decode("1203")

        val destination = Address("0x72ee785458b89d5ec64bec8410c958602e6f7673")

        val method = Method(
            callIndex,
            WithdrawXrpArgs(
                BigInteger("1000000"),
                destination,
            )
        )
        val mortalEra = MortalEra(FastHex.decode("1603"))
        val nonce = BigInteger("52")
        val tip = BigInteger.ZERO

        val extrinsic = method.createExtrinsic(nonce, mortalEra, tip)

        val runtimeVersion = RuntimeVersion("root", "root", 1, 54, 0, arrayListOf(), 9, 0)
        val genesisHash = Hash("83959f7f4262762f7599c2fa48b418b7e102f92c81fab9e6ef22ab379abdb72f")
        val blockHash = Hash("229c525b91f1b6b29c56d57697ba8df4bf4fa6d15aee295e0d611a85f53dde31")

        val payload = extrinsic.getPayload(runtimeVersion, genesisHash, blockHash)

        Assert.assertEquals("120340420f0000000000000000000000000072ee785458b89d5ec64bec8410c958602e6f76731603d000360000000900000083959f7f4262762f7599c2fa48b418b7e102f92c81fab9e6ef22ab379abdb72f229c525b91f1b6b29c56d57697ba8df4bf4fa6d15aee295e0d611a85f53dde31", payload.toByteString().hex())
    }
}