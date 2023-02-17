package app.lajusta.ui.visita.modify

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.R
import app.lajusta.ui.parcela.Parcela
import app.lajusta.ui.parcela.ParcelaVisita

class ParcelaVisitaAdapter(
    private var parcelas: List<ParcelaVisita>
) : RecyclerView.Adapter<ParcelaVisitaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParcelaVisitaViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_parcela_visita, parent, false)
        return ParcelaVisitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParcelaVisitaViewHolder, position: Int) {
        holder.bind(parcelas[position]) {
            parcelas = parcelas.filterIndexed { i: Int, _: ParcelaVisita -> i != position }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = parcelas.size

    fun getParcelas(): List<ParcelaVisita> = parcelas
}