package app.girin.trn.util

import okio.ByteString.Companion.toByteString
import org.junit.Assert
import org.junit.Test
import java.math.BigInteger

class UtilTest {
    @Test
    fun testBnToU8a0() {
        val res = bnToU8a(BigInteger.ZERO)
        Assert.assertEquals("00", res.toByteString().hex())
    }

    @Test
    fun testBnToU8a1() {
        val res = bnToU8a(BigInteger.ONE)
        Assert.assertEquals("01", res.toByteString().hex())
    }

    @Test
    fun testBnToU8a2() {
        val res = bnToU8a(BigInteger("999"))
        Assert.assertEquals("e703", res.toByteString().hex())
    }

    @Test
    fun testBnToU8a3() {
        val res = bnToU8a(BigInteger("1000000"))
        Assert.assertEquals("40420f", res.toByteString().hex())
    }

    @Test
    fun testBnToU8a4() {
        val res = bnToU8a(BigInteger("1000000"), 128)
        Assert.assertEquals("40420f00000000000000000000000000", res.toByteString().hex())
    }

    @Test
    fun testBnToU8a5() {
        val res = bnToU8a(BigInteger("54"), 32)
        Assert.assertEquals("36000000", res.toByteString().hex())
    }

    @Test
    fun testBnToU8a6() {
        val res = bnToU8a(BigInteger("9"), 32)
        Assert.assertEquals("09000000", res.toByteString().hex())
    }

    @Test
    fun testcompactToU8a1() {
        val res = compactToU8a(BigInteger("65"))
        Assert.assertEquals("0501", res.toByteString().hex())
    }

    @Test
    fun testcompactToU8a2() {
        val res = compactToU8a(BigInteger("16383"))
        Assert.assertEquals("fdff", res.toByteString().hex())
    }

    @Test
    fun testcompactToU8a3() {
        val res = compactToU8a(BigInteger("16386"))
        Assert.assertEquals("0a000100", res.toByteString().hex())
    }

    @Test
    fun testcompactToU8a4() {
        val res = compactToU8a(BigInteger("17386"))
        Assert.assertEquals("aa0f0100", res.toByteString().hex())
    }

    @Test
    fun testcompactToU8a5() {
        val res = compactToU8a(BigInteger("1073741830"))
        Assert.assertEquals("0306000040", res.toByteString().hex())
    }

    @Test
    fun testcompactToU8a6() {
        val res = compactToU8a(BigInteger("51"))
        Assert.assertEquals("cc", res.toByteString().hex())
    }

    @Test
    fun testcompactToU8a7() {
        val res = compactToU8a(BigInteger("65"))
        Assert.assertEquals("0501", res.toByteString().hex())
    }

    @Test
    fun testcompactToU8a8() {
        val res = compactToU8a(BigInteger("2073741830"))
        Assert.assertEquals("0306ca9a7b", res.toByteString().hex())
    }
}