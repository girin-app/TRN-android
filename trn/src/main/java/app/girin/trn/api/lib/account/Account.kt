package app.girin.trn.api.lib.account

import app.girin.trn.RpcMethod
import io.ethers.core.types.Address
import io.ethers.providers.Provider
import io.ethers.providers.RpcError
import io.ethers.providers.types.RpcCall
import io.ethers.providers.types.RpcRequest

fun Provider.nextIndex(address: Address): RpcRequest<Int, RpcError> {
    return RpcCall(client, RpcMethod.AccountNextIndex.methodName, arrayOf(address), Int::class.java)
}