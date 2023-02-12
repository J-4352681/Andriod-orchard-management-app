package app.lajusta.ui.bolson.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.databinding.ItemBolsonBinding
import app.lajusta.ui.bolson.Bolson

class BolsonesViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemBolsonBinding.bind(view)

    fun render(bolsonItem: Bolson) {
        binding.tvId.text = bolsonItem.id_bolson.toString()
        binding.tvFamilia.text = bolsonItem.idFp.toString()
        binding.tvRonda.text = bolsonItem.idRonda.toString()
        binding.tvTitle.text = bolsonItem.cantidad.toString()
    }
}