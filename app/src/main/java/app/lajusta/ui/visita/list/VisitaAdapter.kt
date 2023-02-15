package app.lajusta.ui.visita.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.R
import app.lajusta.ui.visita.Visita

class VisitaAdapter(
    private val visitasList: List<Visita>,
    private val clickListener: (Visita) -> Unit
) : RecyclerView.Adapter<VisitaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitaViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_visita, parent, false)
        return VisitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VisitaViewHolder, position: Int) {
        holder.bind(visitasList[position], clickListener)
    }

    override fun getItemCount(): Int = visitasList.size
}