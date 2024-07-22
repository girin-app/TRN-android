package app.girin.trn.api.lib.author

import app.girin.trn.RpcMethod
import io.ethers.core.types.Hash
import io.ethers.providers.Provider
import io.ethers.providers.RpcError
import io.ethers.providers.types.RpcCall
import io.ethers.providers.types.RpcRequest

fun Provider.submitExtrinsic(encodedData: String): RpcRequest<Hash, RpcError> {
    return RpcCall(client, RpcMethod.AuthorSubmitExtrinsic.methodName, arrayOf(encodedData), Hash::class.java)
}