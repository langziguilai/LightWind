/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.lightwind

import android.content.Context
import com.app.lightwind.components.Core
import com.app.lightwind.components.Analytics
import com.app.lightwind.components.BackgroundServices
import com.app.lightwind.components.Services
import com.app.lightwind.components.Search
import com.app.lightwind.components.Utilities
import com.app.lightwind.components.Toolbar
import com.app.lightwind.components.UseCases

/**
 * Provides access to all components.
 */
class Components(private val context: Context) {
    val core by lazy { Core(context) }
    val search by lazy { Search(context) }
    val useCases by lazy { UseCases(context, core.sessionManager, search.searchEngineManager) }

    // Background services are initiated eagerly; they kick off periodic tasks and setup an accounts system.
    val backgroundServices by lazy { BackgroundServices(context, core.historyStorage) }

    val toolbar by lazy { Toolbar(context, useCases.sessionUseCases, useCases.tabsUseCases, core.sessionManager) }
    val analytics by lazy { Analytics(context) }
    val utils by lazy {
        Utilities(context, core.sessionManager, useCases.sessionUseCases, useCases.searchUseCases)
    }
    val services by lazy { Services(backgroundServices.accountManager, useCases.tabsUseCases) }
}
