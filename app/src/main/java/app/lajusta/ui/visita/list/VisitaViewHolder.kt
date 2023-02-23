package app.lajusta.ui.visita.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.lajusta.databinding.ItemVisitaBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.parcela.ParcelaVisita
import app.lajusta.ui.visita.model.VisitaCompleta

class VisitaViewHolder(
    itemView: View
): ViewHolder(itemView) {

    private val binding = ItemVisitaBinding.bind(itemView)

    fun bind(visitaItem: VisitaCompleta, clickListener: (VisitaCompleta) -> Unit) {

        binding.tvFecha.text = "Fecha: " + ArrayedDate.toString(visitaItem.fecha_visita)
        binding.tvQuinta.text = "Quinta: " + visitaItem.quinta.nombre
        binding.tvTecnico.text = "Técnico: " + visitaItem.tecnico.nombre
        var verdurasParcela = visitaItem.parcelas.map { it.verdura.nombre }.toSet().toString()
        verdurasParcela = verdurasParcela.substring(1, verdurasParcela.length - 1)
        if(verdurasParcela.isNotEmpty())
            binding.tvSPACE .text = "Parcelas con: $verdurasParcela"
        else binding.tvSPACE .text = "Parcela sin verduras."

        itemView.setOnClickListener { clickListener(visitaItem) }
    }
}