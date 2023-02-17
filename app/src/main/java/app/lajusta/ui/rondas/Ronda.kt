package app.lajusta.ui.rondas

import android.os.Parcelable
import app.lajusta.ui.verdura.Verdura
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ronda(
    var id_ronda: Int,
    var fecha_fin: Array<Int>?,
    var fecha_inicio: Array<Int>,
    //var verdura: Verdura  //Tiene una verdura pero esta en null en las api...
): Parcelable