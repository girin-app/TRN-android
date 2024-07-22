package app.girin.trn.evm.lib.dex

import app.girin.trn.NetworkName
import app.girin.trn.ROOT_ID
import app.girin.trn.getPublicProviderInfo
import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigInteger

class DexTest{
    @Test
    fun testGetAmountIn() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, false)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val res = provider.getAmountIn(BigInteger("100000"), ROOT_ID).sendAwait().unwrap()
        assertTrue(res.Ok.get(0).compareTo(0) > 0)
    }
}