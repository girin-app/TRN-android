package app.girin.trn.api.lib.chain

import app.girin.trn.RpcMethod
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.ethers.core.types.Hash
import io.ethers.providers.Provider
import io.ethers.providers.RpcError
import io.ethers.providers.types.RpcCall
import io.ethers.providers.types.RpcRequest
import java.io.IOException
import java.math.BigInteger

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetBlockResponse(
    val block: Block
)

data class Block (
    val header: Header,
    var extrinsics: List<String>
)

@JsonDeserialize(using = HeaderDeserializer::class)
data class Header(
    val parentHash: Hash,
    val number: BigInteger,
    val stateRoot: Hash,
    val extrinsicsRoot: Hash,
    val digest: Log
)

class HeaderDeserializer : JsonDeserializer<Header>() {
    @Throws(IOException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Header {
        val node: JsonNode = p.codec.readTree(p)
        val numString = node.get("number").asText().removePrefix("0x")
        val num = BigInteger(numString, 16)
        return Header(
            parentHash = Hash(node.get("parentHash").asText()),
            number = num,
            stateRoot = Hash(node.get("stateRoot").asText()),
            extrinsicsRoot = Hash(node.get("extrinsicsRoot").asText()),
            digest = Log(node.get("digest").get("logs").map { it.asText() })
        )
    }
}

data class Log (
    val logs: List<String>
)

fun Provider.getBlock(hash: Hash): RpcRequest<GetBlockResponse, RpcError> {
    return RpcCall(client, RpcMethod.ChainGetBlock.methodName, arrayOf(hash), GetBlockResponse::class.java)
}

fun Provider.getFinalizedHead(): RpcRequest<Hash, RpcError> {
    return RpcCall(client, RpcMethod.ChainGetFinalizedHead.methodName, emptyArray<String>(), Hash::class.java)
}