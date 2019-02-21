/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.lightwind.components

import android.content.Context
import android.content.Intent
import com.app.browser_components.browser.domains.autocomplete.ShippedDomainsProvider
import com.app.browser_components.browser.menu.BrowserMenuBuilder
import com.app.browser_components.browser.menu.item.BrowserMenuItemToolbar
import com.app.browser_components.browser.menu.item.BrowserMenuSwitch
import com.app.browser_components.browser.menu.item.SimpleBrowserMenuItem
import com.app.browser_components.browser.session.SessionManager
import com.app.browser_components.feature.session.SessionUseCases
import com.app.browser_components.feature.tabs.TabsUseCases
import com.app.lightwind.R
import com.app.lightwind.ext.share
import com.app.lightwind.settings.SettingsActivity
import com.app.lightwind.browser.FindInPageIntegration

/**
 * Component group for all functionality related to the browser toolbar.
 */
class Toolbar(
    private val context: Context,
    private val sessionUseCases: SessionUseCases,
    private val tabsUseCases: TabsUseCases,
    private val sessionManager: SessionManager
) {

    /**
     * Helper class for building browser menus.
     */
    val menuBuilder by lazy { BrowserMenuBuilder(menuItems) }

    /**
     * Provides autocomplete functionality for shipped / provided domain lists.
     */
    val shippedDomainsProvider by lazy {
        ShippedDomainsProvider().also { it.initialize(context) }
    }

    private val menuToolbar by lazy {
        val forward = BrowserMenuItemToolbar.Button(
                com.app.browser_components.ui.icons.R.drawable.mozac_ic_forward,
                iconTintColorResource = R.color.icons,
                contentDescription = "Forward") {
            sessionUseCases.goForward.invoke()
        }

        val refresh = BrowserMenuItemToolbar.Button(
                com.app.browser_components.ui.icons.R.drawable.mozac_ic_refresh,
                iconTintColorResource = R.color.icons,
                contentDescription = "Refresh") {
            sessionUseCases.reload.invoke()
        }

        val stop = BrowserMenuItemToolbar.Button(
                com.app.browser_components.ui.icons.R.drawable.mozac_ic_stop,
                iconTintColorResource = R.color.icons,
                contentDescription = "Stop") {
            sessionUseCases.stopLoading.invoke()
        }

        BrowserMenuItemToolbar(listOf(forward, refresh, stop))
    }

    private val menuItems by lazy {
        listOf(
            menuToolbar,
            SimpleBrowserMenuItem("Share") {
                val url = sessionManager.selectedSession?.url ?: ""
                context.share(url)
            }.apply {
                visible = { sessionManager.selectedSession != null }
            },

            BrowserMenuSwitch("Request desktop site", {
                sessionManager.selectedSessionOrThrow.desktopMode
            }) { checked ->
                sessionUseCases.requestDesktopSite.invoke(checked)
            }.apply {
                visible = { sessionManager.selectedSession != null }
            },

            SimpleBrowserMenuItem("Find in Page") {
                FindInPageIntegration.launch?.invoke()
            }.apply {
                visible = { sessionManager.selectedSession != null }
            },

            SimpleBrowserMenuItem("Report issue") {
                tabsUseCases.addTab.invoke(
                    "https://github.com/mozilla-mobile/reference-browser/issues/new")
            },

            SimpleBrowserMenuItem("Settings") {
                openSettingsActivity()
            }
        )
    }

    private fun openSettingsActivity() {
        val intent = Intent(context, SettingsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
