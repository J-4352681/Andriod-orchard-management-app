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
        binding.tvTitle.text = "Bolsón: " + bolsonItem.id_bolson.toString()
        binding.tvFamilia.text = "Familia: " + bolsonItem.idFp.toString()
        binding.tvRonda.text = "Ronda: " + bolsonItem.idRonda.toString()
        binding.tvCantidad.text = "Cantidad: " + bolsonItem.cantidad.toString()

        itemView.setOnClickListener { clickListener(bolsonItem) }
    }
}