package app.girin.trn.api.lib.types

interface Call {
    val callIndex: ByteArray
    val args: Any
    fun toU8a(): ByteArray
}


