package app.girin.trn.api.lib.types

import app.girin.trn.api.lib.state.RuntimeVersion
import app.girin.trn.util.bnToU8a
import app.girin.trn.util.compactToU8a
import io.ethers.core.FastHex
import io.ethers.core.types.Address
import io.ethers.core.types.Hash
import io.ethers.crypto.Hashing
import io.ethers.signers.PrivateKeySigner
import okio.ByteString.Companion.toByteString
import java.math.BigInteger


class SubmittableExtrinsic(var signature: Signature, var method: Method) {

    fun sign(privateKey: PrivateKeySigner, runtimeVersion: RuntimeVersion, genesisHash: Hash, blockHash: Hash) {
        val payload = getPayload(runtimeVersion, genesisHash, blockHash)
        val signHash = Hashing.keccak256(payload)
        val sig = privateKey.signHash(signHash)

        signature.signature = sig.toByteArray()
        signature.signer = privateKey.address
    }

    fun getPayload(runtimeVersion: RuntimeVersion, genesisHash: Hash, blockHash: Hash): ByteArray {
        var payload = method.callIndex
        payload += bnToU8a(method.args.amount, 128)
        payload += method.args.destination.toByteArray()
        payload += signature.era.mortalEra
        payload += compactToU8a(signature.nonce)
        payload += bnToU8a(signature.tip)
        payload += bnToU8a(BigInteger.valueOf(runtimeVersion.specVersion.toLong()), 32)
        payload += bnToU8a(BigInteger.valueOf(runtimeVersion.transactionVersion.toLong()), 32)
        payload += genesisHash.toByteArray()
        payload += blockHash.toByteArray()

        return payload
    }

    fun toU8a(): ByteArray {
        var u8a = byteArrayOf(132.toByte()) // version 4 signed(128)

        val signer = signature.signer ?: throw Exception("empty signer")
        u8a += signer.toByteArray()

        val sig = signature.signature ?: throw Exception("empty signature")
        u8a += sig

        u8a += signature.era.mortalEra
        u8a += compactToU8a(signature.nonce)
        u8a += bnToU8a(signature.tip)
        u8a += method.callIndex
        u8a += bnToU8a(method.args.amount, 128)
        u8a += method.args.destination.toByteArray()

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
    var signature: ByteArray?,
    val era: MortalEra,
    val nonce: BigInteger,
    val tip: BigInteger
)

data class MortalEra(val mortalEra: ByteArray)

data class Method(
    val callIndex: ByteArray = FastHex.decode("1203"),
    val args: WithdrawXrpArgs
) {
    fun createExtrinsic(nonce: BigInteger, era: MortalEra, tip: BigInteger): SubmittableExtrinsic {
        return SubmittableExtrinsic(
            signature = Signature(signer = null, signature = null, era = era, nonce = nonce, tip = tip),
            method = this
        )
    }
}

data class WithdrawXrpArgs(
    val amount: BigInteger,
    val destination: Address
)