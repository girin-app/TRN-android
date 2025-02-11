package app.girin.trn.api.lib.types

import app.girin.trn.util.bnToU8a
import app.girin.trn.util.u128ToU8a
import app.girin.trn.util.u32ToU8a
import io.ethers.core.FastHex
import io.ethers.core.types.Address
import java.math.BigInteger

typealias u32 = UInt
typealias u128 = BigInteger
typealias H160 = Address

interface Method {
    val callIndex: ByteArray
    val args: Any
    fun toU8a(): ByteArray
}

@Deprecated("use MethodWithdraw instead")
class MethodWithdrawXrp(
    override val callIndex: ByteArray = FastHex.decode("1203"),
    override val args: WithdrawXrpArgs
) : Method {
    override fun toU8a(): ByteArray {
        var u8a = callIndex
        u8a += bnToU8a(args.amount, 128)
        u8a += args.destination.toByteArray()
        return u8a
    }
}

@Deprecated("use WithdrawArgs instead")
data class WithdrawXrpArgs(
    val amount: BigInteger,
    val destination: Address
)

/**
 * function withdraw(
 *   asset_id: u32,
 *   amount: u128,
 *   destination: H160,
 *   destination_tag: Option<u32>
 * )
 */
class MethodWithdraw(
    override val callIndex: ByteArray = FastHex.decode("120f"),
    override val args: WithdrawArgs
) : Method {

    override fun toU8a(): ByteArray {
        val u8a = ByteArray(2 + args.byteLength)
        callIndex.copyInto(u8a) //offset: 0
        u32ToU8a(args.assetId).copyInto(u8a, 2)
        u128ToU8a(args.amount).copyInto(u8a, 6)
        args.destination.toByteArray().copyInto(u8a, 22)
        args.destinationTag?.let { destinationTag ->
            u32ToU8a(destinationTag).copyInto(u8a, 42)
        } ?: run {
            u8a[42] = 0
        }
        return u8a
    }
}

class WithdrawArgs(
    val assetId: u32,
    val amount: u128,
    val destination: H160,
    val destinationTag: u32? = null
) {
    val byteLength = if (destinationTag == null) 41 else 44
}

class MethodFeeProxy(
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
}

data class FeeProxyArgs(
    val paymentAsset: BigInteger,
    var maxPayment: BigInteger,
    val call: Method
)