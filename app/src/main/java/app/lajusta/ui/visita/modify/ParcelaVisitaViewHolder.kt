package app.lajusta.ui.visita.modify

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.databinding.ItemParcelaVisitaBinding
import app.lajusta.ui.parcela.Parcela

class ParcelaVisitaViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemParcelaVisitaBinding.bind(itemView)

    fun bind(parcelaItem: Parcela, clickListener: () -> Unit) {
        binding.tvVerdura.text = parcelaItem.verdura.nombre + " | Surcos: " + parcelaItem.cantidad_surcos.toString()
        if(parcelaItem.cosecha) binding.cbCosecha.isChecked = true
        if(parcelaItem.cubierta) binding.cbCubierto.isChecked = true

        binding.bEliminar.setOnClickListener { clickListener() }
    }
}