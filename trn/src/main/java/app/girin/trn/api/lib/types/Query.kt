package app.girin.trn.api.lib.types

interface Query {
    val moduleAndMethod: ByteArray
    val args: Any

    fun encode(): ByteArray

    @OptIn(ExperimentalStdlibApi::class)
    fun getStorageKey(): String = encode().toHexString()
}

