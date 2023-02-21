package app.lajusta.ui.verdura

import android.os.Parcelable
import app.lajusta.ui.bolson.BolsonCompleto
import kotlinx.parcelize.Parcelize

@Parcelize
data class Verdura(
    val id_verdura: Int,
    var tiempo_cosecha: Array<Int>?,
    var mes_siembra: Array<Int>?,
    var archImg: String?,
    var nombre: String,
    var descripcion: String?,
): Parcelable

@Parcelize
data class Verduras(
    val verduras: List<Verdura>
): ArrayList<Verdura>(verduras), Parcelable

@Parcelize
data class IdVerduras(
    val verduras: List<Int>
): ArrayList<Int>(verduras), Parcelable
