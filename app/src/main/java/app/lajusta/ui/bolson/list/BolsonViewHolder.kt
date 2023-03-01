package app.lajusta.ui.bolson.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.lajusta.databinding.ItemBolsonBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.BolsonCompleto
import app.lajusta.ui.generic.ArrayedDate

class BolsonViewHolder(
    itemView: View
): ViewHolder(itemView) {

    private val binding = ItemBolsonBinding.bind(itemView)

    fun bind(bolsonItem: BolsonCompleto, clickListener: (Bolson) -> Unit) {
        binding.tvTitle.text =
            "Bolsón de familia: ${bolsonItem.familia} para ronda ${bolsonItem.ronda}"
        binding.tvDescripcion.text = "Cantidad: ${bolsonItem.cantidad}"

        /* if(bolsonItem.ronda) {

        } */

        itemView.setOnClickListener { clickListener(bolsonItem.toBolson()) }
    }
}