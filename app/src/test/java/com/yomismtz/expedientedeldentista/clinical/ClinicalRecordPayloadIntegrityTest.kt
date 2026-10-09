package com.yomismtz.expedientedeldentista.clinical

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ClinicalRecordPayloadIntegrityTest {
    @Test
    fun missingEncryptedPayloadUsesLegacyReader() {
        var legacyReads = 0
        val result = resolveStoredClinicalPayload(null, { error("decrypt must not run") }) {
            legacyReads++
            "legacy-data"
        }

        assertFalse(result.encrypted)
        assertEquals("legacy-data", result.raw)
        assertEquals(1, legacyReads)
    }

    @Test
    fun emptyEncryptedPayloadFailsWithoutFallingBackToLegacy() {
        var legacyReads = 0
        val error = runCatching {
            resolveStoredClinicalPayload("", { error("decrypt must not run") }) {
                legacyReads++
                "legacy-data"
            }
        }.exceptionOrNull()

        assertTrue(error is IllegalStateException)
        assertEquals(0, legacyReads)
    }

    @Test
    fun decryptionFailureFailsWithoutFallingBackToLegacy() {
        var legacyReads = 0
        val error = runCatching {
            resolveStoredClinicalPayload("ciphertext", { null }) {
                legacyReads++
                "legacy-data"
            }
        }.exceptionOrNull()

        assertTrue(error is IllegalStateException)
        assertEquals(0, legacyReads)
    }


    @Test
    fun whitespaceEncryptedPayloadFailsWithoutFallingBackToLegacy() {
        var legacyReads = 0
        val error = runCatching {
            resolveStoredClinicalPayload("   ", { error("decrypt must not run") }) {
                legacyReads++
                "legacy-data"
            }
        }.exceptionOrNull()

        assertTrue(error is IllegalStateException)
        assertEquals(0, legacyReads)
    }

    @Test
    fun thrownDecryptionErrorFailsWithoutFallingBackToLegacy() {
        var legacyReads = 0
        val error = runCatching {
            resolveStoredClinicalPayload("ciphertext", { throw SecurityException("invalid tag") }) {
                legacyReads++
                "legacy-data"
            }
        }.exceptionOrNull()

        assertTrue(error is SecurityException)
        assertEquals(0, legacyReads)
    }

    @Test
    fun validEncryptedPayloadIsReturnedAsEncrypted() {
        val result = resolveStoredClinicalPayload("ciphertext", { "decrypted-json" }) {
            error("legacy must not run")
        }

        assertTrue(result.encrypted)
        assertEquals("decrypted-json", result.raw)
    }
}
