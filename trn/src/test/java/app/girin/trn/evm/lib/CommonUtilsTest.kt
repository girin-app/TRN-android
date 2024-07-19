package app.girin.trn.evm.lib

import org.junit.Assert.assertEquals
import org.junit.Test

class CommonUtilsTest {
    @Test
    fun testAssetIdToERC20Address() {
        val address = assetIdToERC20Address(1)
        assertEquals("0xcCcCCccC00000001000000000000000000000000", address.toChecksumString())
    }

    @Test
    fun testCollectionIdToERC721Address() {
        val address = collectionIdToERC721Address(1)
        assertEquals("0xaAAaAaaa00000001000000000000000000000000", address.toChecksumString())
    }

    @Test
    fun testCollectionIdToERC1155Address() {
        val address = collectionIdToERC1155Address(1)
        assertEquals("0xBBBBBbbB00000001000000000000000000000000", address.toChecksumString())
    }
}