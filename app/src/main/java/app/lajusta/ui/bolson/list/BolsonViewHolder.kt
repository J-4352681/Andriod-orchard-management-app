package app.lajusta.ui.bolson.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.databinding.ItemBolsonBinding
import app.lajusta.ui.bolson.Bolson

class BolsonViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private val binding = ItemBolsonBinding.bind(itemView)

    fun bind(bolsonItem: Bolson, clickListener: (Bolson) -> Unit) {
        binding.tvFamilia.text = bolsonItem.idFp.toString()
        binding.tvRonda.text = bolsonItem.idRonda.toString()
        binding.tvTitle.text = "Bolsón: " + bolsonItem.cantidad.toString()

        itemView.setOnClickListener { clickListener(bolsonItem) }
    }
}