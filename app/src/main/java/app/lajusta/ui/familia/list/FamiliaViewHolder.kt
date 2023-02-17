package app.lajusta.ui.familia.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.lajusta.databinding.ItemFamiliaBinding
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.generic.ArrayedDate

class FamiliaViewHolder(
    itemView: View
): ViewHolder(itemView) {

    private val binding = ItemFamiliaBinding.bind(itemView)

    fun bind(familiaItem: Familia, clickListener: (Familia) -> Unit) {
        binding.tvTitle.text = familiaItem.nombre
        binding.tvDescripcion.text =
            "Fecha de afiliación: " + ArrayedDate.toString(familiaItem.fecha_afiliacion)

        itemView.setOnClickListener { clickListener(familiaItem) }
    }
}