package app.lajusta.ui.bolson.modify

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.lajusta.databinding.ItemVerduraBolsonBinding
import app.lajusta.ui.verdura.Verdura

class VerduraBolsonViewHolder(
    itemView: View
) : ViewHolder(itemView) {

    private val binding = ItemVerduraBolsonBinding.bind(itemView)

    fun bind(verduraItem: Verdura) {
        binding.tvTitle.text = verduraItem.nombre
    }
}