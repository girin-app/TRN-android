package app.girin.trn.evm.lib.transfer

import app.girin.trn.evm.lib.getAmountIn
import io.ethers.core.types.BlockId
import io.ethers.providers.Provider
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.jvm.optionals.getOrNull

data class PricePairResult(
    val estimateGasCost: BigInteger,
    val maxPayment: BigInteger,
    val maxFeePerGas: BigInteger
)

fun getFeeProxyPricePair(
    provider: Provider,
    gasEstimate: BigInteger,
    feeAssetId: Int,
    slippage: Double = 0.0
): PricePairResult {
    val block = provider.getBlockWithHashes(BlockId.LATEST).sendAwait().unwrap().getOrNull()
    val maxFeePerGas = block?.baseFeePerGas ?: BigInteger.ZERO

    // Convert gasPrice in ETH to gasPrice in XRP, which has different decimals, one is 18 & one is 6
    val slippageBigDecimal = BigDecimal.valueOf(slippage + 1)
    val maxFeePerGasBigDecimal = maxFeePerGas.toBigDecimal()
    val gasCostInEth = gasEstimate.toBigDecimal().multiply(maxFeePerGasBigDecimal.multiply(slippageBigDecimal)).toBigInteger()

    val remainder = gasCostInEth.mod(BigInteger("1000000000000"))
    val gasCostInXRP = gasCostInEth.divide(BigInteger("1000000000000")) + if (remainder > BigInteger.ZERO) BigInteger.ONE else BigInteger.ZERO

    // Query the `dex` to determine the `maxPayment`
    val res = provider.getAmountIn(gasCostInXRP, feeAssetId).sendAwait().unwrap()

    return PricePairResult(
        estimateGasCost = gasCostInXRP,
        maxPayment = res.Ok.get(0).toBigInteger(),
        maxFeePerGas = maxFeePerGas
    )
}