/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.feature.session.bundling.db

import android.content.Context
import androidx.room.*

/**
 * Internal database for saving bundles.
 */
@Database(entities = [BundleEntity::class], version = 1)
@TypeConverters(UrlListConverter::class)
internal abstract class BundleDatabase : RoomDatabase() {
    abstract fun bundleDao(): BundleDao

    companion object {
        @Volatile private var instance: BundleDatabase? = null

        @Synchronized
        fun get(context: Context): BundleDatabase {
            instance?.let { return it }

            return Room.databaseBuilder(
                context,
                BundleDatabase::class.java,
                "bundle_database"
            ).allowMainThreadQueries().build().also { instance = it }
        }
    }
}

@Suppress("unused")
internal class UrlListConverter {
    @TypeConverter
    fun fromUrlList(urls: UrlList): String {
        return urls.entries.joinToString(separator = "\n")
    }

    @TypeConverter
    fun toUrlList(value: String): UrlList {
        return UrlList(value.split('\n'))
    }
}
