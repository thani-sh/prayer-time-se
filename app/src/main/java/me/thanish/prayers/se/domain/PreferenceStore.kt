package me.thanish.prayers.se.domain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


/**
 * Context.dataStore is the datastore instance that can be used across the app.
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


/**
 * setIntegerSync stores the integer value on device for the given key.
 */
fun setIntegerSync(context: Context, key: Preferences.Key<Int>, value: Int) {
    runBlocking {
        context.dataStore.edit { it[key] = value }
    }
}

/**
 * getIntegerSync returns the integer value stored on device for the given key.
 */
fun getIntegerSync(context: Context, key: Preferences.Key<Int>, default: Int): Int {
    return runBlocking {
        val flow = context.dataStore.data.map { it[key] ?: default }
        flow.firstOrNull() ?: default
    }
}
