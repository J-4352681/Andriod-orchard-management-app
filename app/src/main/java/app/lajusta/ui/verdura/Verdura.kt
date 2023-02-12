package app.lajusta.ui.verdura

data class Verdura(
    val id_verdura: Int,
    val tiempo_cosecha: Int?,
    val mes_siembra: Int?,
    val nombre: String,
    val descripcion: String?
)
