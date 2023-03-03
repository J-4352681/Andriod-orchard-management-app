package app.lajusta.ui.verdura

import android.os.Parcelable
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.usuarios.Usuario
import kotlinx.parcelize.Parcelize

@Parcelize
data class Verdura(
    val id_verdura: Int,
    var tiempo_cosecha: List<Int>?,
    var mes_siembra: List<Int>?,
    var archImg: String?,
    var nombre: String,
    var descripcion: String?,
): Parcelable, Comparable<Verdura> {

    override fun compareTo(other: Verdura): Int {
        return nombre.compareTo(other.nombre)
    }
    override fun toString(): String = nombre

    companion object {
        fun filter(
            verduras: MutableList<Verdura>,
            verdurasOriginales: MutableList<Verdura>,
            query: String?
        ): MutableList<Verdura> {
            verduras.clear()
            if(query.isNullOrEmpty()) verduras.addAll(verdurasOriginales)
            else verduras.addAll(
                verdurasOriginales.filter { verdura ->
                    verdura.nombre.contains(query)
                    || verdura.descripcion!!.contains(query)
                    || ArrayedDate.toString(verdura.tiempo_cosecha!!).contains(query)
                    || ArrayedDate.toString(verdura.mes_siembra!!).contains(query)
                }
            )
            return verduras
        }
    }
}

@Parcelize
data class Verduras(
    val verduras: List<Verdura>
): ArrayList<Verdura>(verduras), Parcelable

@Parcelize
data class IdVerduras(
    val verduras: List<Int>
): ArrayList<Int>(verduras), Parcelable
