package app.lajusta.ui.generic

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.TextView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ArrayedDate() {
    companion object{
        private val c = Calendar.getInstance()
        private val year = c.get(Calendar.YEAR)
        private val month = c.get(Calendar.MONTH)
        private val day = c.get(Calendar.DAY_OF_MONTH)
        private const val delimiter = "/"
        private val sdf = SimpleDateFormat("dd/mm/yyyy")

        fun toArray(date: String): List<Int> {
            return date.split(delimiter).map { it.toInt() }.reversed()
        }

        fun toString(date: List<Int>): String {
            return (
                date[2].toString() + delimiter
                + date[1] + delimiter + date[0]
            )
        }

        fun todayStringed(): String = toString(todayArrayed())

        fun todayArrayed(): List<Int> = listOf(
            c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        )

        fun datePickerListener(context: Context, tv: TextView): (View) -> Unit {
            return {
                var arrayedDate = toArray(tv.text.toString())
                DatePickerDialog(
                    context,
                    { _, year, monthOfYear, dayOfMonth ->
                        tv.text = (
                            dayOfMonth.toString() + delimiter
                            + (monthOfYear + 1) + delimiter + year
                        )
                    }, arrayedDate[0], arrayedDate[1] - 1, arrayedDate[2]
                ).show()
            }
        }

        fun laterThanToday(date: String): Boolean {
            return sdf.parse(date) > sdf.parse(todayStringed())
        }
    }
}