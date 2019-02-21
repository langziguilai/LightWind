/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.service.telemetry.measurement;

import android.content.SharedPreferences;

import com.app.browser_components.service.telemetry.config.TelemetryConfiguration;
import com.app.browser_components.service.telemetry.ping.TelemetryPingBuilder;

public class SequenceMeasurement extends TelemetryMeasurement {
    private static final String FIELD_NAME = "seq";

    private static final String PREFERENCE_PREFIX = "sequence_";

    private final TelemetryConfiguration configuration;
    private final String preferenceKeySequence;

    public SequenceMeasurement(TelemetryConfiguration configuration, TelemetryPingBuilder ping) {
        super(FIELD_NAME);

        this.configuration = configuration;
        this.preferenceKeySequence = PREFERENCE_PREFIX + ping.getType();
    }

    @Override
    public Object flush() {
        return getAndIncrementSequence();
    }

    private synchronized long getAndIncrementSequence() {
        final SharedPreferences preferences = configuration.getSharedPreferences();

        long sequence = preferences.getLong(preferenceKeySequence, 0);

        preferences.edit()
                .putLong(preferenceKeySequence, ++sequence)
                .apply();

        return sequence;
    }
}
