package app.lajusta.ui.verdura

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Verdura(
    var id_verdura: Int,
    var tiempo_cosecha: Array<Int>?,
    var mes_siembra: Array<Int>?,
    var archImg: String?,
    var nombre: String,
    var descripcion: String?,
): Parcelable
