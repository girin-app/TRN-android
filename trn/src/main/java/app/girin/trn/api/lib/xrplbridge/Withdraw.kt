package app.girin.trn.api.lib.xrplbridge

import app.girin.trn.api.lib.types.H160
import app.girin.trn.api.lib.types.Method
import app.girin.trn.api.lib.types.u128
import app.girin.trn.api.lib.types.u32
import app.girin.trn.util.u128ToU8a
import app.girin.trn.util.u32ToU8a
import io.ethers.core.FastHex

/**
 * function withdraw(
 *   asset_id: u32,
 *   amount: u128,
 *   destination: H160,
 *   destination_tag: Option<u32>
 * )
 */
class Withdraw(
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

    class WithdrawArgs(
        val assetId: u32,
        val amount: u128,
        val destination: H160,
        val destinationTag: u32? = null
    ) {
        val byteLength = if (destinationTag == null) 41 else 44
    }

    companion object {
        fun create(
            assetId: u32,
            amount: u128,
            destination: H160,
            destinationTag: u32? = null
        ): Method {
            return Withdraw(
                args = WithdrawArgs(
                    assetId = assetId,
                    amount = amount,
                    destination = destination,
                    destinationTag = destinationTag
                )
            )
        }
    }
}