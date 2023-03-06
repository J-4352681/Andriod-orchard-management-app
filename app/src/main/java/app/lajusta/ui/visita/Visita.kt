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
        return ArrayedDate.toLocalDate(fecha_visita).compareTo(ArrayedDate.toLocalDate(other.fecha_visita))
    }

    fun toPrefilledVisita() =
        PrefilledVisita(fecha_visita, descripcion, id_tecnico, id_quinta, parcelas)

    fun toBlockedPrefilledVisita() =
        PrefilledVisita(
            fecha_visita, descripcion, id_tecnico, id_quinta,
            parcelas, true, true
        )
}

@Parcelize
data class PrefilledVisita(
    var fecha_visita: List<Int>? = null,
    var descripcion: String? = null,
    var id_tecnico: Int? = null,
    var id_quinta: Int? = null,
    val parcelas: MutableList<ParcelaVisita>? = null,
    var _blockFields: Boolean = false,
    var _blockSubmitAction: Boolean = false
): Parcelable