package app.girin.trn.evm.lib.dex

import app.girin.trn.rpc.RpcMethod
import app.girin.trn.XRP_ID
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.ethers.providers.Provider
import io.ethers.providers.RpcError
import io.ethers.providers.types.RpcCall
import io.ethers.providers.types.RpcRequest
import java.io.IOException
import java.math.BigInteger

@JsonDeserialize(using = AmountInDeserializer::class)
data class AmountIn(val Ok: List<Int>)

class AmountInDeserializer : JsonDeserializer<AmountIn>() {
    @Throws(IOException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): AmountIn {
        val node: JsonNode = p.codec.readTree(p)
        val okList = node.get("Ok").map { it.asInt() }.toList()
        return AmountIn(okList)
    }
}

fun Provider.getAmountIn(gasCostInXRP: BigInteger, feeAssetID: Int): RpcRequest<Int, RpcError> {
    return RpcCall(client, RpcMethod.DexGetAmountsIn.methodName, arrayOf(gasCostInXRP, arrayOf(feeAssetID, XRP_ID))) { lt ->
        lt.readValueAs(AmountIn::class.java).Ok[0]
    }
}