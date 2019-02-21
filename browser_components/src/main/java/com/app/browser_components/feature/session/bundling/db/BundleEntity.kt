/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.feature.session.bundling.db


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.browser_components.browser.session.SessionManager
import com.app.browser_components.browser.session.storage.SnapshotSerializer
import com.app.browser_components.feature.session.bundling.SessionBundle

/**
 * Internal entity representing a session bundle as it gets saved to the database. This class implements [SessionBundle]
 * which only exposes the part of the API we want to make public.
 */
@Entity(tableName = "bundles")
internal data class BundleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long?,

    @ColumnInfo(name = "state")
    var state: String,

    @ColumnInfo(name = "saved_at")
    var savedAt: Long,

    @ColumnInfo(name = "urls")
    var urls: UrlList
) {
    /**
     * Updates this entity with the value from the given snapshot.
     */
    fun updateFrom(snapshot: SessionManager.Snapshot): BundleEntity {
        state = SnapshotSerializer().toJSON(snapshot)
        savedAt = System.currentTimeMillis()
        urls = UrlList(snapshot.sessions.map { it.session.url })
        return this
    }
}

internal data class UrlList(
    val entries: List<String>
)
