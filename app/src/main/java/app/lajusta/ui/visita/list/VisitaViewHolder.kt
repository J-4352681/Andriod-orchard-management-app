package app.lajusta.ui.visita.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.lajusta.databinding.ItemVisitaBinding
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.visita.Visita
import app.lajusta.ui.visita.model.VisitaCompleta

class VisitaViewHolder(
    itemView: View
): ViewHolder(itemView) {

    private val binding = ItemVisitaBinding.bind(itemView)

    fun bind(visitaItem: VisitaCompleta, clickListener: (VisitaCompleta) -> Unit) {

        binding.tvFecha.text = "Fecha: " + ArrayedDate.toString(visitaItem.fecha_visita)
        binding.tvQuinta.text = "Quinta: " + visitaItem.quinta.nombre
        binding.tvTecnico.text = "Técnico: " + visitaItem.tecnico.nombre
        //binding.tvSPACE .text = "Parcelas: " + visitaItem.parcelas.map { it.id_parcela }.toString() //El id de las psrcelas es medio inutil, capaz la cantidad, o la verdura!
        binding.tvSPACE .text = "Parcelas con: " + visitaItem.parcelas.map { it.verdura.nombre }.toString()

        itemView.setOnClickListener { clickListener(visitaItem) }
    }
}