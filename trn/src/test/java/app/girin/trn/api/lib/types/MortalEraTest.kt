package app.girin.trn.api.lib.types

import okio.ByteString.Companion.toByteString
import org.junit.Assert
import org.junit.Test

class MortalEraTest {
    @Test
    fun TestMortalEra() {
        val res = Mortal(80, 14068466)
        Assert.assertEquals("2607", res.toU8a().toByteString().hex())
    }

    @Test
    fun TestMortalEra2() {
        val res = Mortal(80, 14138070)
        Assert.assertEquals("6605", res.toU8a().toByteString().hex())
    }

    @Test
    fun TestMortalEra3() {
        val res = Mortal(80, 14138184)
        Assert.assertEquals("8604", res.toU8a().toByteString().hex())
    }
}