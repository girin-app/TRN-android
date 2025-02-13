package app.girin.trn.api.lib.assets

import app.girin.trn.NetworkName
import app.girin.trn.api.lib.rpc.stateGetStorage
import app.girin.trn.getPublicProviderInfo
import io.ethers.core.FastHex
import io.ethers.providers.HttpClient
import io.ethers.providers.Provider
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigInteger

class MetadataTest {

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun createMetadataQueryTest() {
        val assetId = 1U
        val query = Metadata.create(assetId).encode()

        assertEquals(
            "682a59d51ab9e48a8c8cc418ff9708d2b5f3822e35ca2f31ce3526eab1363fd2d82c12285b5d4551f88e8f6e7eb52b8101000000",
            query.toHexString()
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