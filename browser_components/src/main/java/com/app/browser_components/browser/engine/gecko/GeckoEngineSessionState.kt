/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.browser.engine.gecko

import com.app.browser_components.concept.engine.EngineSessionState
import org.json.JSONException
import org.json.JSONObject
import org.mozilla.geckoview.GeckoSession

private const val GECKO_STATE_KEY = "GECKO_STATE"

class GeckoEngineSessionState internal constructor(
    internal val actualState: GeckoSession.SessionState?
) : EngineSessionState {
    override fun toJSON() = JSONObject().apply {
        if (actualState != null) {
            put(GECKO_STATE_KEY, actualState.toString())
        }
    }

    companion object {
        fun fromJSON(json: JSONObject): GeckoEngineSessionState = try {
            val state = json.getString(GECKO_STATE_KEY)
            GeckoEngineSessionState(GeckoSession.SessionState(state))
        } catch (e: JSONException) {
            GeckoEngineSessionState(null)
        }
    }
}
