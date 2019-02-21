/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.feature.toolbar

import com.app.browser_components.concept.toolbar.Toolbar
import com.app.browser_components.feature.session.SessionUseCases
import com.app.browser_components.support.ktx.kotlin.isUrl
import com.app.browser_components.support.ktx.kotlin.toNormalizedUrl

/**
 * Connects a toolbar instance to the browser engine via use cases
 */
class ToolbarInteractor(
    private val toolbar: Toolbar,
    private val loadUrlUseCase: SessionUseCases.LoadUrlUseCase,
    private val searchUseCase: SearchUseCase? = null
) {

    /**
     * Starts this interactor. Makes sure this interactor is listening
     * to relevant UI changes and triggers the corresponding use-cases
     * in response.
     */
    fun start() {
        toolbar.setOnUrlCommitListener { text ->
            if (text.isUrl()) {
                loadUrlUseCase.invoke(text.toNormalizedUrl())
            } else {
                searchUseCase?.invoke(text) ?: loadUrlUseCase.invoke(text)
            }
        }
    }

    /**
     * Stops this interactor.
     */
    fun stop() {
        toolbar.setOnUrlCommitListener { }
    }
}
