/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.feature.tabs.toolbar

import com.app.browser_components.browser.session.SessionManager
import com.app.browser_components.browser.session.runWithSession
import com.app.browser_components.concept.toolbar.Toolbar

/**
 * Feature implementation for connecting a tabs tray implementation with a toolbar implementation.
 */

class TabsToolbarFeature(
    toolbar: Toolbar,
    sessionManager: SessionManager,
    sessionId: String? = null,
    showTabs: () -> Unit
) {
    init {
        run {
            sessionManager.runWithSession(sessionId) {
                it.isCustomTabSession()
            }.also { isCustomTab ->
                if (isCustomTab) return@run
            }
            val tabsAction = TabCounterToolbarButton(
                sessionManager,
                showTabs
            )
            toolbar.addBrowserAction(tabsAction)
        }
    }
}
