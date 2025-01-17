package com.goodwy.contacts.dialogs

import androidx.appcompat.app.AlertDialog
import com.goodwy.commons.activities.BaseSimpleActivity
import com.goodwy.commons.extensions.*
import com.goodwy.commons.helpers.isSPlus
import com.goodwy.contacts.R
import com.goodwy.contacts.extensions.config
import kotlinx.android.synthetic.main.dialog_date_picker.view.*
import org.joda.time.DateTime
import java.util.*
import kotlin.text.toInt

class MyDatePickerDialog(val activity: BaseSimpleActivity, val defaultDate: String, val callback: (dateTag: String) -> Unit) {
    private var view = activity.layoutInflater.inflate(R.layout.dialog_date_picker, null)

    init {
        //activity.getAlertDialogBuilder()
        AlertDialog.Builder(activity, R.style.MyDialogTheme_Black)
            .setPositiveButton(R.string.ok) { dialog, which -> dialogConfirmed() }
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(view, this) { alertDialog ->
                    val today = Calendar.getInstance()
                    var year = today.get(Calendar.YEAR)
                    var month = today.get(Calendar.MONTH)
                    var day = today.get(Calendar.DAY_OF_MONTH)

                    if (defaultDate.isNotEmpty()) {
                        val ignoreYear = defaultDate.startsWith("-")
                        view.hide_year.isChecked = ignoreYear

                        if (ignoreYear) {
                            month = defaultDate.substring(2, 4).toInt() - 1
                            day = defaultDate.substring(5, 7).toInt()
                        } else {
                            year = defaultDate.substring(0, 4).toInt()
                            month = defaultDate.substring(5, 7).toInt() - 1
                            day = defaultDate.substring(8, 10).toInt()
                        }
                    }

                    if (activity.config.isUsingSystemTheme && isSPlus() || activity.isBlackTheme()) {
                        val dialogBackgroundColor = activity.getColor(R.color.you_dialog_background_color)
                        view.dialog_holder.setBackgroundColor(dialogBackgroundColor)
                        view.date_picker.setBackgroundColor(dialogBackgroundColor)
                    }

                    view.date_picker.updateDate(year, month, day)
                }
            }
    }

    private fun dialogConfirmed() {
        val year = view.date_picker.year
        val month = view.date_picker.month + 1
        val day = view.date_picker.dayOfMonth
        val date = DateTime().withDate(year, month, day).withTimeAtStartOfDay()

        val tag = if (view.hide_year.isChecked) {
            date.toString("--MM-dd")
        } else {
            date.toString("yyyy-MM-dd")
        }

        callback(tag)
    }
}
