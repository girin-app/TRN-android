package app.girin.trn.util

import app.girin.trn.api.lib.types.u32
import org.bouncycastle.crypto.digests.Blake2bDigest
import java.nio.ByteBuffer
import java.nio.ByteOrder

object Blake2b128Encoder {

    fun encodeU32(u32Value: u32): ByteArray {

        // 1. Prepare the data to hash (u32 as bytes)
        val dataBytes = ByteBuffer.allocate(4) // u32 = 4 bytes
            .order(ByteOrder.LITTLE_ENDIAN) // We use little-endian because it is commonly used in embedded systems
            .putInt(u32Value.toInt())
            .array()

        // 2. Initialize the BLAKE2b-128 digest
        // We set the key and the digest size.
        val digest = Blake2bDigest(null, 16, null, null)

        // 3. Update the digest with the data
        digest.update(dataBytes, 0, dataBytes.size)

        // 4. Finalize the digest and get the hash
        val hash = ByteArray(16) // 16 bytes for BLAKE2b-128
        digest.doFinal(hash, 0)

        return hash
    }
}