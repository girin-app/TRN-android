package app.girin.trn.api.lib.types

import app.girin.trn.util.bnToU8a
import io.ethers.core.FastHex
import io.ethers.core.types.Address
import java.math.BigInteger


interface Method {
    val callIndex: ByteArray
    fun toU8a(): ByteArray
}

data class MethodWithdrawXrp(
    override val callIndex: ByteArray = FastHex.decode("1203"),
    val args: WithdrawXrpArgs
) : Method {
    override fun toU8a(): ByteArray {
        var u8a = callIndex
        u8a += bnToU8a(args.amount, 128)
        u8a += args.destination.toByteArray()
        return u8a
    }
}

data class WithdrawXrpArgs(
    val amount: BigInteger,
    val destination: Address
)

data class MethodFeeProxy(
    override val callIndex: ByteArray = FastHex.decode("1f00"),
    val args: FeeProxyArgs
) : Method {

    override fun toU8a(): ByteArray {
        var u8a = callIndex
        u8a += bnToU8a(args.paymentAsset, 32)
        u8a += bnToU8a(args.maxPayment, 128)
        u8a += args.call.toU8a()
        return u8a
    }
}

data class FeeProxyArgs (
    val paymentAsset: BigInteger,
    val maxPayment: BigInteger,
    val call: MethodWithdrawXrp
)