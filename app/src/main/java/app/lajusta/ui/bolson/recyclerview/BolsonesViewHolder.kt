package app.lajusta.ui.bolson.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.databinding.ItemBolsonBinding
import app.lajusta.ui.bolson.BolsonDataclass

class BolsonesViewHolder(view: View): RecyclerView.ViewHolder(View) {

    val binding = ItemBolsonBinding.bind(view)

    fun render(bolsonItem: BolsonDataclass) {
        binding.tvId.text = bolsonItem.id_bolson.toString()
        binding.tvFamilia.text = bolsonItem.id_fp.toString()
        binding.tvRonda.text = bolsonItem.idRonda.toString()
        binding.tvTitle.text = "Bolson"
    }
}