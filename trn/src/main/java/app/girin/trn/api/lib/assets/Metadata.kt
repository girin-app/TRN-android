package app.girin.trn.api.lib.assets

import app.girin.trn.api.lib.types.Query
import app.girin.trn.api.lib.types.u32
import app.girin.trn.util.Blake2b128Encoder
import app.girin.trn.util.u32ToU8a
import io.ethers.core.FastHex

class Metadata(
    override val moduleAndMethod: ByteArray = FastHex.decode("682a59d51ab9e48a8c8cc418ff9708d2b5f3822e35ca2f31ce3526eab1363fd2"),
    override val args: QueryAssetsMetadataArgs
) : Query {

    override fun encode(): ByteArray {
        return moduleAndMethod + args.encode()
    }

    companion object {
        fun create(assetId: u32): Metadata {
            return Metadata(
                args = QueryAssetsMetadataArgs(assetId)
            )
        }
    }

    class QueryAssetsMetadataArgs(
        private val assetId: u32
    ) {
        fun encode(): ByteArray {
            return Blake2b128Encoder.encodeU32(assetId) + u32ToU8a(assetId)
        }
    }
}