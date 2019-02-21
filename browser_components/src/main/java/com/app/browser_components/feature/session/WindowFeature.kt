/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.feature.session

import com.app.browser_components.browser.session.SelectionAwareSessionObserver
import com.app.browser_components.browser.session.Session
import com.app.browser_components.browser.session.SessionManager
import com.app.browser_components.concept.engine.Engine
import com.app.browser_components.concept.engine.window.WindowRequest
import com.app.browser_components.support.base.feature.LifecycleAwareFeature

/**
 * Feature implementation for handling window requests.
 */
class WindowFeature(private val engine: Engine, private val sessionManager: SessionManager) : LifecycleAwareFeature {

    internal val windowObserver = object : SelectionAwareSessionObserver(sessionManager) {
        override fun onOpenWindowRequested(session: Session, windowRequest: WindowRequest): Boolean {
            val newSession = Session(windowRequest.url, session.private)
            val newEngineSession = engine.createSession(session.private)
            windowRequest.prepare(newEngineSession)

            sessionManager.add(newSession, true, newEngineSession, parent = session)
            windowRequest.start()
            return true
        }

        override fun onCloseWindowRequested(session: Session, windowRequest: WindowRequest): Boolean {
            sessionManager.remove(session)
            return true
        }
    }

    /**
     * Starts the feature and a observer to listen for window requests.
     */
    override fun start() {
        windowObserver.observeSelected()
    }

    /**
     * Stops the feature and the window request observer.
     */
    override fun stop() {
        windowObserver.stop()
    }
}
