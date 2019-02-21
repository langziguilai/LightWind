/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.service.telemetry.ping;

import com.app.browser_components.service.telemetry.config.TelemetryConfiguration;
import com.app.browser_components.service.telemetry.measurement.CreatedTimestampMeasurement;
import com.app.browser_components.service.telemetry.measurement.DeviceMeasurement;
import com.app.browser_components.service.telemetry.measurement.EventsMeasurement;
import com.app.browser_components.service.telemetry.measurement.LocaleMeasurement;
import com.app.browser_components.service.telemetry.measurement.OperatingSystemMeasurement;
import com.app.browser_components.service.telemetry.measurement.OperatingSystemVersionMeasurement;
import com.app.browser_components.service.telemetry.measurement.PocketIdMeasurement;
import com.app.browser_components.service.telemetry.measurement.ProcessStartTimestampMeasurement;
import com.app.browser_components.service.telemetry.measurement.SequenceMeasurement;
import com.app.browser_components.service.telemetry.measurement.TimezoneOffsetMeasurement;

/**
 * A telemetry ping builder for events of type "fire-tv-events".
 *
 * See the schema for more details:
 *   https://github.com/mozilla-services/mozilla-pipeline-schemas/blob/dc458113a7a523e60a9ba50e1174a3b1e0cfdc24/schemas/pocket/fire-tv-events/fire-tv-events.1.schema.json
 */
public class TelemetryPocketEventPingBuilder extends TelemetryPingBuilder {
    public static final String TYPE = "fire-tv-events";
    private static final int VERSION = 1;

    private EventsMeasurement eventsMeasurement;

    public TelemetryPocketEventPingBuilder(TelemetryConfiguration configuration) {
        super(configuration, TYPE, VERSION);

        addMeasurement(new PocketIdMeasurement(configuration));
        addMeasurement(new ProcessStartTimestampMeasurement(configuration));
        addMeasurement(new SequenceMeasurement(configuration, this));
        addMeasurement(new LocaleMeasurement());
        addMeasurement(new DeviceMeasurement());
        addMeasurement(new OperatingSystemMeasurement());
        addMeasurement(new OperatingSystemVersionMeasurement());
        addMeasurement(new CreatedTimestampMeasurement());
        addMeasurement(new TimezoneOffsetMeasurement());
        addMeasurement(eventsMeasurement = new EventsMeasurement(configuration));
    }

    @Override
    protected boolean shouldIncludeClientId() {
        return false;
    }

    public EventsMeasurement getEventsMeasurement() {
        return eventsMeasurement;
    }

    @Override
    protected String getUploadPath(final String documentId) {
        return String.format("/submit/pocket/%s/%s/%s",
                getType(),
                VERSION,
                documentId);
    }
}
