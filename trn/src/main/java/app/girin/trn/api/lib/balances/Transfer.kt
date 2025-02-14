package app.girin.trn.api.lib.balances

import app.girin.trn.api.lib.types.Call
import app.girin.trn.api.lib.types.SeedPrimitivesSignatureAccountId20
import app.girin.trn.api.lib.types.u128
import app.girin.trn.util.compactToU8a
import io.ethers.core.FastHex

class Transfer(
    override val callIndex: ByteArray = FastHex.decode("0507"),
    override val args: TransferArgs
) : Call {

    class TransferArgs(
        val target: SeedPrimitivesSignatureAccountId20,
        val amount: u128,
    )

    override fun toU8a(): ByteArray {
        val amount = compactToU8a(args.amount)
        val u8a = ByteArray(2 + 20 + amount.size)
        callIndex.copyInto(u8a) //offset: 0
        args.target.toByteArray().copyInto(u8a, 2)
        amount.copyInto(u8a, 22)
        return u8a
    }

    companion object {
        fun create(
            target: SeedPrimitivesSignatureAccountId20,
            amount: u128
        ): Call {
            return Transfer(
                args = TransferArgs(
                    target = target,
                    amount = amount
                )
            )
        }
    }
}