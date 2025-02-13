package app.girin.trn.evm.lib.transfer

import app.girin.trn.NetworkName
import app.girin.trn.ROOT_ID
import app.girin.trn.evm.lib.ERC20_PRECOMPILE
import app.girin.trn.getPublicProviderInfo
import io.ethers.abi.AbiFunction
import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigInteger

class TransferTest {
    @Test
    fun testGetFeeProxyPricePair() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, false)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val res = getFeeProxyPricePair(
            provider, BigInteger("1000000"), ROOT_ID, 0.1
        )

        assertTrue(res.maxFeePerGas.compareTo(BigInteger.ZERO) > 0)
        assertTrue(res.maxPayment.compareTo(BigInteger.ZERO) > 0)
        assertTrue(res.estimateGasCost.compareTo(BigInteger.ZERO) > 0)
    }

}