/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.feature.tabs.tabstray


import androidx.annotation.VisibleForTesting
import com.app.browser_components.browser.session.Session
import com.app.browser_components.browser.session.SessionManager
import com.app.browser_components.concept.tabstray.TabsTray
import com.app.browser_components.feature.tabs.TabsUseCases
import com.app.browser_components.support.base.feature.LifecycleAwareFeature

/**
 * Feature implementation for connecting a tabs tray implementation with the session module.
 */
class TabsFeature(
    tabsTray: TabsTray,
    sessionManager: SessionManager,
    tabsUseCases: TabsUseCases,
    closeTabsTray: () -> Unit
) : LifecycleAwareFeature {
    @VisibleForTesting
    internal var presenter = TabsTrayPresenter(
        tabsTray,
        sessionManager,
        closeTabsTray
    )

    @VisibleForTesting
    internal var interactor = TabsTrayInteractor(
        tabsTray,
        tabsUseCases.selectTab,
        tabsUseCases.removeTab,
        closeTabsTray)

    override fun start() {
        presenter.start()
        interactor.start()
    }

    override fun stop() {
        presenter.stop()
        interactor.stop()
    }

    fun filterTabs(tabsFilter: (Session) -> Boolean) {
        presenter.sessionsFilter = tabsFilter
        presenter.calculateDiffAndUpdateTabsTray()
    }
}
