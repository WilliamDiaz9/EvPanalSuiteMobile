package com.duoc.evpanalsuitemobile.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Que se guarda en el telefono: el usuario con el que se entro
interface SessionStorage {
    // Id del ultimo usuario elegido (null si nunca se eligio)
    val userId: Flow<Int?>

    suspend fun saveUserId(id: Int)
}

// Archivo de preferencias de la app (DataStore)
private val Context.dataStore by preferencesDataStore(name = "panalsuite_prefs")

class DataStoreSessionStorage(private val context: Context) : SessionStorage {

    private val userIdKey = intPreferencesKey("user_id")

    override val userId: Flow<Int?> =
        context.dataStore.data.map { prefs -> prefs[userIdKey] }

    override suspend fun saveUserId(id: Int) {
        context.dataStore.edit { prefs -> prefs[userIdKey] = id }
    }
}
