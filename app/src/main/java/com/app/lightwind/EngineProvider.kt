package com.app.lightwind

import android.content.Context
import com.app.browser_components.browser.engine.system.SystemEngine
import com.app.browser_components.concept.engine.DefaultSettings
import com.app.browser_components.concept.engine.Engine

object EngineProvider {
    fun getEngine(context: Context, defaultSettings: DefaultSettings): Engine {
//        defaultSettings.apply {
//            trackingProtectionPolicy = EngineSession.TrackingProtectionPolicy.all()
//        }
        return SystemEngine(context, defaultSettings)
    }
}