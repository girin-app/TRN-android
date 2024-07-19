package app.girin.trn.evm.lib

import io.ethers.core.types.Address

val ROOT_ID = 1
val XRP_ID = 2

fun assetIdToERC20Address(assetId: Int): Address {
    val assetIdInHex = assetId.toString(16).padStart(8, '0').uppercase()
    return Address("0xCCCCCCCC${assetIdInHex}000000000000000000000000")
}

fun collectionIdToERC721Address(collectionId: Int): Address {
    val collectionIdInHex = collectionId.toString(16).padStart(8, '0').uppercase()
    return Address("0xAAAAAAAA${collectionIdInHex}000000000000000000000000")
}

fun collectionIdToERC1155Address(collectionId: Int): Address {
    val collectionIdInHex = collectionId.toString(16).padStart(8, '0').uppercase()
    return Address("0xBBBBBBBB${collectionIdInHex}000000000000000000000000")
}