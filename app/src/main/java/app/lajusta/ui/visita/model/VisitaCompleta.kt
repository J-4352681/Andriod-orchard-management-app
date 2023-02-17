package app.lajusta.ui.visita.model

import android.os.Parcelable
import app.lajusta.ui.parcela.Parcela
import app.lajusta.ui.parcela.ParcelaVisita
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.usuarios.Usuario
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class VisitaCompleta(
    val id_visita: Int,
    var fecha_visita: List<Int>,
    var descripcion: String?,
    var Tecnico: Usuario,
    var quinta: Quinta,
    var parcelas: List<ParcelaVisita>
): Parcelable