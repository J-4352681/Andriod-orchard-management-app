package app.lajusta.ui.bolson.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.lajusta.databinding.ItemBolsonBinding
import app.lajusta.ui.bolson.BolsonCompleto
import app.lajusta.ui.generic.ArrayedDate

class BolsonViewHolder(
    itemView: View
): ViewHolder(itemView) {

    private val binding = ItemBolsonBinding.bind(itemView)

    fun bind(bolsonItem: BolsonCompleto, clickListener: (BolsonCompleto) -> Unit) {

        binding.tvTitle.text = "Bolsón: " + bolsonItem.id_bolson.toString()
        binding.tvFamilia.text = "Familia: " + bolsonItem.familia.nombre
        binding.tvRonda.text = "Ronda: " + ArrayedDate.toString(
            bolsonItem.ronda.fecha_inicio.toMutableList()
        )
        binding.tvCantidad.text = "Cantidad: " + bolsonItem.cantidad.toString()

        itemView.setOnClickListener { clickListener(bolsonItem) }
    }
}