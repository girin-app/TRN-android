package app.girin.trn

import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import org.junit.Assert.assertEquals
import org.junit.Test

class TRNTest {
    @Test
    fun testGetPublicProviderURLHttpRoot() {
        val providerInfo = getPublicProviderInfo(NetworkName.ROOT, false, false)
        assertEquals("https://root.rootnet.live/", providerInfo.url)
        assertEquals(7668, providerInfo.chainId)
        assertEquals("0x046e7cb5cdfee1b96e7bd59e051f80aeba61b030ce8c9275446e0209704fd338", providerInfo.genesisHash.toString())
    }

    @Test
    fun testGetPublicProviderURLWsRoot() {
        val providerInfo = getPublicProviderInfo(NetworkName.ROOT, true, false)
        assertEquals("wss://root.rootnet.live/", providerInfo.url)
        assertEquals(7668, providerInfo.chainId)
        assertEquals("0x046e7cb5cdfee1b96e7bd59e051f80aeba61b030ce8c9275446e0209704fd338", providerInfo.genesisHash.toString())
    }

    @Test
    fun testGetPublicProviderURLWsRootArchive() {
        val providerInfo = getPublicProviderInfo(NetworkName.ROOT, true, true)
        assertEquals("wss://root.rootnet.live/archive/", providerInfo.url)
        assertEquals(7668, providerInfo.chainId)
        assertEquals("0x046e7cb5cdfee1b96e7bd59e051f80aeba61b030ce8c9275446e0209704fd338", providerInfo.genesisHash.toString())
    }

    @Test
    fun testGetPublicProviderURLHttpRootArchive() {
        val providerInfo = getPublicProviderInfo(NetworkName.ROOT, false, true)
        assertEquals("https://root.rootnet.live/archive/", providerInfo.url)
        assertEquals(7668, providerInfo.chainId)
        assertEquals("0x046e7cb5cdfee1b96e7bd59e051f80aeba61b030ce8c9275446e0209704fd338", providerInfo.genesisHash.toString())
    }

    @Test
    fun testGetPublicProviderURLHttpPorciniArchive() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, true)
        assertEquals("https://porcini.rootnet.app/archive/", providerInfo.url)
        assertEquals(7672, providerInfo.chainId)
        assertEquals("0x83959f7f4262762f7599c2fa48b418b7e102f92c81fab9e6ef22ab379abdb72f", providerInfo.genesisHash.toString())
    }

    @Test
    fun testGetPublicProviderURLHttpPorcini() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, false)
        assertEquals("https://porcini.rootnet.app/", providerInfo.url)
        assertEquals(7672, providerInfo.chainId)
        assertEquals("0x83959f7f4262762f7599c2fa48b418b7e102f92c81fab9e6ef22ab379abdb72f", providerInfo.genesisHash.toString())
    }

    @Test
    fun testGetPublicProviderURLWsPorciniArchive() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, true, true)
        assertEquals("wss://porcini.rootnet.app/archive/", providerInfo.url)
        assertEquals(7672, providerInfo.chainId)
        assertEquals("0x83959f7f4262762f7599c2fa48b418b7e102f92c81fab9e6ef22ab379abdb72f", providerInfo.genesisHash.toString())
    }

    @Test
    fun testGetPublicProviderURLWsPorcini() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, true, false)
        assertEquals("wss://porcini.rootnet.app/", providerInfo.url)
        assertEquals(7672, providerInfo.chainId)
        assertEquals("0x83959f7f4262762f7599c2fa48b418b7e102f92c81fab9e6ef22ab379abdb72f", providerInfo.genesisHash.toString())
    }

}