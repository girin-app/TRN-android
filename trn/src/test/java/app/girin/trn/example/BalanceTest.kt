package app.girin.trn.example

import app.girin.trn.NetworkName
import app.girin.trn.ROOT_ID
import app.girin.trn.evm.lib.ERC20_PRECOMPILE
import app.girin.trn.evm.lib.assetIdToERC20Address
import app.girin.trn.getPublicProviderInfo
import io.ethers.abi.AbiFunction
import io.ethers.core.types.Address
import io.ethers.core.types.BlockId
import io.ethers.core.types.CallRequest

import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import java.math.BigInteger

@Ignore("test")
class BalanceTest {
    @Test
    fun queryNativeBalance() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, true)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val address = Address("0x55D77A60Fd951117f531D2277a5BB4aFbE3fB292")
        val balance = provider.getBalance(address, BlockId.LATEST).sendAwait().unwrap()

        assertTrue(balance.compareTo(BigInteger.ZERO) > 0)
    }

    @Test
    fun queryERC20Balance() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, false)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val rootContract = assetIdToERC20Address(ROOT_ID)

        val address = Address("0x55D77A60Fd951117f531D2277a5BB4aFbE3fB292")

        val balanceFunction =
            AbiFunction.parseSignature(ERC20_PRECOMPILE.getAbi(ERC20_PRECOMPILE.Index.FUNCTION_BALANCE_OF))
        val params = arrayOf(
            address
        )

        val encoded = balanceFunction.encodeCall(params)

        val res = provider.call(CallRequest().apply {
            to = rootContract
            data = encoded
        }, BlockId.LATEST).sendAwait().unwrap()

        val balance = BigInteger(res.asByteArray())
        assertTrue(balance.compareTo(BigInteger.ZERO) > 0)
    }
}