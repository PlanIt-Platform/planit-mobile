package com.example.myapplication.sessionStorage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

/**
 * DataStore implementation for storing and retrieving session-related data, such as access tokens,
 * refresh tokens, and user IDs.
 *
 * @property store The DataStore instance for storing session data.
 */
class SessionDataStore(private val store: DataStore<Preferences>) {

    private val accessToken = stringPreferencesKey("access_token")
    private val refreshToken = stringPreferencesKey("refresh_token")
    private val userID = intPreferencesKey("user_id")

    /**
     * Retrieves the stored access token from the session data.
     *
     * @return The stored access token, or null if not present.
     */
    suspend fun getSessionAccessToken(): String? {
        val token = store.data.first()
        val accessToken = token[accessToken]
        return if (accessToken.isNullOrEmpty()) null else accessToken
    }

    /**
     * Retrieves the stored refresh token from the session data.
     *
     * @return The stored refresh token, or null if not present.
     */
    suspend fun getSessionRefreshToken(): String? {
        val token = store.data.first()
        val refreshToken = token[refreshToken]
        return if (refreshToken.isNullOrEmpty()) null else refreshToken
    }

    /**
     * Retrieves the stored user ID from the session data.
     *
     * @return The stored user ID, or null if not present.
     */
    suspend fun getUserID(): Int? {
        val id = store.data.first()
        return id[this.userID]
    }

    /**
     * Checks if a user is logged in by verifying the presence of an access token.
     *
     * @return True if a user is logged in; false otherwise.
     */
    suspend fun isLogged(): Boolean {
        val token = store.data.first()
        val accessToken = token[accessToken]
        return !accessToken.isNullOrEmpty()
    }

    /**
     * Clears the session data, effectively logging out the user.
     */
    suspend fun clearSession() {
        store.edit {
            it.clear()
        }
    }

    /**
     * Sets the session data with the provided access token, refresh token, and user ID.
     *
     * @param accessToken The access token to be stored.
     * @param refreshToken The refresh token to be stored.
     * @param userID The user ID to be stored.
     */
    suspend fun setSession(accessToken: String, refreshToken: String, userID: Int) {
        store.edit {
            it[this.accessToken] = accessToken
            it[this.refreshToken] = refreshToken
            it[this.userID] = userID
        }
    }
}
