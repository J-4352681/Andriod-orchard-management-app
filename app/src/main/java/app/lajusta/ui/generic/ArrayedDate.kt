package app.lajusta.ui.generic

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ArrayedDate() {
    companion object{
        private val c = Calendar.getInstance()
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

        fun toDate(date: List<Int>): Date {
            return sdf.parse(toString(date))!!
        }

        fun toLocalDate(date: List<Int>): LocalDate = LocalDate.of(date[0], date[1], date[2])

        fun todayStringed(): String = toString(todayArrayed().also { it[1]+=1  })

        fun todayArrayed(): MutableList<Int> = mutableListOf(
            c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        )

        fun datePickerListener(
            context: Context,
            tv: TextView,
            dateSetListener: OnDateSetListener
        ): (View) -> Unit {
            return {
                val arrayedDate = toArray(tv.text.toString())
                DatePickerDialog(
                    context,
                    { _, year, monthOfYear, dayOfMonth ->
                        tv.text = (
                            dayOfMonth.toString() + delimiter
                            + (monthOfYear + 1) + delimiter + year
                        )
                    }, arrayedDate[0], arrayedDate[1] - 1, arrayedDate[2]
                ).also {
                    it.setOnDateSetListener(dateSetListener)
                    it.show()
                }
            }
        }

        fun laterThanToday(date: String): Boolean {
            /*return sdf.parse(date)!! > sdf.parse(todayStringed())*/

            val arrayDate = toArray(date)
            val arrayToday = todayArrayed().also { it[1]+=1  }

            Log.e("Test", "$arrayDate > $arrayToday")

            return greaterThanArrayedDate(arrayDate, arrayToday)
        }

        fun greaterThanArrayedDate( date1:List<Int>, date2:List<Int>):Boolean {
            return if ( date1[0] != date2[0]) date1[0] > date2[0]
            else if ( date1[1] != date2[1] ) date1[1] > date2[1]
            else date1[2] > date2[2]
        }

    }
}