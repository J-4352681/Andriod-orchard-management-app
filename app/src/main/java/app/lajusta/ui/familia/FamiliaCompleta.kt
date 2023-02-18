package app.lajusta.ui.familia

import android.os.Parcelable
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.quinta.Quinta
import kotlinx.parcelize.Parcelize

@Parcelize
data class FamiliaCompleta(
    var id_fp: Int,
    var nombre: String,
    var fecha_afiliacion: List<Int>,
    val quintas: MutableList<Quinta>,
    val bolsones: MutableList<Bolson>
): Parcelable {
    fun toFamilia(): Familia {
        return Familia(id_fp, nombre, fecha_afiliacion)
    }

    companion object {
        fun toFamiliaCompleta(
            familia: Familia,
            quintas: MutableList<Quinta>,
            bolsones: MutableList<Bolson>
        ): FamiliaCompleta {
            return FamiliaCompleta(
                familia.id_fp,
                familia.nombre,
                familia.fecha_afiliacion,
                quintas,
                bolsones
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
                    /*|| familia.verduras.map { it.nombre.lowercase() }.contains(query)
                    || ArrayedDate.toString(familia.ronda.fecha_inicio).contains(query)
                    || ArrayedDate.toString(familia.ronda.fecha_fin!!).contains(query)
                    || familia.cantidad.toString().contains(query)*/
                }
            )
            return familiasCompletas
        }
    }
}
