/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.service.telemetry.ping;

import org.json.JSONObject;
import com.app.browser_components.service.telemetry.config.TelemetryConfiguration;
import com.app.browser_components.service.telemetry.measurement.ArchMeasurement;
import com.app.browser_components.service.telemetry.measurement.CreatedDateMeasurementNew;
import com.app.browser_components.service.telemetry.measurement.CreatedTimestampMeasurementNew;
import com.app.browser_components.service.telemetry.measurement.DeviceMeasurement;
import com.app.browser_components.service.telemetry.measurement.FirstRunProfileDateMeasurement;
import com.app.browser_components.service.telemetry.measurement.LocaleMeasurement;
import com.app.browser_components.service.telemetry.measurement.MetricsMeasurement;
import com.app.browser_components.service.telemetry.measurement.OperatingSystemMeasurement;
import com.app.browser_components.service.telemetry.measurement.OperatingSystemVersionMeasurement;
import com.app.browser_components.service.telemetry.measurement.ProcessStartTimestampMeasurement;
import com.app.browser_components.service.telemetry.measurement.SequenceMeasurement;
import com.app.browser_components.service.telemetry.measurement.TimezoneOffsetMeasurement;

/**
 * A telemetry ping builder for events of type "mobile-metrics".
 *
 * See the schema for more details:
 *   https://github.com/mozilla-services/mozilla-pipeline-schemas/blob/master/schemas/telemetry/mobile-metrics/mobile-metrics.1.schema.json
 */
public class TelemetryMobileMetricsPingBuilder extends TelemetryPingBuilder {
    public static final String TYPE = "mobile-metrics";
    private static final int VERSION = 1;

    public TelemetryMobileMetricsPingBuilder(JSONObject snapshots, TelemetryConfiguration configuration) {
        super(configuration, TYPE, VERSION);

        addMeasurement(new ProcessStartTimestampMeasurement(configuration));
        addMeasurement(new SequenceMeasurement(configuration, this));
        addMeasurement(new LocaleMeasurement());
        addMeasurement(new DeviceMeasurement());
        addMeasurement(new ArchMeasurement());
        addMeasurement(new FirstRunProfileDateMeasurement(configuration));
        addMeasurement(new OperatingSystemMeasurement());
        addMeasurement(new OperatingSystemVersionMeasurement());
        addMeasurement(new CreatedDateMeasurementNew());
        addMeasurement(new CreatedTimestampMeasurementNew());
        addMeasurement(new TimezoneOffsetMeasurement());
        addMeasurement(new MetricsMeasurement(snapshots));
    }

    @Override
    protected String getUploadPath(final String documentId) {
        return super.getUploadPath(documentId) + "?v=4";
    }
}
