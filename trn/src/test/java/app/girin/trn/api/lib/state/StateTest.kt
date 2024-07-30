package app.girin.trn.api.lib.state

import app.girin.trn.NetworkName
import app.girin.trn.getPublicProviderInfo
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
        Assert.assertEquals(runtimeVersion.transactionVersion, 9)
        Assert.assertEquals(runtimeVersion.stateVersion, 0)
        Assert.assertEquals(runtimeVersion.apis.count(), 17)
    }

    @Test
    fun testStateCall() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, true)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val res = provider.call(arrayOf("TransactionPaymentApi_query_info", "0x5d02841e06bd9904c05ec472d665b7e654b6265ba40c33010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010126034d01001f000100000000000000000000000000000000000000120340420f0000000000000000000000000072ee785458b89d5ec64bec8410c958602e6f767399000000")).sendAwait().unwrap()
        Assert.assertEquals("0x0370aca54cedd401c7010100000000000000000000000000", res)
    }
}