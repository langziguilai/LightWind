/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.lightwind.components

import android.content.Context
import com.app.browser_components.browser.session.SessionManager
import com.app.browser_components.feature.intent.IntentProcessor
import com.app.browser_components.feature.search.SearchUseCases
import com.app.browser_components.feature.session.SessionUseCases

/**
 * Component group for miscellaneous components.
 */
class Utilities(
    private val context: Context,
    private val sessionManager: SessionManager,
    private val sessionUseCases: SessionUseCases,
    private val searchUseCases: SearchUseCases
) {
    /**
     * Provides intent processing functionality for CustomTab, ACTION_VIEW
     * and ACTION_SEND intents.
     */
    val intentProcessor by lazy {
        IntentProcessor(sessionUseCases, sessionManager, searchUseCases, context)
    }
}
