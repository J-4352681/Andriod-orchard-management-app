package app.lajusta.ui.ronda

import android.os.Parcelable
import app.lajusta.ui.generic.ArrayedDate
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ronda(
    val id_ronda: Int,
    var fecha_fin: List<Int>?,
    var fecha_inicio: List<Int>,
    //var verdura: Verdura  //Tiene una verdura pero esta en null en las api...
): Parcelable {
    override fun toString(): String =
        ArrayedDate.toString(fecha_inicio) + " - " + ArrayedDate.toString(fecha_fin!!)

    companion object {
        fun filter(
            rondas: MutableList<Ronda>,
            rondasOriginales: MutableList<Ronda>,
            query: String?
        ): MutableList<Ronda> {
            rondas.clear()
            if(query.isNullOrEmpty()) rondas.addAll(rondasOriginales)
            else rondas.addAll(
                rondasOriginales.filter { ronda ->
                    ArrayedDate.toString(ronda.fecha_inicio).contains(query)
                    || ArrayedDate.toString(ronda.fecha_fin!!).contains(query)
                }
            )
            return rondas
        }
    }
}


@Parcelize
data class Rondas(
    val rondas: List<Ronda>
): ArrayList<Ronda>(rondas), Parcelable