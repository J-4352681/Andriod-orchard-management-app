package app.lajusta.ui.verdura

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Verdura(
    val id_verdura: Int,
    val tiempo_cosecha: String,
    val mes_siembra: String,
    val archImg: String,
    val nombre: String,
    val descripcion: String,
): Parcelable
