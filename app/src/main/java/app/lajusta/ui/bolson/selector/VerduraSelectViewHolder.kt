package app.lajusta.ui.bolson.selector

import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.databinding.ItemVerduraSelectBinding
import app.lajusta.ui.verdura.Verdura

class VerduraSelectViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private val binding = ItemVerduraSelectBinding.bind(itemView)

    fun bind(
        verduraItem: Verdura,
        selected: Boolean,
        clickListenerIfChecked: (CheckBox) -> Unit,
        clickListenerIfNotChecked: (CheckBox) -> Unit
    ) {
        val cb = binding.cbNombre
        cb.text = verduraItem.nombre
        cb.isChecked = selected
        cb.setOnClickListener {
            if (!cb.isChecked) clickListenerIfChecked(cb)
            else clickListenerIfNotChecked(cb)
        }
    }
}