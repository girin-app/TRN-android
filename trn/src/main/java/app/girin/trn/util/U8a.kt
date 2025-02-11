package app.girin.trn.util

import app.girin.trn.api.lib.types.u128
import app.girin.trn.api.lib.types.u32
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
            u8aConcatStrict(
                listOf(
                    byteArrayOf((((length - 4) shl 2) + 0b11).toByte()),
                    u8a.copyOfRange(0, length)
                )
            )
        }
    }
}

fun bnToU8a(value: BigInteger, bitLength: Int = -1, isLe: Boolean = true): ByteArray {
    val byteLength = if (bitLength == -1) (value.bitLength() + 7) / 8 else (bitLength + 7) / 8
    if (value == BigInteger.ZERO) {
        return if (bitLength == -1) byteArrayOf(0) else ByteArray(byteLength)
    }
    val output = ByteArray(byteLength)
    val bnArray = value.toByteArray()
    if (isLe) {
        bnArray.reverse()
        System.arraycopy(bnArray, 0, output, 0, bnArray.size.coerceAtMost(byteLength))
    } else {
        System.arraycopy(bnArray, 0, output, 0, bnArray.size.coerceAtMost(byteLength))
    }
    return output
}

fun u32ToU8a(value: u32, isLe: Boolean = true): ByteArray {
    val size = 4
    val output = ByteArray(size)
    if (isLe) {
        repeat(size) { index ->
            output[index] = (value shr (index * 8)).toByte()
        }
    } else {
        repeat(size) { index ->
            val beShiftRight = (size - index - 1)
            output[index] = (value shr (beShiftRight * 8)).toByte()
        }
    }
    return output
}

fun u128ToU8a(value: u128, isLe: Boolean = true): ByteArray = bnToU8a(value, 128, isLe)

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

fun decodeCompact(u8a: ByteArray): Pair<Int, BigInteger> {
    return if ((u8a[0].toUByte().toUInt() and 0b11u) < 0b11u) {
        compactFromU8aLim(u8a)
    } else {
        compactFromU8a(u8a)
    }
}

fun compactFromU8a(u8a: ByteArray): Pair<Int, BigInteger> {
    return when (u8a[0].toUByte().toUInt() and 0b11u) {
        0b00u -> Pair(1, BigInteger.valueOf((u8a[0].toUByte().toLong() ushr 2)))
        0b01u -> Pair(
            2,
            BigInteger.valueOf(
                (u8a[0].toUByte().toLong() + (u8a[1].toUByte().toLong() shl 8)) ushr 2
            )
        )

        0b10u -> Pair(
            4,
            BigInteger.valueOf(
                (u8a[0].toUByte().toLong() + (u8a[1].toUByte().toLong() shl 8) + (u8a[2].toUByte()
                    .toLong() shl 16) + (u8a[3].toUByte().toLong() * 16777216)) ushr 2
            )
        )

        else -> {
            val offset = ((u8a[0].toUByte().toLong() ushr 2) + 5).toInt()
            when (offset) {
                5 -> Pair(
                    5,
                    BigInteger.valueOf(
                        u8a[1].toLong() + (u8a[2].toUByte().toLong() shl 8) + (u8a[3].toUByte()
                            .toLong() shl 16) + (u8a[4].toUByte().toLong() * 16777216)
                    )
                )

                6 -> Pair(
                    6,
                    BigInteger.valueOf(
                        u8a[1].toUByte().toLong() + (u8a[2].toUByte()
                            .toLong() shl 8) + (u8a[3].toUByte()
                            .toLong() shl 16) + ((u8a[4].toUByte().toLong() + (u8a[5].toUByte()
                            .toLong() shl 8)) * 16777216)
                    )
                )

                7 -> Pair(
                    7,
                    BigInteger.valueOf(
                        u8a[1].toUByte().toLong() + (u8a[2].toUByte()
                            .toLong() shl 8) + (u8a[3].toUByte()
                            .toLong() shl 16) + ((u8a[4].toUByte().toLong() + (u8a[5].toUByte()
                            .toLong() shl 8) + (u8a[6].toUByte().toLong() shl 16) * 16777216))
                    )
                )

                else -> Pair(offset, BigInteger(u8a.copyOfRange(1, offset)))
            }
        }
    }
}

fun compactFromU8aLim(u8a: ByteArray): Pair<Int, BigInteger> {
    return when (u8a[0].toUByte().toUInt() and 0b11u) {
        0b00u -> Pair(1, (u8a[0].toUByte().toLong() ushr 2).toBigInteger())
        0b01u -> Pair(
            2,
            ((u8a[0].toUByte().toUInt() + (u8a[1].toUByte()
                .toUInt() shl 8)).toLong() ushr 2).toBigInteger()
        )

        0b10u -> Pair(
            4,
            ((u8a[0].toUByte().toUInt() + (u8a[1].toUByte().toUInt() shl 8) + (u8a[2].toUByte()
                .toUInt() shl 16) + (u8a[3].toUByte()
                .toUInt() * 16777216u)).toLong() ushr 2).toBigInteger()
        )

        else -> {
            val offset = ((u8a[0].toUByte().toLong() ushr 2) + 5).toUInt()
            when (offset) {
                5u -> Pair(
                    5,
                    BigInteger(
                        (u8a[1].toUByte().toUInt() + (u8a[2].toUByte()
                            .toUInt() shl 8) + (u8a[3].toUByte()
                            .toUInt() shl 16) + (u8a[4].toUByte()
                            .toUInt() * 16777216u)).toString(10)
                    )
                )

                6u -> Pair(
                    6,
                    BigInteger(
                        (u8a[1].toUByte().toUInt() + (u8a[2].toUByte()
                            .toUInt() shl 8) + (u8a[3].toUByte()
                            .toUInt() shl 16) + ((u8a[4].toUByte().toUInt() + (u8a[5].toUByte()
                            .toUInt() shl 8)) * 16777216u)).toString(10)
                    )
                )

                7u -> Pair(
                    7,
                    BigInteger(
                        (u8a[1].toUByte().toUInt() + (u8a[2].toUByte()
                            .toUInt() shl 8) + (u8a[3].toUByte()
                            .toUInt() shl 16) + ((u8a[4].toUByte().toUInt() + (u8a[5].toUByte()
                            .toUInt() shl 8) + (u8a[6].toUByte()
                            .toUInt() shl 16)) * 16777216u)).toString(10)
                    )
                )

                else -> throw Exception("Compact input is > Number.MAX_SAFE_INTEGER")
            }
        }
    }
}