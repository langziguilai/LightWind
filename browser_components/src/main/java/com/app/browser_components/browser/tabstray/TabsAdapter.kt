/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.browser.tabstray

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.app.browser_components.R
import com.app.browser_components.browser.session.Session
import com.app.browser_components.concept.tabstray.TabsTray
import com.app.browser_components.support.base.observer.Observable
import com.app.browser_components.support.base.observer.ObserverRegistry

/**
 * RecyclerView adapter implementation to display a list/grid of tabs.
 */
@Suppress("TooManyFunctions")
class TabsAdapter(
    delegate: Observable<TabsTray.Observer> = ObserverRegistry()
) : RecyclerView.Adapter<TabViewHolder>(),
    TabsTray,
    Observable<TabsTray.Observer> by delegate {

    internal lateinit var tabsTray: BrowserTabsTray

    private val holders = mutableListOf<TabViewHolder>()
    private var sessions: List<Session> = listOf()
    private var selectedIndex: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        return TabViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.mozac_browser_tabstray_item,
                parent,
                false),
            tabsTray
        ).also {
            holders.add(it)
        }
    }

    override fun getItemCount() = sessions.size

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        holder.bind(sessions[position], position == selectedIndex, this)
    }

    override fun onViewRecycled(holder: TabViewHolder) {
        holder.unbind()
    }

    fun unsubscribeHolders() {
        holders.forEach { it.unbind() }
        holders.clear()
    }

    override fun displaySessions(sessions: List<Session>, selectedIndex: Int) {
        this.sessions = sessions
        this.selectedIndex = selectedIndex
        notifyDataSetChanged()
    }

    override fun updateSessions(sessions: List<Session>, selectedIndex: Int) {
        this.sessions = sessions
        this.selectedIndex = selectedIndex
    }

    override fun onSessionsInserted(position: Int, count: Int) =
        notifyItemRangeInserted(position, count)

    override fun onSessionsRemoved(position: Int, count: Int) =
        notifyItemRangeRemoved(position, count)

    override fun onSessionMoved(fromPosition: Int, toPosition: Int) =
        notifyItemMoved(fromPosition, toPosition)

    override fun onSessionsChanged(position: Int, count: Int) =
        notifyItemRangeChanged(position, count, null)
}
