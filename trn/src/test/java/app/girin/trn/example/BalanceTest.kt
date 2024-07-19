package app.girin.trn.example

import app.girin.trn.evm.lib.ERC20_PRECOMPILE
import app.girin.trn.evm.lib.NetworkName
import app.girin.trn.evm.lib.ROOT_ID
import app.girin.trn.evm.lib.assetIdToERC20Address
import app.girin.trn.evm.lib.getPublicProviderInfo
import io.ethers.abi.AbiFunction
import io.ethers.abi.ContractStruct
import io.ethers.core.types.Address
import io.ethers.core.types.BlockId
import io.ethers.core.types.CallRequest

import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import io.ethers.providers.types.RpcRequest
import okhttp3.internal.wait
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigInteger

class BalanceTest {
    @Test
    fun queryNativeBalance() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val address = Address("0x55D77A60Fd951117f531D2277a5BB4aFbE3fB292")
        val balance = provider.getBalance(address, BlockId.LATEST).sendAwait().unwrap()

        assertTrue(balance.compareTo(BigInteger.ZERO) > 0)
    }

    @Test
    fun queryERC20Balance() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val rootContract = assetIdToERC20Address(ROOT_ID)

        val address = Address("0x55D77A60Fd951117f531D2277a5BB4aFbE3fB292")

        val balanceFunction = AbiFunction.parseSignature(ERC20_PRECOMPILE.getAbi(ERC20_PRECOMPILE.Index.FUNCTION_BALANCE_OF))
        val params = arrayOf(
            address
        )

        val encoded = balanceFunction.encodeCall(params)

        val res = provider.call( CallRequest().apply {
            to = rootContract
            data = encoded
        }, BlockId.LATEST).sendAwait().unwrap()

        val balance = BigInteger(res.asByteArray())
        assertTrue(balance.compareTo(BigInteger.ZERO) > 0)
    }
}