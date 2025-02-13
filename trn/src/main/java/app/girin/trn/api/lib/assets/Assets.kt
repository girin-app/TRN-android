package app.girin.trn.api.lib.assets

import app.girin.trn.util.decodeCompactLength
import java.math.BigInteger

data class AssetMetadata(
    val deposit: BigInteger,
    val name: String,
    val symbol: String,
    val decimals: Int,
    val isFrozen: Boolean,
) {
    companion object {
        fun decode(bytes: ByteArray): AssetMetadata {
            return decodeAssetMetadata(bytes)
        }
    }
}

private fun decodeAssetMetadata(bytes: ByteArray): AssetMetadata {
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

    return AssetMetadata(deposit, name, symbol, decimals, isFrozen)
}