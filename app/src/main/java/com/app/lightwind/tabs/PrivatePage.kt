/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.app.lightwind.tabs

import android.content.Context
import androidx.annotation.RawRes
import com.app.lightwind.R

object PrivatePage {
    /**
     * Load and generate a private browsing page for the given url and html/css resources
     */
    fun createPrivateBrowsingPage(
        context: Context,
        url: String,
        @RawRes htmlRes: Int = R.raw.private_mode,
        @RawRes cssRes: Int = R.raw.private_style
    ): String {
        val css = context.resources.openRawResource(cssRes).bufferedReader().use {
            it.readText()
        }

        return context.resources.openRawResource(htmlRes)
            .bufferedReader()
            .use { it.readText() }
            .replace("%pageTitle%", context.getString(R.string.private_browsing_title))
            .replace("%pageBody%", context.getString(R.string.private_browsing_body))
            .replace("%privateBrowsingSupportUrl%", url)
            .replace("%css%", css)
    }
}
