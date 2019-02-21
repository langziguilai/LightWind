/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.feature.awesomebar.provider

import com.app.browser_components.concept.awesomebar.AwesomeBar
import com.app.browser_components.concept.storage.HistoryStorage
import com.app.browser_components.concept.storage.SearchResult
import com.app.browser_components.feature.session.SessionUseCases

private const val HISTORY_SUGGESTION_LIMIT = 20

/**
 * A [AwesomeBar.SuggestionProvider] implementation that provides suggestions based on the browsing
 * history stored in the [HistoryStorage].
 */
class HistoryStorageSuggestionProvider(
    private val historyStorage: HistoryStorage,
    private val loadUrlUseCase: SessionUseCases.LoadUrlUseCase
) : AwesomeBar.SuggestionProvider {

    override suspend fun onInputChanged(text: String): List<AwesomeBar.Suggestion> {
        if (text.isEmpty()) {
            return emptyList()
        }
        return historyStorage.getSuggestions(text, HISTORY_SUGGESTION_LIMIT).into()
    }

    private fun Iterable<SearchResult>.into(): List<AwesomeBar.Suggestion> {
        return this.map {
            AwesomeBar.Suggestion(
                id = it.id,
                title = it.title,
                description = it.url,
                score = it.score,
                onSuggestionClicked = { loadUrlUseCase.invoke(it.url) }
            )
        }
    }
}
