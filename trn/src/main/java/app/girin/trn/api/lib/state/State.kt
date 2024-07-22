package app.girin.trn.api.lib.state

import app.girin.trn.RpcMethod
import io.ethers.providers.Provider
import io.ethers.providers.RpcError
import io.ethers.providers.types.RpcCall
import io.ethers.providers.types.RpcRequest
import java.lang.reflect.Type

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