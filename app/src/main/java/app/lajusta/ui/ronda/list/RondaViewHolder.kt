package app.lajusta.ui.ronda.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.databinding.ItemRondaBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.ronda.Ronda

class RondaViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemRondaBinding.bind(itemView)

    fun bind(rondaItem: Ronda, clickListener: (Ronda) -> Unit) {
        binding.tvTitle.text = "Ronda $rondaItem"

        if(!rondaItem.isActive()) binding.tvTitle.isEnabled = false

        itemView.setOnClickListener { clickListener(rondaItem) }
    }
}