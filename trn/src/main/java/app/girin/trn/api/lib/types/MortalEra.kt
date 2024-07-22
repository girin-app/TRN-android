package app.girin.trn.api.lib.types

class Mortal(private var period: Long = 80, private val current: Long) {
    val quantizedPhase: Long

    init {
        val nextPowerOfTwo = period.nextPowerOfTwo()
        val clampedPeriod = nextPowerOfTwo.coerceIn(4, 1 shl 16)
        val phase = current % clampedPeriod
        val quantizeFactor = maxOf(clampedPeriod shr 12, 1)
        quantizedPhase = (phase / quantizeFactor) * quantizeFactor
        period = clampedPeriod
    }

    fun toMortalEra(): MortalEra {
        return MortalEra(toU8a())
    }

    fun toU8a(): ByteArray {
        val periodInt = period.toInt()
        val trailingZeros = maxOf(1, periodInt.countTrailingZeroBits() - 1)
        val encoded = minOf(15, trailingZeros) + ((quantizedPhase.toInt() / maxOf(periodInt shr 12, 1)) shl 4)
        return byteArrayOf(
            (encoded and 0xff).toByte(),
            (encoded shr 8).toByte()
        )
    }

    private fun Long.nextPowerOfTwo(): Long {
        var value = this
        value--
        value = value or (value shr 1)
        value = value or (value shr 2)
        value = value or (value shr 4)
        value = value or (value shr 8)
        value = value or (value shr 16)
        value = value or (value shr 32)
        return value + 1
    }
}