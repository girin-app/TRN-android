package app.girin.trn.util

import org.junit.Assert.assertEquals
import org.junit.Test

class Blake2B128EncoderTest {

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun encodeU32Test() {
        val actual = "d82c12285b5d4551f88e8f6e7eb52b81"
        val encoded = Blake2b128Encoder.encodeU32(1U)

        assertEquals(actual, encoded.toHexString())
    }
}