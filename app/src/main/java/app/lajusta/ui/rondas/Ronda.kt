package app.lajusta.ui.rondas

import android.os.Parcelable
import app.lajusta.ui.verdura.Verdura
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ronda(
    val id_ronda: Int,
    var fecha_fin: List<Int>?,
    var fecha_inicio: List<Int>,
    //var verdura: Verdura  //Tiene una verdura pero esta en null en las api...
): Parcelable