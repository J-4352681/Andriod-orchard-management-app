package app.lajusta.ui.verdura.extra

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.databinding.ItemVerduraBinding
import app.lajusta.databinding.ItemVerduraSelectBinding
import app.lajusta.ui.verdura.Verdura

class VerduraSelectViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private val binding = ItemVerduraSelectBinding.bind(itemView)

    fun bind(verduraItem: Verdura, clickListener: (Verdura) -> Unit) {
        binding.cbNombre.text = verduraItem.nombre
        itemView.setOnClickListener { clickListener(verduraItem) }
    }
}