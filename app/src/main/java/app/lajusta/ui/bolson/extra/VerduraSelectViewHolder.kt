package app.lajusta.ui.bolson.extra

import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.databinding.ItemVerduraSelectBinding
import app.lajusta.ui.verdura.Verdura

class VerduraSelectViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private val binding = ItemVerduraSelectBinding.bind(itemView)

    fun bind(verduraItem: Verdura, selected: Boolean, clickListener: (CheckBox) -> Unit) {
        binding.cbNombre.text = verduraItem.nombre
        binding.cbNombre.isChecked = selected
        binding.cbNombre.setOnClickListener { clickListener(binding.cbNombre) }
    }
}