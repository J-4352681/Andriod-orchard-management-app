package app.lajusta.ui.ronda

import android.os.Parcelable
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.visita.Visita
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Ronda(
    val id_ronda: Int,
    var fecha_fin: List<Int>?,
    var fecha_inicio: List<Int>,
    //var verdura: Verdura  //Tiene una verdura pero esta en null en las api...
): Parcelable, Comparable<Ronda> {

    override fun compareTo(other: Ronda): Int {
        return ArrayedDate.toDate(fecha_inicio).compareTo(ArrayedDate.toDate(other.fecha_inicio))
    }

    override fun toString(): String {
        val base = "del ${ArrayedDate.toString(fecha_inicio)}"
        if(fecha_fin != null) return "$base al ${ArrayedDate.toString(fecha_fin!!)}"
        return "$base sin fecha de fin"
    }

    fun toPrefilledRonda() =
        PrefilledRonda(fecha_fin, fecha_inicio)

    fun toBlockedPrefilledRonda() =
        PrefilledRonda(fecha_fin, fecha_inicio, true, true)

    fun isActive(): Boolean =
        fecha_fin.isNullOrEmpty()
        || (
            LocalDate.now() < ArrayedDate.toLocalDate(fecha_fin!!)
            && LocalDate.now() > ArrayedDate.toLocalDate(fecha_inicio)
        )

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
data class PrefilledRonda(
    var fecha_fin: List<Int>?,
    var fecha_inicio: List<Int>?,
    var _blockFields: Boolean = false,
    var _blockSubmitAction: Boolean = false
): Parcelable


@Parcelize
data class Rondas(
    val rondas: List<Ronda>
): ArrayList<Ronda>(rondas), Parcelable