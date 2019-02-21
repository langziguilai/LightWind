/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.lightwind

import android.content.ComponentCallbacks2
import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import com.app.browser_components.browser.tabstray.BrowserTabsTray
import com.app.browser_components.concept.engine.EngineView
import com.app.browser_components.concept.tabstray.TabsTray
import com.app.browser_components.feature.intent.IntentProcessor
import com.app.browser_components.lib.crash.Crash
import com.app.browser_components.support.base.feature.BackHandler
import com.app.browser_components.support.utils.SafeIntent
import com.app.lightwind.R.string.crash_report_non_fatal_action
import com.app.lightwind.R.string.crash_report_non_fatal_message
import com.app.lightwind.browser.BrowserFragment
import com.app.lightwind.browser.CrashIntegration
import com.app.lightwind.ext.components
import com.app.lightwind.ext.isCrashReportActive
import com.app.lightwind.telemetry.DataReportingNotification

open class BrowserActivity : AppCompatActivity(), ComponentCallbacks2 {

    private lateinit var crashIntegration: CrashIntegration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val sessionId = SafeIntent(intent).getStringExtra(IntentProcessor.ACTIVE_SESSION_ID)
            supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.container, BrowserFragment.create(sessionId))
                commit()
            }
        }

        if (isCrashReportActive) {
            crashIntegration = CrashIntegration(this, components.analytics.crashReporter) { crash ->
                onNonFatalCrash(crash)
            }
            lifecycle.addObserver(crashIntegration)
        }

        DataReportingNotification.checkAndNotifyPolicy(this)
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is BackHandler && it.onBackPressed()) {
                return
            }
        }

        super.onBackPressed()
    }

    override fun onUserLeaveHint() {
        supportFragmentManager.fragments.forEach {
            if (it is UserInteractionHandler && it.onHomePressed()) {
                return
            }
        }

        super.onUserLeaveHint()
    }

    override fun onCreateView(parent: View?, name: String?, context: Context, attrs: AttributeSet?): View? =
        when (name) {
            EngineView::class.java.name -> components.core.engine.createView(context, attrs).asView()
            TabsTray::class.java.name -> BrowserTabsTray(context, attrs)
            else -> super.onCreateView(parent, name, context, attrs)
        }

    override fun onTrimMemory(level: Int) {
        components.core.sessionManager.onLowMemory()
    }

    private fun onNonFatalCrash(crash: Crash) {
        Snackbar.make(findViewById(android.R.id.content), crash_report_non_fatal_message, LENGTH_LONG)
            .setAction(crash_report_non_fatal_action) { _ ->
                crashIntegration.sendCrashReport(crash)
            }.show()
    }
}
