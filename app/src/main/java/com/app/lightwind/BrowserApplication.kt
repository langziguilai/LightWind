/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.lightwind

import android.app.Application
import android.content.Context
import com.app.browser_components.service.glean.Glean
import com.app.browser_components.support.base.log.Log
import com.app.browser_components.support.base.log.logger.Logger
import com.app.browser_components.support.base.log.sink.AndroidLogSink
import com.app.browser_components.support.rustlog.RustLog
import com.app.lightwind.ext.isCrashReportActive
import com.app.lightwind.settings.Settings

open class BrowserApplication : Application() {
    val components by lazy { Components(this) }

    override fun onCreate() {
        super.onCreate()

        setupCrashReporting(this)
        setupGlean(this)
        val megazordEnabled = setupMegazord()
        setupLogging(megazordEnabled)
    }

    companion object {
        const val NON_FATAL_CRASH_BROADCAST = "com.app.lightwind"
    }
}

private fun setupLogging(megazordEnabled: Boolean) {
    // We want the log messages of all builds to go to Android logcat
    Log.addSink(AndroidLogSink())

    if (megazordEnabled) {
        // We want rust logging to go through the log sinks.
        // This has to happen after initializing the megazord, and
        // it's only worth doing in the case that we are a megazord.
        RustLog.enable()
    }
}

private fun setupGlean(context: Context) {
    Glean.initialize(context)
    Glean.setUploadEnabled(BuildConfig.TELEMETRY_ENABLED && Settings.isTelemetryEnabled(context))
}

private fun setupCrashReporting(application: BrowserApplication) {
    if (isCrashReportActive) {
        application
            .components
            .analytics
            .crashReporter.install(application)
    }
}

/**
 * Initiate Megazord sequence! Megazord Battle Mode!
 *
 * Mozilla Application Services publishes many native (Rust) code libraries that stand alone: each published Android
 * ARchive (AAR) contains managed code (classes.jar) and multiple .so library files (one for each supported
 * architecture). That means consuming multiple such libraries entails at least two .so libraries, and each of those
 * libraries includes the entire Rust standard library as well as (potentially many) duplicated dependencies. To save
 * space and allow cross-component native-code Link Time Optimization (LTO, i.e., inlining, dead code elimination, etc)
 * Application Services also publishes composite libraries -- so called megazord libraries or just megazords -- that
 * compose multiple Rust components into a single optimized .so library file.
 *
 * @return Boolean indicating if we're in a megazord.
 */
private fun setupMegazord(): Boolean {
    // mozilla.appservices.ReferenceBrowserMegazord will be missing if we're doing an application-services
    // dependency substitution locally. That class is supplied dynamically by the org.mozilla.appservices
    // gradle plugin, and that won't happen if we're not megazording. We won't megazord if we're
    // locally substituting every module that's part of the megazord's definition, which is what
    // happens during a local substitution of application-services.
    // As a workaround, use reflections to conditionally initialize the megazord in case it's present.
    // See https://github.com/mozilla-mobile/reference-browser/pull/356.
    return try {
        val megazordClass = Class.forName("mozilla.appservices.ReferenceBrowserMegazord")
        val megazordInitMethod = megazordClass.getDeclaredMethod("init")
        megazordInitMethod.invoke(megazordClass)
        true
    } catch (e: ClassNotFoundException) {
        Logger.info("mozilla.appservices.ReferenceBrowserMegazord not found; skipping megazord init.")
        false
    }
}
