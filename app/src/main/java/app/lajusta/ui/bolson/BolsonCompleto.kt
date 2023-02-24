package app.lajusta.ui.bolson

import android.os.Parcelable
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.ronda.Ronda
import app.lajusta.ui.verdura.Verdura
import kotlinx.parcelize.Parcelize

@Parcelize
data class BolsonCompleto(
    val id_bolson: Int,
    var cantidad: Int,
    var familia: Familia,
    var ronda: Ronda,
    val verduras: MutableList<Verdura>
): Parcelable {
    fun toBolson(): Bolson {
        return Bolson(id_bolson,cantidad,familia.id_fp,ronda.id_ronda,verduras)
    }

    companion object {
        fun toBolsonCompleto(bolson: Bolson, familia: Familia, ronda: Ronda): BolsonCompleto {
            return BolsonCompleto(
                bolson.id_bolson, bolson.cantidad, familia, ronda, bolson.verduras
            )
        }

        fun filter(
            bolsonesCompletos: MutableList<BolsonCompleto>,
            bolsonesCompletosOriginal: MutableList<BolsonCompleto>,
            query: String?
        ): MutableList<BolsonCompleto> {
            bolsonesCompletos.clear()
            if(query.isNullOrEmpty()) bolsonesCompletos.addAll(bolsonesCompletosOriginal)
            else bolsonesCompletos.addAll(
                bolsonesCompletosOriginal.filter { bolson ->
                    bolson.familia.nombre.lowercase().contains(query)
                    || bolson.verduras.any {
                        it.nombre.lowercase().contains(query)
                    }
                    || ArrayedDate.toString(bolson.ronda.fecha_inicio).contains(query)
                    || ArrayedDate.toString(bolson.ronda.fecha_fin!!).contains(query)
                    || bolson.cantidad.toString().contains(query)
                }
            )
            return bolsonesCompletos
        }
    }
}

@Parcelize
data class BolsonesCompletos(
    val bolsones: List<BolsonCompleto>
): ArrayList<BolsonCompleto>(bolsones), Parcelable