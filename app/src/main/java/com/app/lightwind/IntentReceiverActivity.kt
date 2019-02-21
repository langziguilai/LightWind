/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.lightwind

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.app.browser_components.browser.session.tab.CustomTabConfig
import com.app.browser_components.support.utils.SafeIntent
import com.app.lightwind.ext.components

class IntentReceiverActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        components.utils.intentProcessor.process(intent)

        val intent = Intent(intent)
        if (CustomTabConfig.isCustomTabIntent(SafeIntent(intent))) {
            intent.setClassName(applicationContext, CustomTabActivity::class.java.name)
        } else {
            intent.setClassName(applicationContext, BrowserActivity::class.java.name)
        }

        startActivity(intent)
        finish()
    }
}
