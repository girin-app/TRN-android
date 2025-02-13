package app.girin.trn.api.lib.feeproxy

import app.girin.trn.api.lib.types.Call
import app.girin.trn.api.lib.types.u128
import app.girin.trn.api.lib.types.u32
import app.girin.trn.util.u128ToU8a
import app.girin.trn.util.u32ToU8a
import io.ethers.core.FastHex

class FeeProxy(
    override val callIndex: ByteArray = FastHex.decode("1f00"),
    override val args: FeeProxyArgs
) : Call {

    override fun toU8a(): ByteArray {
        val call = args.call.toU8a()
        val u8a = ByteArray(22 + call.size) // 2 + 4 + 16 + call
        callIndex.copyInto(u8a) //offset: 0
        u32ToU8a(args.paymentAsset).copyInto(u8a, 2)
        u128ToU8a(args.maxPayment).copyInto(u8a, 6)
        call.copyInto(u8a, 22)
        return u8a
    }

    data class FeeProxyArgs(
        val paymentAsset: u32,
        val maxPayment: u128,
        val call: Call
    )

    fun withMaxPayment(maxPayment: u128): FeeProxy {
        return FeeProxy(args = args.copy(maxPayment = maxPayment))
    }

    companion object {
        fun create(paymentAsset: u32, maxPayment: u128, call: Call): FeeProxy {
            return FeeProxy(
                args = FeeProxyArgs(paymentAsset, maxPayment, call)
            )
        }
    }
}