package app.girin.trn.api.lib.chain

import app.girin.trn.NetworkName
import app.girin.trn.getPublicProviderInfo
import io.ethers.core.types.Hash
import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigInteger

class ChainTest {
    @Test
    fun testGetBlock() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, true)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val hash = Hash("0x8285aac7457e68c44e893402efc582c862eb37667a54529778c683ed70b6dc1a")
        val res = provider.getBlock(hash).sendAwait().unwrap()
        assertEquals(res.block.header.number, BigInteger("14087392"))
    }

    @Test
    fun testFinalizedHead() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, true)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val hash = provider.getFinalizedHead().sendAwait().unwrap()
        assertEquals(hash.rlpSize(), 33)
    }
}