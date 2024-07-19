package app.girin.trn.evm.lib

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

enum class NetworkName {
    ROOT, PORCINI
}

data class ProviderInfo(val url: String, val chainId: Long)

fun getPublicProviderInfo(network: NetworkName): ProviderInfo {
    return when (network) {
        NetworkName.ROOT -> ProviderInfo("https://root.rootnet.live/archive", 7668)
        NetworkName.PORCINI -> ProviderInfo("https://porcini.rootnet.app/archive", 7672)
    }
}

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

fun Provider.getAmountIn(gasCostInXRP: BigInteger, feeAssetID: Int): RpcRequest<AmountIn, RpcError> {
    return RpcCall(client, "dex_getAmountsIn", arrayOf(gasCostInXRP, arrayOf(feeAssetID, XRP_ID)), AmountIn::class.java)
}