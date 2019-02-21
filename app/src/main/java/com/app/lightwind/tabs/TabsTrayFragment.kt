/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.lightwind.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_tabstray.tabsPanel
import kotlinx.android.synthetic.main.fragment_tabstray.tabsTray
import com.app.browser_components.feature.tabs.tabstray.TabsFeature
import com.app.browser_components.support.base.feature.BackHandler
import com.app.lightwind.R
import com.app.lightwind.browser.BrowserFragment
import com.app.lightwind.ext.requireComponents

/**
 * A fragment for displaying the tabs tray.
 */
class TabsTrayFragment : Fragment(), BackHandler {
    private var tabsFeature: TabsFeature? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_tabstray, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabsFeature = TabsFeature(
            tabsTray,
            requireComponents.core.sessionManager,
            requireComponents.useCases.tabsUseCases,
            ::closeTabsTray)

        tabsPanel.initialize(tabsFeature) { closeTabsTray() }
    }

    override fun onStart() {
        super.onStart()

        tabsFeature?.start()
    }

    override fun onStop() {
        super.onStop()

        tabsFeature?.stop()
    }

    override fun onBackPressed(): Boolean {
        closeTabsTray()
        return true
    }

    private fun closeTabsTray() {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.container, BrowserFragment.create())
            commit()
        }
    }
}
