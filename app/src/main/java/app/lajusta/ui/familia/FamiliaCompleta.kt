package app.lajusta.ui.familia

import android.os.Parcelable
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.ronda.Ronda
import kotlinx.parcelize.Parcelize

@Parcelize
data class FamiliaCompleta(
    val id_fp: Int,
    var nombre: String,
    var fecha_afiliacion: List<Int>,
    val quintas: MutableList<Quinta>,
    val bolsones: MutableList<Bolson>,
    val rondas: MutableList<Ronda>
): Parcelable {
    fun toFamilia(): Familia {
        return Familia(id_fp, nombre, fecha_afiliacion)
    }

    companion object {
        fun toFamiliaCompleta(
            familia: Familia,
            quintas: MutableList<Quinta>,
            bolsones: MutableList<Bolson>,
            rondas: MutableList<Ronda>
        ): FamiliaCompleta {
            return FamiliaCompleta(
                familia.id_fp, familia.nombre, familia.fecha_afiliacion, quintas, bolsones, rondas
            )
        }

        fun filter(
            familiasCompletas: MutableList<FamiliaCompleta>,
            familiasCompletasOriginales: MutableList<FamiliaCompleta>,
            query: String?
        ): MutableList<FamiliaCompleta> {
            familiasCompletas.clear()
            if(query.isNullOrEmpty()) familiasCompletas.addAll(familiasCompletasOriginales)
            else familiasCompletas.addAll(
                familiasCompletasOriginales.filter { familia ->
                    familia.nombre.lowercase().contains(query)
                    || familia.quintas.any { it.nombre == query }
                    || ArrayedDate.toString(familia.fecha_afiliacion).contains(query)
                }
            )
            return familiasCompletas
        }
    }
}

@Parcelize
data class FamiliasCompletas(
    val familias: List<FamiliaCompleta>
): ArrayList<FamiliaCompleta>(familias), Parcelable
