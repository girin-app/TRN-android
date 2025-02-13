package app.girin.trn.api.lib.assets

import app.girin.trn.NetworkName
import app.girin.trn.api.lib.rpc.stateGetStorage
import app.girin.trn.getPublicProviderInfo
import io.ethers.core.FastHex
import io.ethers.core.types.Address
import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigInteger

class TransferTest {


    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun createTransferMethodTest() {
        val assetId = 2U
        val target = Address("784c245295885e8bf48711d431a880a09246da35")
        val amount = BigInteger("1000000")

        val call = Transfer.create(assetId, target, amount)


        assertEquals(
            "060802000000784c245295885e8bf48711d431a880a09246da3502093d00",
            call.toU8a().toHexString()
        )
    }

    @Test
    fun decodeMetadataTest() {
        /**
         * 	PalletAssetsAssetMetadata: {
         * 		deposit: "u128",
         * 		name: "Bytes",
         * 		symbol: "Bytes",
         * 		decimals: "u8",
         * 		isFrozen: "bool",
         * 	},
         */
        val storageData = FastHex.decode("0000000000000000000000000000000010526f6f7410524f4f540600")

        AssetMetadata.decode(storageData).let {
            assertEquals(BigInteger.ZERO, it.deposit)
            assertEquals("Root", it.name)
            assertEquals("ROOT", it.symbol)
            assertEquals(6, it.decimals)
            assertEquals(false, it.isFrozen)
        }

    }

    @Test
    fun metadataTest() {
        val providerInfo = getPublicProviderInfo(NetworkName.PORCINI, false, false)
        val provider = Provider(HttpClient(providerInfo.url), providerInfo.chainId)

        val assetId = 1U

        val query = Metadata.create(assetId)

        val metaData = provider.stateGetStorage(query.getStorageKey()).sendAwait().unwrap()
            .let { AssetMetadata.decode(it) }

        metaData.let {
            assertEquals(BigInteger.ZERO, it.deposit)
            assertEquals("Root", it.name)
            assertEquals("ROOT", it.symbol)
            assertEquals(6, it.decimals)
            assertEquals(false, it.isFrozen)
        }
    }
}