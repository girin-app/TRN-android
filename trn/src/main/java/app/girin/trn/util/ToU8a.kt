package app.girin.trn.util

import java.math.BigInteger

val MAX_U8 = BigInteger("2").pow(6) // 64
val MAX_U16 = BigInteger("2").pow(14) // 16384
val MAX_U32 = BigInteger("2").pow(30) // 1073741824

fun bnToU8aLittleEndian(value: BigInteger, bitLength: Int): ByteArray {
    val data = bnToU8a(value)
    val paddedData = data + ByteArray(bitLength / 8 - data.size) { 0 }
    return paddedData
}

fun compactToU8a(value: BigInteger): ByteArray {
    return when {
        value <= MAX_U8 -> byteArrayOf((value shl 2).toByte())
        value <= MAX_U16 -> {
            val shiftedValue = (value shl 2) + BigInteger.ONE
            bnToU8aLittleEndian(shiftedValue, 16)
        }
        value <= MAX_U32 -> {
            val shiftedValue = (value shl 2) + BigInteger("2")
            bnToU8aLittleEndian(shiftedValue, 32)
        }
        else -> {
            val u8a = bnToU8a(value)
            var length = u8a.size
            while (u8a[length - 1] == 0.toByte()) {
                length--
            }
            if (length < 4) throw Exception("Invalid length, previous checks match anything less than 2^30")
            u8aConcatStrict(listOf(byteArrayOf((((length - 4) shl 2) + 0b11).toByte()), u8a.copyOfRange(0, length)))
        }
    }
}

fun bnToU8a(value: BigInteger, bitLength: Int = -1, isLe: Boolean = true): ByteArray {
    val byteLength = if (bitLength == -1) (value.bitLength() + 7) / 8 else (bitLength + 7) / 8
    if (value == BigInteger.ZERO) {
        return if (bitLength == -1) byteArrayOf(0) else ByteArray(byteLength)
    }
    var output = ByteArray(byteLength)
    val bnArray = value.toByteArray()
    if (isLe) {
        bnArray.reverse()
        System.arraycopy(bnArray, 0, output, 0, bnArray.size.coerceAtMost(byteLength))
    } else {
        System.arraycopy(bnArray, 0, output, 0, bnArray.size.coerceAtMost(byteLength))
    }
    return output
}

fun u8aConcatStrict(arr: List<ByteArray>): ByteArray {
    var length = 0
    for (u8a in arr) {
        length += u8a.size
    }
    val output = ByteArray(length)
    var offset = 0
    for (u8a in arr) {
        System.arraycopy(u8a, 0, output, offset, u8a.size)
        offset += u8a.size
    }
    return output
}