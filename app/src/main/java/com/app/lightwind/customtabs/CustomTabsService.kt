/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.app.lightwind.customtabs

import com.app.browser_components.concept.engine.Engine
import com.app.browser_components.feature.customtabs.AbstractCustomTabsService
import com.app.lightwind.ext.components

class CustomTabsService : AbstractCustomTabsService() {
    override val engine: Engine by lazy { components.core.engine }
}
