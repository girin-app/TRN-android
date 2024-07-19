package app.girin.trn.evm.lib

import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigInteger

class ProviderTest {
    @Test
    fun testGetPublicProviderURLRoot() {
        val providerInfo = getPublicProviderInfo(NetworkName.ROOT)
        assertEquals("https://root.rootnet.live/archive", providerInfo.url)
        assertEquals(7668, providerInfo.chainId)
    }

    @Test
    fun testGetPublicProviderURLPorcini() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI)
        assertEquals("https://porcini.rootnet.app/archive", providerInfo.url)
        assertEquals(7672, providerInfo.chainId)
    }

    @Test
    fun testGetAmountIn() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val res = provider.getAmountIn(BigInteger("100000"), ROOT_ID).sendAwait().unwrap()
        assertTrue(res.Ok.get(0).compareTo(0) > 0)

    }
}