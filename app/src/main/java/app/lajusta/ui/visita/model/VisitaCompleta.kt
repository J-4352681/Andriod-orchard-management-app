package app.lajusta.ui.visita.model

import android.os.Parcelable
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.parcela.ParcelaVisita
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.usuarios.Usuario
import app.lajusta.ui.visita.Visita
import kotlinx.parcelize.Parcelize

@Parcelize
data class VisitaCompleta(
    val id_visita: Int,
    var fecha_visita: List<Int>,
    var descripcion: String?,
    var tecnico: Usuario,
    var quinta: Quinta,
    var parcelas: List<ParcelaVisita>
): Parcelable {

    fun toVisita(): Visita {
        return Visita(id_visita, fecha_visita, descripcion, tecnico.id_user, quinta.id_quinta, parcelas)
    }

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

        fun filter(
            visitasCompletas: MutableList<VisitaCompleta>,
            visitasCompletasOriginal: MutableList<VisitaCompleta>,
            query: String?
        ): MutableList<VisitaCompleta> {
            visitasCompletas.clear()
            if(query.isNullOrEmpty()) visitasCompletas.addAll(visitasCompletasOriginal)
            else visitasCompletas.addAll(
                visitasCompletasOriginal.filter { visita ->
                    visita.tecnico.nombre.lowercase().contains(query)
                            || ArrayedDate.toString(visita.fecha_visita).contains(query)
                            || visita.quinta.nombre.lowercase().contains(query)
                            || visita.parcelas.filter {
                        it.verdura.nombre.lowercase().contains(query)
                    }.isNotEmpty()
                            || visita.descripcion!!.lowercase().contains(query)
                }
            )
            return visitasCompletas
        }
    }}