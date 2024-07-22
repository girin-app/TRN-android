package app.girin.trn.api.lib.account

import app.girin.trn.NetworkName
import app.girin.trn.getPublicProviderInfo
import io.ethers.abi.AbiType
import io.ethers.core.types.Address
import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AccountTest {
    @Test
    fun testNextIndex() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, true)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val address = Address("0x55D77A60Fd951117f531D2277a5BB4aFbE3fB292")
        val nonce = provider.nextIndex(address).sendAwait().unwrap()
        assertTrue(nonce > 0)
    }
}