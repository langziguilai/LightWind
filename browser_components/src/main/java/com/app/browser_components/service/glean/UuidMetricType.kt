/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.service.glean


import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.launch
import java.util.UUID

import com.app.browser_components.service.glean.storages.UuidsStorageEngine
import com.app.browser_components.support.base.log.logger.Logger

/**
 * This implements the developer facing API for recording uuids.
 *
 * Instances of this class type are automatically generated by the parsers at build time,
 * allowing developers to record values that were previously registered in the metrics.yaml file.
 *
 * The uuid API exposes the [generateAndSet] and [set] methods.
 */
data class UuidMetricType(
    override val disabled: Boolean,
    override val category: String,
    override val lifetime: Lifetime,
    override val name: String,
    override val sendInPings: List<String>
) : CommonMetricData {

    override val defaultStorageDestinations: List<String> = listOf("metrics")

    private val logger = Logger("glean/UuidMetricType")

    /**
     * Generate a new UUID value and set it in the metric store.
     *
     * @return a [UUID] or [null] if we're not allowed to record.
     */
    fun generateAndSet(): UUID? {
        // Even if `set` is already checking if we're allowed to record,
        // we need to check here as well otherwise we'd return a `UUID`
        // that won't be stored anywhere.
        if (!shouldRecord(logger)) {
            return null
        }

        val uuid = UUID.randomUUID()
        set(uuid)
        return uuid
    }

    /**
     * Explicitly set an existing UUID value
     *
     * @param value a valid [UUID] to set the metric to
     */
    fun set(value: UUID) {
        // TODO report errors through other special metrics handled by the SDK. See bug 1499761.

        if (!shouldRecord(logger)) {
            return
        }

        Dispatchers.API.launch {
            // Delegate storing the event to the storage engine.
            UuidsStorageEngine.record(
                this@UuidMetricType,
                value = value
            )
        }
    }

    /**
     * Tests whether a value is stored for the metric for testing purposes only
     *
     * @param pingName represents the name of the ping to retrieve the metric for.  Defaults
     *                 to the either the first value in [defaultStorageDestinations] or the first
     *                 value in [sendInPings]
     * @return true if metric value exists, otherwise false
     */
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun testHasValue(pingName: String = getStorageNames().first()): Boolean {
        return UuidsStorageEngine
            .getSnapshot(pingName, false)?.get(identifier) != null
    }

    /**
     * Returns the stored value for testing purposes only
     *
     * @param pingName represents the name of the ping to retrieve the metric for.  Defaults
     *                 to the either the first value in [defaultStorageDestinations] or the first
     *                 value in [sendInPings]
     * @return value of the stored metric
     * @throws [NullPointerException] if no value is stored
     */
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun testGetValue(pingName: String = getStorageNames().first()): UUID {
        return UuidsStorageEngine.getSnapshot(pingName, false)!![identifier]!!
    }
}
