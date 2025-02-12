package app.girin.trn

import io.ethers.core.types.Hash

val ROOT_ID = 1
val XRP_ID = 2
val RLUSD_ID = 0x26864

enum class NetworkName {
    ROOT, PORCINI
}

data class ProviderInfo(val url: String, val chainId: Long, val genesisHash: Hash)

fun getPublicProviderInfo(network: NetworkName, useWsProvider: Boolean, useArchiveNode: Boolean): ProviderInfo {
    val domain = when(network) {
        NetworkName.ROOT -> "root.rootnet.live"
        NetworkName.PORCINI -> "porcini.rootnet.app"
    }

    val chainId = when(network) {
        NetworkName.ROOT -> 7668
        NetworkName.PORCINI -> 7672
    }

    val genesisHash = when(network) {
        NetworkName.ROOT -> Hash("0x046e7cb5cdfee1b96e7bd59e051f80aeba61b030ce8c9275446e0209704fd338")
        NetworkName.PORCINI -> Hash("0x83959f7f4262762f7599c2fa48b418b7e102f92c81fab9e6ef22ab379abdb72f")
    }

    val protocol = if (useWsProvider) "wss" else "https"
    val path = if (useArchiveNode) "archive/" else ""
    val url = "$protocol://${domain}/$path"
    return ProviderInfo(url, chainId.toLong(), genesisHash)
}