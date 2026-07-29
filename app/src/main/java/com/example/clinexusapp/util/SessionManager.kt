package com.example.clinexusapp.util

import android.content.Context
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
    private const val PREF_NAME = "secure_session_prefs"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_PATIENT_INFO = "patient_info"

    private val _currentUser = MutableStateFlow<PatientInfo?>(null)
    val currentUser = _currentUser.asStateFlow()

    private var _token: String? = null
    val token: String? get() = _token

    private lateinit var sharedPreferences: android.content.SharedPreferences

    fun init(context: Context) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        _token = sharedPreferences.getString(KEY_TOKEN, null)
        val patientJson = sharedPreferences.getString(KEY_PATIENT_INFO, null)
        if (patientJson != null) {
            try {
                _currentUser.value = Gson().fromJson(patientJson, PatientInfo::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveSession(token: String, patient: PatientInfo) {
        _token = token
        _currentUser.value = patient

        sharedPreferences.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_PATIENT_INFO, Gson().toJson(patient))
            .apply()
    }

    fun logout() {
        _token = null
        _currentUser.value = null
        sharedPreferences.edit().clear().apply()
    }

    val isLoggedIn: Boolean get() = _token != null
}
