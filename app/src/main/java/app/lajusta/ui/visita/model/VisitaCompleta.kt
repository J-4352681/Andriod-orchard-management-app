package app.lajusta.ui.visita.model

import android.os.Parcelable
import app.lajusta.ui.parcela.Parcela
import app.lajusta.ui.parcela.ParcelaVisita
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.visita.Visita
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class VisitaCompleta(
    val id_visita: Int,
    var fecha_visita: List<Int>,
    var descripcion: String?,
    var tecnico: Usuario,
    var quinta: Quinta,
    var parcelas: List<ParcelaVisita>
): Parcelable{

companion object {
    fun toVisitaCompleta(visita: Visita, tecnico: Usuario, quinta: Quinta): VisitaCompleta {
        return VisitaCompleta(
            visita.id_visita,
            visita.fecha_visita,
            visita.descripcion,
            tecnico,
            quinta,
            visita.parcelas
        )
    }
}}