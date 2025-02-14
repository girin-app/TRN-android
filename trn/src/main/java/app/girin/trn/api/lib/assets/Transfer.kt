package app.girin.trn.api.lib.assets

import app.girin.trn.api.lib.types.Call
import app.girin.trn.api.lib.types.SeedPrimitivesSignatureAccountId20
import app.girin.trn.api.lib.types.u128
import app.girin.trn.api.lib.types.u32
import app.girin.trn.util.compactToU8a
import app.girin.trn.util.u32ToU8a
import io.ethers.core.FastHex

class Transfer(
    override val callIndex: ByteArray = FastHex.decode("0608"),
    override val args: TransferArgs
) : Call {

    class TransferArgs(
        val assetId: u32,
        val target: SeedPrimitivesSignatureAccountId20,
        val amount: u128,
    )

    override fun toU8a(): ByteArray {
        val amount = compactToU8a(args.amount)
        val u8a = ByteArray(26 + amount.size)
        callIndex.copyInto(u8a) //offset: 0
        u32ToU8a(args.assetId).copyInto(u8a, 2)
        args.target.toByteArray().copyInto(u8a, 6)
        amount.copyInto(u8a, 26)
        return u8a
    }

    companion object {
        fun create(
            assetId: u32,
            target: SeedPrimitivesSignatureAccountId20,
            amount: u128
        ): Call {
            return Transfer(
                args = TransferArgs(
                    assetId = assetId,
                    target = target,
                    amount = amount
                )
            )
        }
    }
}