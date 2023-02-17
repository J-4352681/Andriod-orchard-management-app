package app.lajusta.ui.bolson.model

import android.os.Parcelable
import app.lajusta.ui.bolson.Bolson
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
    fun toBolson():Bolson {
        return Bolson(id_bolson,cantidad,familia.id_fp,ronda.id_ronda,verduras)
    }
}