/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.feature.sync

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.app.browser_components.concept.storage.SyncError
import com.app.browser_components.concept.storage.SyncableStore
import com.app.browser_components.service.fxa.FirefoxAccountShaped
import com.app.browser_components.support.base.log.logger.Logger
import com.app.browser_components.support.base.observer.Observable
import com.app.browser_components.support.base.observer.ObserverRegistry
import java.lang.Exception

/**
 * An interface for consumers that wish to observer "sync lifecycle" events.
 */
interface SyncStatusObserver {
    /**
     * Gets called at the start of a sync, before any configured syncable is synchronized.
     */
    fun onStarted()

    /**
     * Gets called at the end of a sync, after every configured syncable has been synchronized.
     */
    fun onIdle()

    /**
     * Gets called if sync encounters an error that's worthy of processing by status observers.
     * @param error Optional relevant exception.
     */
    fun onError(error: Exception?)
}

val registry = ObserverRegistry<SyncStatusObserver>()

/**
 * A feature implementation which orchestrates data synchronization of a set of [SyncableStore] which
 * all share a common [AuthType].
 *
 * [AuthType] provides us with a layer of indirection that allows consumers of [FirefoxSyncFeature]
 * to use entirely different types of [SyncableStore], without this feature needing to depend on
 * their specific implementations. Those implementations might have heavy native dependencies
 * (e.g. places and logins depend on native libraries), and we do not want to force a consumer which
 * only cares about syncing logins to have to import a places native library.
 *
 * @param reifyAuth A conversion method which reifies a generic [FxaAuthInfo] into an object of
 * type [AuthType].
 */
class FirefoxSyncFeature<AuthType>(
    private val syncableStores: Map<String, SyncableStore<AuthType>>,
    private val reifyAuth: suspend (authInfo: FxaAuthInfo) -> AuthType
) : Observable<SyncStatusObserver> by registry {
    private val logger = Logger("feature-sync")

    /**
     * Sync operation exposed by this feature is guarded by a mutex, ensuring that only one Sync
     * may be running at any given time.
     */
    private var syncMutex = Mutex()

    /**
     * @return A [Boolean] indicating if any sync operations are currently running.
     */
    fun syncRunning(): Boolean {
        return syncMutex.isLocked
    }

    /**
     * Performs a sync of configured [SyncableStore] history instance. This method guarantees that
     * only one sync may be running at any given time.
     *
     * @param account [FirefoxAccountShaped] for which to perform a sync.
     * @return a [SyncResult] indicating result of synchronization of configured stores.
     */
    suspend fun sync(account: FirefoxAccountShaped): SyncResult = syncMutex.withLock { withListeners {
        if (syncableStores.isEmpty()) {
            return@withListeners mapOf()
        }

        val results = mutableMapOf<String, StoreSyncStatus>()

        val reifiedAuthInfo = try {
            reifyAuth(account.authInfo())
        } catch (e: AuthException) {
            syncableStores.keys.forEach { storeName ->
                results[storeName] = StoreSyncStatus(SyncError(e))
            }
            return@withListeners results
        }

        syncableStores.keys.forEach { storeName ->
            results[storeName] = syncStore(syncableStores[storeName]!!, storeName, reifiedAuthInfo)
        }

        return@withListeners results
    } }

    private suspend fun syncStore(
        store: SyncableStore<AuthType>,
        storeName: String,
        account: AuthType
    ): StoreSyncStatus {
        return StoreSyncStatus(store.sync(account).also {
            if (it is SyncError) {
                logger.error("Error synchronizing a $storeName store", it.exception)
            } else {
                logger.info("Synchronized $storeName store.")
            }
        })
    }

    private suspend fun withListeners(block: suspend () -> SyncResult): SyncResult {
        notifyObservers { onStarted() }
        val result = block()
        notifyObservers { onIdle() }
        return result
    }
}
