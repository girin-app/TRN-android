package app.girin.trn.api.lib.types

import app.girin.trn.api.lib.state.RuntimeVersion
import app.girin.trn.util.bnToU8a
import app.girin.trn.util.compactToU8a
import io.ethers.core.types.Address
import io.ethers.core.types.Hash
import io.ethers.crypto.Hashing
import io.ethers.signers.PrivateKeySigner
import okio.ByteString.Companion.toByteString
import java.math.BigInteger


class SubmittableExtrinsic(var signature: Signature, var call: Call) {

    fun sign(privateKey: PrivateKeySigner, runtimeVersion: RuntimeVersion, genesisHash: Hash, blockHash: Hash) {
        val payload = getPayload(runtimeVersion, genesisHash, blockHash)
        val signHash = Hashing.keccak256(payload)
        val sig = privateKey.signHash(signHash)

        signature.signature = sig.toByteArray()
        signature.signer = privateKey.address
    }

    fun getPayload(runtimeVersion: RuntimeVersion, genesisHash: Hash, blockHash: Hash): ByteArray {
        var payload = call.toU8a()
        payload += signature.era.mortalEra
        payload += compactToU8a(signature.nonce)
        payload += bnToU8a(signature.tip)
        payload += bnToU8a(BigInteger.valueOf(runtimeVersion.specVersion), 32)
        payload += bnToU8a(BigInteger.valueOf(runtimeVersion.transactionVersion), 32)
        payload += genesisHash.toByteArray()
        payload += blockHash.toByteArray()

        return payload
    }

    fun toU8a(): ByteArray {
        var u8a = byteArrayOf(132.toByte()) // version 4 signed(128)

        u8a += signature.toU8a()

        u8a += call.toU8a()

        val count = compactToU8a(u8a.size.toBigInteger())
        return count + u8a
    }

    fun toHex(): String {
        val toU8aHex = toU8a().toByteString().hex()
        return "0x$toU8aHex"
    }
}

data class Signature(
    var signer: Address?,
    var signature: ByteArray? = ByteArray(65) { 1 },
    val era: MortalEra,
    val nonce: BigInteger,
    val tip: BigInteger
) {
    fun toU8a(): ByteArray {
        val signer = this.signer ?: throw Exception("empty signer")
        var u8a = signer.toByteArray()

        val sig = this.signature ?: throw Exception("empty signature")
        u8a += sig

        u8a += era.mortalEra
        u8a += compactToU8a(nonce)
        u8a += bnToU8a(tip)

        return u8a
    }
}

data class MortalEra(val mortalEra: ByteArray)
