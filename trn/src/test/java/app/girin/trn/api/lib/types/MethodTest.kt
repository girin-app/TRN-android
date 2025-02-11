package app.girin.trn.api.lib.types

import io.ethers.core.types.Address
import okio.ByteString.Companion.toByteString
import org.junit.Assert
import org.junit.Test
import java.math.BigInteger

class MethodTest {
    @Test
    fun testFeeProxyMethod() {
        val method = MethodFeeProxy(
            args = FeeProxyArgs(
                BigInteger.ONE,
                BigInteger.ZERO,
                MethodWithdrawXrp(
                    args = WithdrawXrpArgs(
                        BigInteger("1000000"),
                        Address("0x72ee785458b89d5ec64bec8410c958602e6f7673")
                    )
                )
            )
        )

        Assert.assertEquals("1f000100000000000000000000000000000000000000120340420f0000000000000000000000000072ee785458b89d5ec64bec8410c958602e6f7673", method.toU8a().toByteString().hex())
    }

    @Test
    fun testWithdrawXrpMethod() {
        val method = MethodWithdrawXrp(
            args = WithdrawXrpArgs(
                amount = BigInteger("1000000"),
                destination = Address("0x72ee785458b89d5ec64bec8410c958602e6f7673")
            )
        )

        Assert.assertEquals("120340420f0000000000000000000000000072ee785458b89d5ec64bec8410c958602e6f7673", method.toU8a().toByteString().hex())
    }
    @Test
    fun testWithdrawMethod() {
        val method = MethodWithdraw(
            args = WithdrawArgs(
                assetId = 0x1234.toUInt(),
                amount = BigInteger("1000000"),
                destination = Address("0x72ee785458b89d5ec64bec8410c958602e6f7673")
            )
        )

        Assert.assertEquals("120f3412000040420f0000000000000000000000000072ee785458b89d5ec64bec8410c958602e6f767300", method.toU8a().toByteString().hex())
    }
}