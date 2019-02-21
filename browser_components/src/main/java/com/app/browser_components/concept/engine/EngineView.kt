/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.concept.engine



import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * View component that renders web content.
 */
interface EngineView {

    /**
     * Convenience method to cast the implementation of this interface to an Android View object.
     */
    fun asView(): View = this as View

    /**
     * Render the content of the given session.
     */
    fun render(session: EngineSession)

    /**
     * To be called in response to [Lifecycle.Event.ON_RESUME]. See [EngineView]
     * implementations for details.
     */
    fun onResume() = Unit

    /**
     * To be called in response to [Lifecycle.Event.ON_PAUSE]. See [EngineView]
     * implementations for details.
     */
    fun onPause() = Unit

    /**
     * To be called in response to [Lifecycle.Event.ON_START]. See [EngineView]
     * implementations for details.
     */
    fun onStart() = Unit

    /**
     * To be called in response to [Lifecycle.Event.ON_STOP]. See [EngineView]
     * implementations for details.
     */
    fun onStop() = Unit

    /**
     * To be called in response to [Lifecycle.Event.ON_CREATE]. See [EngineView]
     * implementations for details.
     */
    fun onCreate() = Unit

    /**
     * To be called in response to [Lifecycle.Event.ON_DESTROY]. See [EngineView]
     * implementations for details.
     */
    fun onDestroy() = Unit

    /**
    * Check if [EngineView] can be scrolled vertically down.
    * true if can and false otherwise.
    */
    fun canScrollVerticallyDown(): Boolean = true
}

/**
 * [LifecycleObserver] which dispatches lifecycle events to an [EngineView].
 */
class LifecycleObserver(val engineView: EngineView) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        engineView.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        engineView.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        engineView.onStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        engineView.onStop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        engineView.onCreate()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        engineView.onDestroy()
    }
}
