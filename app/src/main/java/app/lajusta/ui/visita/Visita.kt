package app.lajusta.ui.visita

import android.os.Parcelable
import app.lajusta.ui.parcela.Parcela
import app.lajusta.ui.parcela.ParcelaVisita
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Visita(
    val id_visita: Int,
    var fecha_visita: List<Int>,
    var descripcion: String?,
    var id_tecnico: Int,
    var id_quinta: Int,
    var parcelas: List<ParcelaVisita>
): Parcelable