package com.yomismtz.expedientedeldentista.clinical

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClinicalRecordStoreInstrumentedTest {
    private lateinit var context: Context
    private lateinit var preferences: android.content.SharedPreferences
    private lateinit var store: ClinicalRecordStore

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        preferences = context.getSharedPreferences("clinical_records_v1", Context.MODE_PRIVATE)
        preferences.edit().clear().commit()
        store = ClinicalRecordStore(context)
    }

    @After
    fun tearDown() {
        preferences.edit().clear().commit()
    }

    @Test
    fun createRecordEncryptsItAndLoadsItBack() {
        val created = store.create(EducationalSession())

        val encrypted = preferences.getString("records_secure", null)
        assertNotNull("Encrypted payload must be stored", encrypted)
        assertFalse("Clinical records must not remain in legacy plaintext storage", preferences.contains("records"))
        assertEquals(created.id, store.loadAll().single().id)
    }

    @Test
    fun unreadableEncryptedPayloadIsNotReplacedWhenSaving() {
        val damagedPayload = "not-valid-ciphertext"
        preferences.edit().putString("records_secure", damagedPayload).commit()

        assertThrows(IllegalStateException::class.java) { store.loadAll() }
        assertThrows(IllegalStateException::class.java) { store.create(EducationalSession()) }

        assertEquals(
            "The original unreadable payload must remain available for recovery",
            damagedPayload,
            preferences.getString("records_secure", null)
        )
    }
}
