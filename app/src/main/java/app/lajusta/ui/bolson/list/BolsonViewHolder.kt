package app.lajusta.ui.bolson.list

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.lajusta.R
import app.lajusta.databinding.ItemBolsonBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.BolsonCompleto
import app.lajusta.ui.generic.ArrayedDate
import kotlin.coroutines.coroutineContext

class BolsonViewHolder(
    itemView: View
): ViewHolder(itemView) {

    private val binding = ItemBolsonBinding.bind(itemView)

    fun bind(bolsonItem: BolsonCompleto, clickListener: (Bolson) -> Unit) {
        binding.tvTitle.text =
            "Bolsón de familia: ${bolsonItem.familia}\npara ronda ${bolsonItem.ronda}"
        binding.tvDescripcion.text = "Cantidad: ${bolsonItem.cantidad}"

        if(!bolsonItem.ronda.isActive()) {
            binding.tvTitle.isEnabled = false
            binding.tvDescripcion.isEnabled = false
        }

        itemView.setOnClickListener { clickListener(bolsonItem.toBolson()) }
    }
}