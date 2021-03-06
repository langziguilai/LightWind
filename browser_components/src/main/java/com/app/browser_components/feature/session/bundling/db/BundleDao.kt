/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.feature.session.bundling.db

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*


/**
 * Internal dao for accessing and modifying the bundles in the database.
 */
@Dao
internal interface BundleDao {
    @Insert
    fun insertBundle(bundle: BundleEntity): Long

    @Update
    fun updateBundle(bundle: BundleEntity)

    @Delete
    fun deleteBundle(bundle: BundleEntity)

    @Query("SELECT * FROM bundles ORDER BY saved_at DESC LIMIT :limit")
    fun getBundles(limit: Int = 20): LiveData<List<BundleEntity>>

    @Query("SELECT * FROM bundles")
    fun getBundlesPaged(): DataSource.Factory<Int, BundleEntity>

    @Query("SELECT * FROM bundles WHERE saved_at >= :since ORDER BY saved_at DESC LIMIT 1")
    fun getLastBundle(since: Long): BundleEntity?
}
