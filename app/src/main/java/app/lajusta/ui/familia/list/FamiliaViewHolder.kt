package app.lajusta.ui.familia.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.lajusta.databinding.ItemFamiliaBinding
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.FamiliaCompleta
import app.lajusta.ui.generic.ArrayedDate

class FamiliaViewHolder(
    itemView: View
): ViewHolder(itemView) {

    private val binding = ItemFamiliaBinding.bind(itemView)

    fun bind(familiaItem: FamiliaCompleta, clickListener: (FamiliaCompleta) -> Unit) {
        binding.tvTitle.text = familiaItem.nombre
        val quintas = familiaItem.quintas.map { it.nombre }.toString()
        binding.tvDescripcion.text = (
            "Fecha de afiliación: " + ArrayedDate.toString(familiaItem.fecha_afiliacion)
            + "\nQuintas: " + if (familiaItem.quintas.isNotEmpty())
                quintas.subSequence(1, quintas.length-1)
                else "Familias sin quintas."
        )

        itemView.setOnClickListener { clickListener(familiaItem) }
    }
}