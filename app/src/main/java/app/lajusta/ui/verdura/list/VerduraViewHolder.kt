package app.lajusta.ui.verdura.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.databinding.ItemVerduraBinding
import app.lajusta.ui.verdura.Verdura

class VerduraViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private val binding = ItemVerduraBinding.bind(itemView)

    fun bind(verduraItem: Verdura, clickListener: (Verdura) -> Unit) {
        binding.tvTitle.text = verduraItem.nombre
        binding.tvDescripcion.text = verduraItem.descripcion

        itemView.setOnClickListener { clickListener(verduraItem) }
    }
}