package app.girin.trn.api.lib.types

interface Method {
    val callIndex: ByteArray
    val args: Any
    fun toU8a(): ByteArray
}


