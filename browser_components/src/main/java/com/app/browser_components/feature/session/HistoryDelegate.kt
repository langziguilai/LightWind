/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.feature.session

import com.app.browser_components.concept.engine.history.HistoryTrackingDelegate
import com.app.browser_components.concept.storage.HistoryStorage
import com.app.browser_components.concept.storage.PageObservation
import com.app.browser_components.concept.storage.VisitType

/**
 * Implementation of the [HistoryTrackingDelegate] which delegates work to an instance of [HistoryStorage].
 */
class HistoryDelegate(private val historyStorage: HistoryStorage) : HistoryTrackingDelegate {
    override suspend fun onVisited(uri: String, isReload: Boolean) {
        val visitType = when (isReload) {
            true -> VisitType.RELOAD
            false -> VisitType.LINK
        }
        historyStorage.recordVisit(uri, visitType)
    }

    override suspend fun onTitleChanged(uri: String, title: String) {
        historyStorage.recordObservation(uri, PageObservation(title = title))
    }

    override suspend fun getVisited(uris: List<String>): List<Boolean> {
        return historyStorage.getVisited(uris)
    }

    override suspend fun getVisited(): List<String> {
        return historyStorage.getVisited()
    }
}
