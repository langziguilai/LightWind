/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.browser.tabstray

import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.browser_components.R
import com.app.browser_components.browser.session.Session
import com.app.browser_components.concept.tabstray.TabsTray
import com.app.browser_components.support.base.observer.Observable

/**
 * A RecyclerView ViewHolder implementation for "tab" items.
 */
class TabViewHolder(
    itemView: View,
    private val tabsTray: BrowserTabsTray
) : RecyclerView.ViewHolder(itemView), Session.Observer {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val cardView: CardView = (itemView as CardView).apply {
        elevation = tabsTray.styling.itemElevation
    }
    private val tabView: TextView = itemView.findViewById(R.id.mozac_browser_tabstray_url)
    private val closeView: AppCompatImageButton = itemView.findViewById(R.id.mozac_browser_tabstray_close)
    private val thumbnailView: ImageView = itemView.findViewById(R.id.mozac_browser_tabstray_thumbnail)

    private var session: Session? = null

    /**
     * Displays the data of the given session and notifies the given observable about events.
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun bind(session: Session, isSelected: Boolean, observable: Observable<TabsTray.Observer>) {
        this.session = session.also { it.register(this) }

        val title = if (session.title.isNotEmpty()) {
            session.title
        } else {
            session.url
        }

        tabView.text = title

        itemView.setOnClickListener {
            observable.notifyObservers { onTabSelected(session) }
        }

        closeView.setOnClickListener {
            observable.notifyObservers { onTabClosed(session) }
        }

        if (isSelected) {
            tabView.setTextColor(tabsTray.styling.selectedItemTextColor)
            cardView.setCardBackgroundColor(tabsTray.styling.selectedItemBackgroundColor)
            closeView.imageTintList = ColorStateList.valueOf(tabsTray.styling.selectedItemTextColor)
        } else {
            tabView.setTextColor(tabsTray.styling.itemTextColor)
            cardView.setCardBackgroundColor(tabsTray.styling.itemBackgroundColor)
            closeView.imageTintList = ColorStateList.valueOf(tabsTray.styling.itemTextColor)
        }

        thumbnailView.setImageBitmap(session.thumbnail)
    }

    /**
     * The attached view no longer needs to display any data.
     */
    fun unbind() {
        session?.unregister(this)
    }

    override fun onUrlChanged(session: Session, url: String) {
        tabView.text = url
    }
}
