package app.lajusta.ui.bolson.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.databinding.ItemBolsonBinding
import app.lajusta.ui.bolson.Bolson

class BolsonesViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemBolsonBinding.bind(view)

    fun render(bolsonItem: Bolson) {
        binding.tvId.text = bolsonItem.setup
        binding.tvFamilia.text = bolsonItem.type
        binding.tvRonda.text = bolsonItem.punchline
        binding.tvTitle.text = bolsonItem.id.toString()
    }
}