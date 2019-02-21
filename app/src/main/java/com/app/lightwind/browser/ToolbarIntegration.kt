/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.lightwind.browser

import android.content.Context
import com.app.browser_components.browser.domains.autocomplete.DomainAutocompleteProvider
import com.app.browser_components.browser.toolbar.BrowserToolbar
import com.app.browser_components.concept.storage.HistoryStorage
import com.app.browser_components.feature.toolbar.ToolbarAutocompleteFeature
import com.app.browser_components.feature.toolbar.ToolbarFeature
import com.app.browser_components.support.base.feature.BackHandler
import com.app.browser_components.support.base.feature.LifecycleAwareFeature
import com.app.lightwind.ext.components

class ToolbarIntegration(
    context: Context,
    toolbar: BrowserToolbar,
    historyStorage: HistoryStorage,
    domainAutocompleteProvider: DomainAutocompleteProvider,
    sessionId: String? = null
) : LifecycleAwareFeature, BackHandler {
    init {
        toolbar.setMenuBuilder(context.components.toolbar.menuBuilder)

        ToolbarAutocompleteFeature(toolbar).apply {
            addHistoryStorageProvider(historyStorage)
            addDomainProvider(domainAutocompleteProvider)
        }
    }

    private val toolbarFeature: ToolbarFeature = ToolbarFeature(
        toolbar,
        context.components.core.sessionManager,
        context.components.useCases.sessionUseCases.loadUrl,
        { searchTerms -> context.components.useCases.searchUseCases.defaultSearch.invoke(searchTerms) },
        sessionId
    )

    override fun start() {
        toolbarFeature.start()
    }

    override fun stop() {
        toolbarFeature.stop()
    }

    override fun onBackPressed(): Boolean {
        return toolbarFeature.onBackPressed()
    }
}
