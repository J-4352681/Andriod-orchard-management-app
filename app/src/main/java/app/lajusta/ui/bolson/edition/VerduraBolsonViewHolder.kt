package app.lajusta.ui.bolson.edition

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.lajusta.databinding.ItemVerduraBolsonBinding
import app.lajusta.ui.verdura.Verdura

class VerduraBolsonViewHolder(
    itemView: View
) : ViewHolder(itemView) {

    private val binding = ItemVerduraBolsonBinding.bind(itemView)

    fun bind(verduraItem: Verdura, active: Boolean, clickListener: (Verdura) -> Unit) {
        binding.tvTitle.text = verduraItem.nombre
        binding.bEliminar.setOnClickListener { clickListener(verduraItem) }
        if(!active) binding.bEliminar.isEnabled = false
    }
}