package com.example.clinexusapp.util

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.clinexusapp.model.PatientInfo
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Secure Session Manager (Native Android equivalent to Flutter Secure Storage)
 * Handles hardware-backed encryption for JWT tokens and sensitive patient data.
 */
object SessionManager {
    private const val TAG = "SessionManager"
    private const val PREF_NAME = "secure_session_prefs"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_PATIENT_INFO = "patient_info"

    private val _currentUser = MutableStateFlow<PatientInfo?>(null)
    val currentUser = _currentUser.asStateFlow()

    private var _token: String? = null
    val token: String? get() = _token

    private var sharedPreferences: android.content.SharedPreferences? = null

    fun init(context: Context) {
        try {
            sharedPreferences = createSharedPreferences(context)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize EncryptedSharedPreferences, attempting recovery", e)
            try {
                // Recovery path: clear the preferences file and try again
                // This usually fixes issues where the Keystore is corrupted or the master key is inaccessible
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply()
                sharedPreferences = createSharedPreferences(context)
            } catch (recoveryException: Exception) {
                Log.e(TAG, "Recovery failed, session management will be unavailable", recoveryException)
            }
        }

        sharedPreferences?.let { prefs ->
            _token = prefs.getString(KEY_TOKEN, null)
            val patientJson = prefs.getString(KEY_PATIENT_INFO, null)
            if (patientJson != null) {
                try {
                    _currentUser.value = Gson().fromJson(patientJson, PatientInfo::class.java)
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing patient info from prefs", e)
                }
            }
        }
    }

    private fun createSharedPreferences(context: Context): android.content.SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveSession(token: String, patient: PatientInfo) {
        _token = token
        _currentUser.value = patient

        sharedPreferences?.edit()?.apply {
            putString(KEY_TOKEN, token)
            putString(KEY_PATIENT_INFO, Gson().toJson(patient))
            apply()
            Log.d(TAG, "Session saved successfully for patient ID: ${patient.patientID}")
        } ?: Log.w(TAG, "saveSession called but sharedPreferences is null")
    }

    fun logout() {
        _token = null
        _currentUser.value = null
        sharedPreferences?.edit()?.clear()?.apply()
            ?: Log.w(TAG, "logout called but sharedPreferences is null")
    }

    val isLoggedIn: Boolean get() = _token != null
}
