/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.support.utils

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi

object StatusBarUtils {
    private var statusBarSize = -1

    /**
     * Determine the height of the status bar asynchronously.
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    fun getStatusBarHeight(view: View, block: (Int) -> Unit) {
        if (statusBarSize > 0) {
            block(statusBarSize)
        } else {
            view.setOnApplyWindowInsetsListener { _, insets ->
                statusBarSize = insets.systemWindowInsetTop
                block(statusBarSize)
                view.setOnApplyWindowInsetsListener(null)
                insets
            }
        }
    }
}
