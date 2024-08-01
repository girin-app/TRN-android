package app.girin.trn.api.lib.state

import app.girin.trn.RpcMethod
import app.girin.trn.api.lib.types.SubmittableExtrinsic
import app.girin.trn.util.bnToU8a
import app.girin.trn.util.decodeCompact
import app.girin.trn.util.u8aConcatStrict
import io.ethers.core.readHexByteArray
import io.ethers.providers.Provider
import io.ethers.providers.RpcError
import io.ethers.providers.types.RpcCall
import io.ethers.providers.types.RpcRequest
import okio.ByteString.Companion.toByteString
import java.math.BigInteger

data class RuntimeVersion(
    val specName: String,
    val implName: String,
    val authoringVersion: Long,
    val specVersion: Long,
    val implVersion: Long,
    val apis: List<List<Any>>,
    val transactionVersion: Long,
    val stateVersion: Long
)

fun Provider.getRuntimeVersion(): RpcRequest<RuntimeVersion, RpcError> {
    return RpcCall(client, RpcMethod.StateGetRuntimeVersion.methodName, emptyArray<String>(), RuntimeVersion::class.java)
}

data class Weight (
    val refTime: BigInteger,
    val proofSize: BigInteger
)

fun decodeU8aRuntimeDispatchInfo(data: ByteArray): RuntimeDispatchInfo {
    val (offset1, refTime) = decodeCompact(data)
    val (offset2, proofSize) = decodeCompact(data.copyOfRange(offset1, data.lastIndex))
    val classValue = data[offset1 + offset2].toInt()
    val partialFee = BigInteger(data.copyOfRange(offset1 + offset2 + 1, data.lastIndex).reversedArray())

    return RuntimeDispatchInfo(Weight(refTime, proofSize), classValue, partialFee)
}

data class RuntimeDispatchInfo (
    val weight: Weight,
    val `class`: Int,
    val partialFee: BigInteger
)

enum class CallState(val methodName: String) {
    TransactionPaymentApiQueryInfo("TransactionPaymentApi_query_info")
}

fun Provider.callStateTransactionPayment(extrinsic: SubmittableExtrinsic): RpcRequest<RuntimeDispatchInfo, RpcError> {
    val extrinsicU8a = extrinsic.toU8a()
    val extrinsicSizeU8a = bnToU8a(extrinsicU8a.size.toBigInteger(), 32)
    val extrinsicU8aHex = u8aConcatStrict(arrayListOf(extrinsicU8a, extrinsicSizeU8a)).toByteString().hex()
    val updatedParams = arrayOf(CallState.TransactionPaymentApiQueryInfo.methodName, "0x$extrinsicU8aHex")

    return RpcCall(client, RpcMethod.StateCall.methodName, updatedParams) { it ->
        val decodedByte = it.readHexByteArray()
        decodeU8aRuntimeDispatchInfo(decodedByte)
    }
}