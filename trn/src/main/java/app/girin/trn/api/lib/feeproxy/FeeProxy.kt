package app.girin.trn.api.lib.feeproxy

import app.girin.trn.api.lib.types.Method
import app.girin.trn.util.bnToU8a
import io.ethers.core.FastHex
import java.math.BigInteger

class FeeProxy(
    override val callIndex: ByteArray = FastHex.decode("1f00"),
    override var args: FeeProxyArgs
) : Method {

    override fun toU8a(): ByteArray {
        var u8a = callIndex
        u8a += bnToU8a(args.paymentAsset, 32)
        u8a += bnToU8a(args.maxPayment, 128)
        u8a += args.call.toU8a()
        return u8a
    }

    data class FeeProxyArgs(
        val paymentAsset: BigInteger,
        var maxPayment: BigInteger,
        val call: Method
    )
}