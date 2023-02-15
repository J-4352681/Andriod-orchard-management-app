package app.lajusta.ui.visita.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.lajusta.databinding.ItemVisitaBinding
import app.lajusta.ui.quinta.API.QuintaApi
import app.lajusta.ui.visita.Visita
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VisitaViewHolder(
    itemView: View
): ViewHolder(itemView) {

    private val binding = ItemVisitaBinding.bind(itemView)

    fun bind(visitaItem: Visita, clickListener: (Visita) -> Unit) {
        /* CoroutineScope(Dispatchers.IO).launch {
            try {
                val nombreQuinta = QuintaApi().getQuinta(visitaItem.id_quinta)
            } catch (e: Exception) {

            }
        } */
        binding.tvFecha.text = "Fecha: " + visitaItem.fecha_visita.toString()
        binding.tvQuinta.text = "Quinta: " + visitaItem.id_quinta.toString()
        binding.tvTecnico.text = "Técnico: " + visitaItem.id_tecnico.toString()
        binding.tvSPACE .text = "Parcelas: " + visitaItem.parcelas.map { it.id_parcela }.toString()

        itemView.setOnClickListener { clickListener(visitaItem) }
    }
}