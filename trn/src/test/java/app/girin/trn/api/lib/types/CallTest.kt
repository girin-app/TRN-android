package app.girin.trn.api.lib.types

import app.girin.trn.ROOT_ID
import app.girin.trn.XRP_ID
import app.girin.trn.api.lib.feeproxy.FeeProxy
import app.girin.trn.api.lib.xrplbridge.Withdraw
import io.ethers.core.types.Address
import okio.ByteString.Companion.toByteString
import org.junit.Assert
import org.junit.Test
import java.math.BigInteger

class CallTest {
    @Test
    fun testFeeProxyMethod() {
        val method = FeeProxy.create(
                ROOT_ID.toUInt(),
                BigInteger.ZERO,
                Withdraw.create(
                    assetId = XRP_ID.toUInt(),
                    BigInteger("1000000"),
                    Address("0x72ee785458b89d5ec64bec8410c958602e6f7673")
                )
        )

        Assert.assertEquals(
            "1f000100000000000000000000000000000000000000120f0200000040420f0000000000000000000000000072ee785458b89d5ec64bec8410c958602e6f767300",
            method.toU8a().toByteString().hex()
        )
    }

    @Test
    fun testWithdrawXrpMethod() {
        val method = Withdraw.create(
            assetId = XRP_ID.toUInt(),
            amount = BigInteger("1000000"),
            destination = Address("0x72ee785458b89d5ec64bec8410c958602e6f7673")
        )

        Assert.assertEquals(
            "120f0200000040420f0000000000000000000000000072ee785458b89d5ec64bec8410c958602e6f767300",
            method.toU8a().toByteString().hex()
        )
    }

    @Test
    fun testWithdrawMethod() {
        val method = Withdraw.create(
            assetId = 0x1234.toUInt(),
            amount = BigInteger("1000000"),
            destination = Address("0x72ee785458b89d5ec64bec8410c958602e6f7673")
        )

        Assert.assertEquals(
            "120f3412000040420f0000000000000000000000000072ee785458b89d5ec64bec8410c958602e6f767300",
            method.toU8a().toByteString().hex()
        )
    }
}