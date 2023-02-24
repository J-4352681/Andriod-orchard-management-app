package app.lajusta.ui.visita.edition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.R
import app.lajusta.ui.parcela.ParcelaVisita

class ParcelaVisitaAdapter(
    private val parcelas: MutableList<ParcelaVisita>
) : RecyclerView.Adapter<ParcelaVisitaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParcelaVisitaViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_parcela_visita, parent, false)
        return ParcelaVisitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParcelaVisitaViewHolder, position: Int) {
        holder.bind(parcelas[position]) {
            parcelas -= it
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = parcelas.size

    fun getParcelas(): List<ParcelaVisita> = parcelas
}