/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.lightwind.settings

import android.os.Bundle
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import com.app.browser_components.concept.engine.EngineSession.TrackingProtectionPolicy
import com.app.browser_components.service.glean.Glean
import com.app.lightwind.R
import com.app.lightwind.ext.getPreferenceKey
import com.app.lightwind.ext.requireComponents

class PrivacySettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.privacy_preferences, rootKey)

        val telemetryKey = context?.getPreferenceKey(R.string.pref_key_telemetry)
        val trackingProtectionNormalKey = context?.getPreferenceKey(R.string.pref_key_tracking_protection_normal)
        val trackingProtectionPrivateKey = context?.getPreferenceKey(R.string.pref_key_tracking_protection_private)

        val prefTelemetry = findPreference(telemetryKey)
        val prefTrackingProtectionNormal = findPreference(trackingProtectionNormalKey)
        val prefTrackingProtectionPrivate = findPreference(trackingProtectionPrivateKey)

        prefTelemetry.onPreferenceChangeListener = getChangeListenerForTelemetry()
        prefTrackingProtectionNormal.onPreferenceChangeListener = getChangeListenerForTrackingProtection { enabled ->
            requireComponents.core.createTrackingProtectionPolicy(normalMode = enabled)
        }
        prefTrackingProtectionPrivate.onPreferenceChangeListener = getChangeListenerForTrackingProtection { enabled ->
            requireComponents.core.createTrackingProtectionPolicy(privateMode = enabled)
        }
    }

    private fun getChangeListenerForTelemetry(): OnPreferenceChangeListener {
        return OnPreferenceChangeListener { _, value ->
            val enabled = value as Boolean
            Glean.setUploadEnabled(enabled)
            true
        }
    }

    private fun getChangeListenerForTrackingProtection(
        createTrackingProtectionPolicy: (Boolean) -> TrackingProtectionPolicy
    ): OnPreferenceChangeListener {
        return OnPreferenceChangeListener { _, value ->
            val policy = createTrackingProtectionPolicy(value as Boolean)
            with(requireComponents.core) {
                engine.settings.trackingProtectionPolicy = policy

                with(sessionManager) {
                    sessions.forEach { getEngineSession(it)?.enableTrackingProtection(policy) }
                }
            }
            true
        }
    }
}
