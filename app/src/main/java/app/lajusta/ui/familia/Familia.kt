package app.lajusta.ui.familia

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Familia(
    var id_fp: Int,
    var nombre: String,
    var fecha_afiliacion: Array<Int>?,
): Parcelable