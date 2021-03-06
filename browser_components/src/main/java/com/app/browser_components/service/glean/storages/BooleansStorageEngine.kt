/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.service.glean.storages

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.app.browser_components.service.glean.CommonMetricData
import com.app.browser_components.support.base.log.logger.Logger

/**
 * This singleton handles the in-memory storage logic for booleans. It is meant to be used by
 * the Specific Booleans API and the ping assembling objects. No validation on the stored data
 * is performed at this point: validation must be performed by the Specific Booleans API.
 *
 * This class contains a reference to the Android application Context. While the IDE warns
 * us that this could leak, the application context lives as long as the application and this
 * object. For this reason, we should be safe to suppress the IDE warning.
 */
@SuppressLint("StaticFieldLeak")
internal object BooleansStorageEngine : BooleansStorageEngineImplementation()

internal open class BooleansStorageEngineImplementation(
    override val logger: Logger = Logger("glean/BooleansStorageEngine")
) : GenericScalarStorageEngine<Boolean>() {

    override fun deserializeSingleMetric(metricName: String, value: Any?): Boolean? {
        return value as? Boolean
    }

    override fun serializeSingleMetric(
        userPreferences: SharedPreferences.Editor?,
        storeName: String,
        value: Boolean,
        extraSerializationData: Any?
    ) {
        userPreferences?.putBoolean(storeName, value)
    }

    /**
     * Record a boolean in the desired stores.
     *
     * @param metricData object with metric settings
     * @param value the boolean value to record
     */
    @Synchronized
    fun record(
        metricData: CommonMetricData,
        value: Boolean
    ) {
        super.recordScalar(metricData, value)
    }
}
