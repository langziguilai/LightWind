package com.app.browser_components.feature.session.bundling.ext

import com.app.browser_components.browser.session.SessionManager
import com.app.browser_components.browser.session.storage.SnapshotSerializer
import com.app.browser_components.feature.session.bundling.db.BundleEntity
import com.app.browser_components.feature.session.bundling.db.UrlList

internal fun SessionManager.Snapshot.toBundleEntity() = BundleEntity(
    id = null,
    state = SnapshotSerializer().toJSON(this),
    savedAt = System.currentTimeMillis(),
    urls = UrlList(sessions.map { it.session.url })
)
