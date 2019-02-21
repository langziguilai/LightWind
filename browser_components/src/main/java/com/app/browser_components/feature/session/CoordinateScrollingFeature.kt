/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.feature.session

import android.view.View
import com.app.browser_components.browser.session.SelectionAwareSessionObserver
import com.app.browser_components.browser.session.Session
import com.app.browser_components.browser.session.SessionManager
import com.app.browser_components.concept.engine.EngineView
import com.app.browser_components.support.base.feature.LifecycleAwareFeature
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.*

/**
 * Feature implementation for connecting an [EngineView] with any View that you want to coordinate scrolling
 * behavior with.
 *
 * A use case could be collapsing a toolbar every time that the user scrolls.
 */
class CoordinateScrollingFeature(
    sessionManager: SessionManager,
    private val engineView: EngineView,
    private val view: View,
    private val scrollFlags: Int = DEFAULT_SCROLL_FLAGS
) : SelectionAwareSessionObserver(sessionManager), LifecycleAwareFeature {

    /**
     * Start feature: Starts adding scrolling behavior for the indicated view.
     */
    override fun start() {
        observeSelected()
    }

    override fun onLoadingStateChanged(session: Session, loading: Boolean) {

        val params = view.layoutParams as AppBarLayout.LayoutParams

        if (engineView.canScrollVerticallyDown()) {
            params.scrollFlags = scrollFlags
        } else {
            params.scrollFlags = 0
        }

        view.layoutParams = params
    }

    companion object {
        const val DEFAULT_SCROLL_FLAGS = SCROLL_FLAG_SCROLL or
            SCROLL_FLAG_ENTER_ALWAYS or
            SCROLL_FLAG_SNAP or
            SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
    }
}
