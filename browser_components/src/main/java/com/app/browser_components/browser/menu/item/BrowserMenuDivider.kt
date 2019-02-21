/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.browser.menu.item

import android.view.View
import com.app.browser_components.R
import com.app.browser_components.browser.menu.BrowserMenu
import com.app.browser_components.browser.menu.BrowserMenuItem

/**
 * A browser menu item to display a horizontal divider.
 */
class BrowserMenuDivider : BrowserMenuItem {
    override var visible: () -> Boolean = { true }

    override fun getLayoutResource() = R.layout.mozac_browser_menu_item_divider

    override fun bind(menu: BrowserMenu, view: View) = Unit
}
