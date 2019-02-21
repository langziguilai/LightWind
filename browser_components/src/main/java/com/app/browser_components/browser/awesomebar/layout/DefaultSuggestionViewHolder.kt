/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.app.browser_components.browser.awesomebar.layout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.app.browser_components.R
import com.app.browser_components.browser.awesomebar.BrowserAwesomeBar
import com.app.browser_components.browser.awesomebar.widget.FlowLayout
import com.app.browser_components.concept.awesomebar.AwesomeBar

internal sealed class DefaultSuggestionViewHolder {
    /**
     * Default view holder for suggestions.
     */
    internal class Default(
        private val awesomeBar: BrowserAwesomeBar,
        view: View
    ) : SuggestionViewHolder(view) {
        private val titleView = view.findViewById<TextView>(R.id.mozac_browser_awesomebar_title).apply {
            setTextColor(awesomeBar.styling.titleTextColor)
        }
        private val descriptionView = view.findViewById<TextView>(R.id.mozac_browser_awesomebar_description).apply {
            setTextColor(awesomeBar.styling.descriptionTextColor)
        }
        private val iconView = view.findViewById<ImageView>(R.id.mozac_browser_awesomebar_icon)

        override fun bind(suggestion: AwesomeBar.Suggestion, selectionListener: () -> Unit) {
            val title = if (suggestion.title.isNullOrEmpty()) suggestion.description else suggestion.title

            val icon = suggestion.icon.invoke(iconView.measuredWidth, iconView.measuredHeight)
            iconView.setImageBitmap(icon)

            titleView.text = title
            descriptionView.text = suggestion.description

            view.setOnClickListener {
                suggestion.onSuggestionClicked?.invoke()
                selectionListener.invoke()
            }
        }

        companion object {
            val LAYOUT_ID = R.layout.mozac_browser_awesomebar_item_generic
        }
    }

    /**
     * View holder for suggestions that contain chips.
     */
    internal class Chips(
        private val awesomeBar: BrowserAwesomeBar,
        view: View
    ) : SuggestionViewHolder(view) {
        private val iconView = view.findViewById<ImageView>(R.id.mozac_browser_awesomebar_icon)
        private val chipsView = view.findViewById<FlowLayout>(R.id.mozac_browser_awesomebar_chips).apply {
            spacing = awesomeBar.styling.chipSpacing
        }

        override fun bind(suggestion: AwesomeBar.Suggestion, selectionListener: () -> Unit) {
            chipsView.removeAllViews()

            val inflater = LayoutInflater.from(view.context)

            suggestion.icon.invoke(iconView.measuredWidth, iconView.measuredHeight)?.let { bitmap ->
                iconView.setImageBitmap(bitmap)
            }

            suggestion
                .chips
                .forEach { chip ->
                    val view = inflater.inflate(
                        R.layout.mozac_browser_awesomebar_chip,
                        view as ViewGroup,
                        false
                    ) as TextView

                    view.setTextColor(awesomeBar.styling.chipTextColor)
                    view.setBackgroundColor(awesomeBar.styling.chipBackgroundColor)
                    view.text = chip.title
                    view.setOnClickListener {
                        suggestion.onChipClicked?.invoke(chip)
                        selectionListener.invoke()
                    }

                    chipsView.addView(view)
                }
        }

        companion object {
            val LAYOUT_ID = R.layout.mozac_browser_awesomebar_item_chips
        }
    }
}
