package app.lajusta.ui.quinta.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.databinding.ItemQuintaBinding
import app.lajusta.ui.quinta.model.QuintaCompleta

class QuintaViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private val binding = ItemQuintaBinding.bind(itemView)

    fun bind(quintaItem: QuintaCompleta, clickListener: (QuintaCompleta) -> Unit) {

        binding.tvTitle.text = quintaItem.nombre
        binding.tvDescripcion.text = quintaItem.familia.nombre

        itemView.setOnClickListener { clickListener(quintaItem) }
    }
}