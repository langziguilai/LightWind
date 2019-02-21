/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.lightwind

import android.content.Context
import com.app.browser_components.browser.errorpages.ErrorPages
import com.app.browser_components.browser.errorpages.ErrorType
import com.app.browser_components.concept.engine.EngineSession
import com.app.browser_components.concept.engine.request.RequestInterceptor
import com.app.lightwind.ext.components
import com.app.lightwind.settings.AboutPage
import com.app.lightwind.tabs.PrivatePage

/**
 * NB, and FIXME: this class is consumed by a 'Core' component group, but itself relies on 'firefoxAccountsFeature'
 * component; this creates a circular dependency, since firefoxAccountsFeature relies on tabsUseCases
 * which in turn needs 'core' itself.
 */
class AppRequestInterceptor(private val context: Context) : RequestInterceptor {
    override fun onLoadRequest(session: EngineSession, uri: String): RequestInterceptor.InterceptionResponse? {
        return when (uri) {
            "about:privatebrowsing" -> {
                val page = PrivatePage.createPrivateBrowsingPage(context, uri)
                return RequestInterceptor.InterceptionResponse.Content(page, encoding = "base64")
            }

            "about:version" -> {
                val page = AboutPage.createAboutPage(context)
                return RequestInterceptor.InterceptionResponse.Content(page, encoding = "base64")
            }

            else -> context.components.services.accountsAuthFeature.interceptor.onLoadRequest(session, uri)
        }
    }

    override fun onErrorRequest(
        session: EngineSession,
        errorType: ErrorType,
        uri: String?
    ): RequestInterceptor.ErrorResponse? {
        return RequestInterceptor.ErrorResponse(ErrorPages.createErrorPage(context, errorType))
    }
}
