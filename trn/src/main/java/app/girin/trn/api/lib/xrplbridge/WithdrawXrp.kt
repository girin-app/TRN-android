package app.girin.trn.api.lib.xrplbridge

import app.girin.trn.XRP_ID
import app.girin.trn.api.lib.types.H160
import app.girin.trn.api.lib.types.Call
import app.girin.trn.api.lib.types.u128
import app.girin.trn.api.lib.types.u32

@Deprecated("use MethodWithdraw instead")
object WithdrawXrp {

    fun create(amount: u128, destination: H160, destinationTag: u32? = null): Call {
        return Withdraw.create(
            assetId = XRP_ID.toUInt(),
            destination = destination,
            destinationTag = destinationTag,
            amount = amount
        )
    }
}