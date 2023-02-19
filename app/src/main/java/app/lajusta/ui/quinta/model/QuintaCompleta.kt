package app.lajusta.ui.quinta.model

import android.os.Parcelable
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.quinta.Quinta
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuintaCompleta(
    val id_quinta: Int,
    var nombre: String,
    var direccion: String?,
    var geoImg: String?,
    var familia: Familia,
): Parcelable {
    fun toQuinta(): Quinta = Quinta(id_quinta, nombre, direccion, geoImg, familia.id_fp)

    companion object {
        fun toQuintaCompleta(quinta: Quinta, familia: Familia): QuintaCompleta =
            QuintaCompleta(quinta.id_quinta, quinta.nombre,
                quinta.direccion, quinta.geoImg, familia
            )

        fun filter(
            quintasCompletas: MutableList<QuintaCompleta>,
            quintasCompletasOriginales: MutableList<QuintaCompleta>,
            query: String?
        ): MutableList<QuintaCompleta> {
            quintasCompletas.clear()
            if(query.isNullOrEmpty()) quintasCompletas.addAll(quintasCompletasOriginales)
            else quintasCompletas.addAll(
                quintasCompletasOriginales.filter { quinta ->
                    quinta.nombre.lowercase().contains(query)
                    || quinta.familia.nombre.lowercase().contains(query)
                }
            )
            return quintasCompletas
        }
    }
}

@Parcelize
data class QuintasCompletas(
    val quintas: List<QuintaCompleta>
): ArrayList<QuintaCompleta>(quintas), Parcelable
