package app.lajusta.ui.visita

import android.os.Parcelable
import app.lajusta.ui.parcela.Parcela
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Visita(
    val id_visita: Int,
    val fecha_visita: List<Int>,
    val descripcion: String?,
    val id_tecnico: Int,
    val id_quinta: Int,
    var parcelas: List<Parcela>
): Parcelable