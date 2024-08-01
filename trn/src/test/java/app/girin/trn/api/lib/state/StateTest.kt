package app.girin.trn.api.lib.state

import app.girin.trn.NetworkName
import app.girin.trn.api.lib.types.FeeProxyArgs
import app.girin.trn.api.lib.types.MethodFeeProxy
import app.girin.trn.api.lib.types.MethodWithdrawXrp
import app.girin.trn.api.lib.types.MortalEra
import app.girin.trn.api.lib.types.Signature
import app.girin.trn.api.lib.types.SubmittableExtrinsic
import app.girin.trn.api.lib.types.WithdrawXrpArgs
import app.girin.trn.getPublicProviderInfo
import io.ethers.core.FastHex
import io.ethers.core.types.Address
import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import org.junit.Assert
import org.junit.Test
import java.math.BigInteger

class StateTest {
    @Test
    fun testGetRuntimeVersion() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, true)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val runtimeVersion = provider.getRuntimeVersion().sendAwait().unwrap()
        
        Assert.assertEquals(runtimeVersion.specName, "root")
        Assert.assertEquals(runtimeVersion.implName, "root")
        Assert.assertEquals(runtimeVersion.authoringVersion, 1)
        Assert.assertEquals(runtimeVersion.specVersion, 55)
        Assert.assertEquals(runtimeVersion.implVersion, 0)
        Assert.assertEquals(runtimeVersion.transactionVersion, 10)
        Assert.assertEquals(runtimeVersion.stateVersion, 0)
        Assert.assertEquals(runtimeVersion.apis.count(), 17)
    }

    @Test
    fun testStateCall() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, true)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val extrinsic = SubmittableExtrinsic(
            Signature(
                signer = Address("0x55D77A60Fd951117f531D2277a5BB4aFbE3fB292"),
                era =  MortalEra(FastHex.decode("2603")),
                nonce =  BigInteger("83"),
                tip =  BigInteger.ZERO
            ),
            MethodFeeProxy(
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
        )

        val runtimeDispatchInfo = provider.callStateTransactionPayment(extrinsic).sendAwait().unwrap()
        Assert.assertEquals(runtimeDispatchInfo.weight.refTime, BigInteger("1285926000"))
        Assert.assertEquals(runtimeDispatchInfo.weight.proofSize, BigInteger("13627"))
        Assert.assertEquals(runtimeDispatchInfo.`class`, 1)
        Assert.assertEquals(runtimeDispatchInfo.partialFee, BigInteger("65991"))
    }

    @Test
    fun testDecodeU8aRuntimeDispatchInfo() {
        val u8a: ByteArray = FastHex.decode("0370aca54cedd401c7010100000000000000000000000000")
        val runtimeDispatchInfo = decodeU8aRuntimeDispatchInfo(u8a)

        Assert.assertEquals(runtimeDispatchInfo.weight.refTime, BigInteger("1285926000"))
        Assert.assertEquals(runtimeDispatchInfo.weight.proofSize, BigInteger("13627"))
        Assert.assertEquals(runtimeDispatchInfo.`class`, 1)
        Assert.assertEquals(runtimeDispatchInfo.partialFee, BigInteger("65991"))
    }
}