package app.lajusta.ui.bolson

import android.os.Parcelable
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.rondas.Ronda
import app.lajusta.ui.verdura.Verdura
import kotlinx.parcelize.Parcelize

@Parcelize
data class BolsonCompleto(
    val id_bolson: Int,
    var cantidad: Int,
    var familia: Familia,
    var ronda: Ronda,
    var verduras: List<Verdura>
): Parcelable {
    fun toBolson(): Bolson {
        return Bolson(id_bolson,cantidad,familia.id_fp,ronda.id_ronda,verduras)
    }

    companion object {
        fun toBolsonCompleto(bolson: Bolson, familia: Familia, ronda: Ronda): BolsonCompleto {
            return BolsonCompleto(
                bolson.id_bolson,
                bolson.cantidad,
                familia,
                ronda,
                bolson.verduras
            )
        }
    }
}

@Parcelize
data class BolsonesCompletos(
    val bolsones: List<BolsonCompleto>
): ArrayList<BolsonCompleto>(bolsones), Parcelable