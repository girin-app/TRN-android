package app.girin.trn.rpc

import io.ethers.core.readHexByteArray
import io.ethers.core.types.Address
import io.ethers.core.types.Hash
import io.ethers.providers.Provider
import io.ethers.providers.RpcError
import io.ethers.providers.types.RpcCall
import io.ethers.providers.types.RpcRequest

fun Provider.nextIndex(address: Address): RpcRequest<Int, RpcError> {
    return RpcCall(client, RpcMethod.AccountNextIndex.methodName, arrayOf(address), Int::class.java)
}

fun Provider.stateGetStorage(encodedData: String): RpcRequest<ByteArray, RpcError> {
    return RpcCall(
        client,
        RpcMethod.StateGetStorage.methodName,
        arrayOf(encodedData)
    ) { it.readHexByteArray() }
}

fun Provider.submitExtrinsic(encodedData: String): RpcRequest<Hash, RpcError> {
    return RpcCall(
        client,
        RpcMethod.AuthorSubmitExtrinsic.methodName,
        arrayOf(encodedData),
        Hash::class.java
    )
}