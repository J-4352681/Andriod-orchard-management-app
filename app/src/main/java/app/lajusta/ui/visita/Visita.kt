package app.lajusta.ui.visita

import android.os.Parcelable
import app.lajusta.ui.Parcela.Parcela
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Visita(
    val id_visita: Int,
    val fecha_visita: Date,
    val descripcion: String?,
    val id_tecnico: Int,
    val id_quinta: Int,
    var parcelas: List<Parcela>
): Parcelable