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

    @Test
    fun testU8aToBn() {
        val res = u8aToBn(byteArrayOf(0x00))
        Assert.assertEquals(BigInteger.ZERO, res)
    }

    @Test
    fun testU8aToBn1() {
        val res = u8aToBn(byteArrayOf(0xff.toByte()))
        Assert.assertEquals(BigInteger("255"), res)
    }

    @Test
    fun testU8aToBn2() {
        val res = u8aToBn(byteArrayOf(0x1.toByte(), 0x02.toByte()))
        Assert.assertEquals(BigInteger("513"), res)
    }

    @Test
    fun testU8aToBn3() {
        val res = u8aToBn(byteArrayOf(0x1.toByte(), 0x02.toByte(), 0x03.toByte()))
        Assert.assertEquals(BigInteger("197121"), res)
    }

    @Test
    fun testU8aToBn4() {
        val res = u8aToBn(byteArrayOf(0x1.toByte(), 0x02.toByte(), 0x03.toByte(), 0x04.toByte()))
        Assert.assertEquals(BigInteger("67305985"), res)
    }

    @Test
    fun testU8aToBn5() {
        val res = u8aToBn(byteArrayOf(0x1.toByte(), 0x02.toByte(), 0x03.toByte(), 0x04.toByte(), 0x05.toByte()))
        Assert.assertEquals(BigInteger("21542142465"), res)
    }

    @Test
    fun testU8aToBn6() {
        val res = u8aToBn(byteArrayOf(0x1.toByte(), 0x02.toByte(), 0x03.toByte(), 0x04.toByte(), 0x05.toByte(), 0x06.toByte()))
        Assert.assertEquals(BigInteger("6618611909121"), res)
    }

    @Test
    fun testU8aToBn7() {
        val res = u8aToBn(byteArrayOf(0x1.toByte(), 0x02.toByte(), 0x03.toByte(), 0x04.toByte(), 0x05.toByte(), 0x06.toByte(), 0x07.toByte()))
        Assert.assertEquals(BigInteger("1976943448883713"), res)
    }

    @Test
    fun testU8aToBnWithBe6() {
        val res = u8aToBn(byteArrayOf(0x1.toByte(), 0x02.toByte(), 0x03.toByte(), 0x04.toByte(), 0x05.toByte(), 0x06.toByte()), isLe = false)
        Assert.assertEquals(BigInteger("1108152157446"), res)
    }

    @Test
    fun testU8aToBnWithBe7() {
        val res = u8aToBn(byteArrayOf(0x1.toByte(), 0x02.toByte(), 0x03.toByte(), 0x04.toByte(), 0x05.toByte(), 0x06.toByte(), 0x07.toByte()), isLe = false)
        Assert.assertEquals(BigInteger("283686952306183"), res)
    }
}