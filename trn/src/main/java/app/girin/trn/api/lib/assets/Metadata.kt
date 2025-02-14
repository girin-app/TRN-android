package app.girin.trn.api.lib.assets

import app.girin.trn.api.lib.types.Query
import app.girin.trn.api.lib.types.u32
import app.girin.trn.rpc.RpcMethod
import app.girin.trn.util.Blake2b128Encoder
import app.girin.trn.util.decodeCompactLength
import app.girin.trn.util.u32ToU8a
import io.ethers.core.FastHex
import io.ethers.core.readHexByteArray
import io.ethers.providers.Provider
import io.ethers.providers.RpcError
import io.ethers.providers.types.RpcCall
import io.ethers.providers.types.RpcRequest
import java.math.BigInteger

data class Metadata(
    val deposit: BigInteger,
    val name: String,
    val symbol: String,
    val decimals: Int,
    val isFrozen: Boolean,
) {
    companion object {
        fun decode(bytes: ByteArray): Metadata {
            return decodeU8aAssetMetadata(bytes)
        }
    }
}

class QueryMetadata(
    override val moduleAndMethod: ByteArray = FastHex.decode("682a59d51ab9e48a8c8cc418ff9708d2b5f3822e35ca2f31ce3526eab1363fd2"),
    override val args: QueryMetadataArgs
) : Query {

    override fun encode(): ByteArray {
        return moduleAndMethod + args.encode()
    }

    companion object {
        fun create(assetId: u32): QueryMetadata {
            return QueryMetadata(
                args = QueryMetadataArgs(assetId)
            )
        }
    }

    class QueryMetadataArgs(
        private val assetId: u32
    ) {
        fun encode(): ByteArray {
            return Blake2b128Encoder.encodeU32(assetId) + u32ToU8a(assetId)
        }
    }
}

fun decodeU8aAssetMetadata(bytes: ByteArray): Metadata {
    var offset = 0

    // u128 (deposit)
    val depositLength = 16
    val depositBytes = bytes.copyOfRange(offset, depositLength)
    val deposit = BigInteger(1, depositBytes) // 1 for positive number
    offset += depositLength

    // Bytes (name)
    val namePrefixLength = 1
    val nameLength = decodeCompactLength(bytes[offset].toInt())
    offset += namePrefixLength
    val nameBytes = bytes.copyOfRange(offset, offset + nameLength)
    val name = nameBytes.decodeToString()
    offset += nameLength

    // Bytes (symbol)
    val symbolPrefixLength = 1
    val symbolLength = decodeCompactLength(bytes[offset].toInt())
    offset += symbolPrefixLength
    val symbolBytes = bytes.copyOfRange(offset, offset + symbolLength)
    val symbol = symbolBytes.decodeToString()
    offset += symbolLength

    // u8 (decimals)
    val decimalsLength = 1
    val decimals = bytes[offset].toInt()
    offset += decimalsLength

    // bool (isFrozen)
    val isFrozen = bytes[offset].toInt() != 0

    return Metadata(deposit, name, symbol, decimals, isFrozen)
}

fun Provider.getMetadata(query: QueryMetadata): RpcRequest<Metadata, RpcError> {
    return RpcCall(
        client,
        RpcMethod.StateGetStorage.methodName,
        arrayOf(query.getStorageKey())
    ) {
        it.readHexByteArray().let { Metadata.decode(it) }
    }
}