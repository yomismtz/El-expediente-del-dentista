package com.yomismtz.expedientedeldentista.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RecordFieldStoreSecurityTest {

    @Test
    fun acceptsEncryptedPayloadOnlyWhenRoundTripMatchesOriginalBytes() {
        val original = "clinical-field-payload".toByteArray(Charsets.UTF_8)

        val verified = verifiedEncryptedFieldValue(
            plaintext = original,
            encrypted = "test-envelope",
            decrypt = { original.copyOf() }
        )

        assertEquals("test-envelope", verified)
    }

    @Test
    fun rejectsEncryptedPayloadWhenRecoveredBytesDiffer() {
        val original = "clinical-field-payload".toByteArray(Charsets.UTF_8)

        val verified = verifiedEncryptedFieldValue(
            plaintext = original,
            encrypted = "test-envelope",
            decrypt = { "different-payload".toByteArray(Charsets.UTF_8) }
        )

        assertNull(verified)
    }

    @Test
    fun rejectsMissingOrUndecryptableEncryptedPayload() {
        val original = "clinical-field-payload".toByteArray(Charsets.UTF_8)

        assertNull(verifiedEncryptedFieldValue(original, null) { null })
        assertNull(verifiedEncryptedFieldValue(original, "broken-envelope") { null })
    }
}
