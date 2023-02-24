package app.lajusta.ui.visita

import android.os.Parcelable
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.parcela.ParcelaVisita
import kotlinx.parcelize.Parcelize

@Parcelize
data class Visita(
    val id_visita: Int,
    var fecha_visita: List<Int>,
    var descripcion: String?,
    var id_tecnico: Int,
    var id_quinta: Int,
    val parcelas: MutableList<ParcelaVisita>
): Parcelable, Comparable<Visita> {
    override fun compareTo(other: Visita): Int {
        return ArrayedDate.toDate(fecha_visita).compareTo(ArrayedDate.toDate(other.fecha_visita))
    }

    fun toPrefilledVisita() =
        PrefilledVisita(fecha_visita, descripcion, id_tecnico, id_quinta, parcelas)
}

@Parcelize
data class PrefilledVisita(
    var fecha_visita: List<Int>? = null,
    var descripcion: String? = null,
    var id_tecnico: Int? = null,
    var id_quinta: Int? = null,
    val parcelas: MutableList<ParcelaVisita>? = null,
    var _blocked: Boolean = false
): Parcelable