/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.feature.awesomebar

import android.content.Context
import android.view.View
import com.app.browser_components.browser.search.SearchEngine
import com.app.browser_components.browser.session.SessionManager
import com.app.browser_components.concept.awesomebar.AwesomeBar
import com.app.browser_components.concept.engine.EngineView
import com.app.browser_components.concept.storage.HistoryStorage
import com.app.browser_components.concept.toolbar.Toolbar
import com.app.browser_components.feature.awesomebar.provider.ClipboardSuggestionProvider
import com.app.browser_components.feature.awesomebar.provider.HistoryStorageSuggestionProvider
import com.app.browser_components.feature.awesomebar.provider.SearchSuggestionProvider
import com.app.browser_components.feature.awesomebar.provider.SessionSuggestionProvider
import com.app.browser_components.feature.search.SearchUseCases
import com.app.browser_components.feature.session.SessionUseCases
import com.app.browser_components.feature.tabs.TabsUseCases

/**
 * Connects an [AwesomeBar] with a [Toolbar] and allows adding multiple [AwesomeBar.SuggestionProvider] implementations.
 */
class AwesomeBarFeature(
    private val awesomeBar: AwesomeBar,
    private val toolbar: Toolbar,
    private val engineView: EngineView? = null,
    private val onEditStart: (() -> Unit)? = null,
    private val onEditComplete: (() -> Unit)? = null

) {
    init {
        toolbar.setOnEditListener(object : com.app.browser_components.concept.toolbar.Toolbar.OnEditListener {
            override fun onTextChanged(text: String) = awesomeBar.onInputChanged(text)

            override fun onStartEditing() {
                onEditStart?.invoke() ?: showAwesomeBar()
                awesomeBar.onInputStarted()
            }

            override fun onStopEditing() {
                onEditComplete?.invoke() ?: hideAwesomeBar()
                awesomeBar.onInputCancelled()
            }
        })

        awesomeBar.setOnStopListener { toolbar.displayMode() }
    }

    /**
     * Add a [AwesomeBar.SuggestionProvider] for "Open tabs" to the [AwesomeBar].
     */
    fun addSessionProvider(
        sessionManager: SessionManager,
        selectTabUseCase: TabsUseCases.SelectTabUseCase
    ): AwesomeBarFeature {
        val provider = SessionSuggestionProvider(sessionManager, selectTabUseCase)
        awesomeBar.addProviders(provider)
        return this
    }

    /**
     * Add a [AwesomeBar.SuggestionProvider] for search engine suggestions to the [AwesomeBar].
     */
    fun addSearchProvider(
        searchEngine: SearchEngine,
        searchUseCase: SearchUseCases.SearchUseCase,
        mode: SearchSuggestionProvider.Mode = SearchSuggestionProvider.Mode.SINGLE_SUGGESTION
    ): AwesomeBarFeature {
        awesomeBar.addProviders(SearchSuggestionProvider(searchEngine, searchUseCase, mode))
        return this
    }

    /**
     * Add a [AwesomeBar.SuggestionProvider] for browsing history to the [AwesomeBar].
     */
    fun addHistoryProvider(
        historyStorage: HistoryStorage,
        loadUrlUseCase: SessionUseCases.LoadUrlUseCase
    ): AwesomeBarFeature {
        awesomeBar.addProviders(HistoryStorageSuggestionProvider(historyStorage, loadUrlUseCase))
        return this
    }

    fun addClipboardProvider(
        context: Context,
        loadUrlUseCase: SessionUseCases.LoadUrlUseCase
    ): AwesomeBarFeature {
        awesomeBar.addProviders(ClipboardSuggestionProvider(context, loadUrlUseCase))
        return this
    }

    private fun showAwesomeBar() {
        awesomeBar.asView().visibility = View.VISIBLE
        engineView?.asView()?.visibility = View.GONE
    }

    private fun hideAwesomeBar() {
        awesomeBar.asView().visibility = View.GONE
        engineView?.asView()?.visibility = View.VISIBLE
    }
}
