/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.lightwind.components

import android.content.Context
import com.app.browser_components.browser.search.SearchEngineManager
import com.app.browser_components.browser.session.SessionManager
import com.app.browser_components.feature.search.SearchUseCases
import com.app.browser_components.feature.session.SessionUseCases
import com.app.browser_components.feature.tabs.TabsUseCases

/**
 * Component group for all use cases. Use cases are provided by feature
 * modules and can be triggered by UI interactions.
 */
class UseCases(
    private val context: Context,
    private val sessionManager: SessionManager,
    private val searchEngineManager: SearchEngineManager
) {
    /**
     * Use cases that provide engine interactions for a given browser session.
     */
    val sessionUseCases by lazy { SessionUseCases(sessionManager) }

    /**
     * Use cases that provide tab management.
     */
    val tabsUseCases: TabsUseCases by lazy { TabsUseCases(sessionManager) }

    /**
     * Use cases that provide search engine integration.
     */
    val searchUseCases by lazy { SearchUseCases(context, searchEngineManager, sessionManager) }
}
