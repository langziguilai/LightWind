/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.browser.storage.sync

import android.content.Context
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.async
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.app.browser_components.concept.storage.HistoryAutocompleteResult
import com.app.browser_components.concept.storage.HistoryStorage
import com.app.browser_components.concept.storage.PageObservation
import com.app.browser_components.concept.storage.SearchResult
import com.app.browser_components.concept.storage.SyncOk
import com.app.browser_components.concept.storage.SyncStatus
import com.app.browser_components.concept.storage.SyncableStore
import com.app.browser_components.concept.storage.VisitType
import com.app.browser_components.support.utils.segmentAwareDomainMatch

const val AUTOCOMPLETE_SOURCE_NAME = "placesHistory"

//TODO:
data class SyncAuthInfo(val token:String)

/**
 *
 */
open class PlacesHistoryStorage(context: Context) : HistoryStorage, SyncableStore<SyncAuthInfo> {
    private val scope by lazy { CoroutineScope(Dispatchers.IO) }
    private val storageDir by lazy { context.filesDir }


    override suspend fun recordVisit(uri: String, visitType: VisitType) {
        scope.launch {

        }.join()
    }

    override suspend fun recordObservation(uri: String, observation: PageObservation) {
        // NB: visitType of null means "record meta information about this URL".
        scope.launch {

        }.join()
    }

    override suspend fun getVisited(uris: List<String>): List<Boolean> {
        return scope.async { listOf<Boolean>() }.await()
    }

    override suspend fun getVisited(): List<String> {
        return scope.async {
            listOf<String>()
        }.await()
    }

    override fun cleanup() {
        scope.coroutineContext.cancelChildren()

    }

    override fun getSuggestions(query: String, limit: Int): List<SearchResult> {
        require(limit >= 0) { "Limit must be a positive integer" }
        return listOf()
    }

    override fun getAutocompleteSuggestion(query: String): HistoryAutocompleteResult? {
        val url = ""

        val resultText = segmentAwareDomainMatch(query, arrayListOf(url))
        return resultText?.let {
            HistoryAutocompleteResult(
                input = query,
                text = it.matchedSegment,
                url = it.url,
                source = AUTOCOMPLETE_SOURCE_NAME,
                totalItems = 1
            )
        }
    }

    override suspend fun sync(authInfo: SyncAuthInfo): SyncStatus {
        return SyncOk
    }

}
