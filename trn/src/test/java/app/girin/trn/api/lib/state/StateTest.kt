package app.girin.trn.api.lib.state

import app.girin.trn.NetworkName
import app.girin.trn.getPublicProviderInfo
import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import org.junit.Assert
import org.junit.Test

class StateTest {
    @Test
    fun testGetRuntimeVersion() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, true)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val runtimeVersion = provider.getRuntimeVersion().sendAwait().unwrap()
        
        Assert.assertEquals(runtimeVersion.specName, "root")
        Assert.assertEquals(runtimeVersion.implName, "root")
        Assert.assertEquals(runtimeVersion.authoringVersion, 1)
        Assert.assertEquals(runtimeVersion.specVersion, 54)
        Assert.assertEquals(runtimeVersion.implVersion, 0)
        Assert.assertEquals(runtimeVersion.transactionVersion, 9)
        Assert.assertEquals(runtimeVersion.stateVersion, 0)
        Assert.assertEquals(runtimeVersion.apis.count(), 17)
    }
}