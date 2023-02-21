package app.lajusta.ui.ronda.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.R
import app.lajusta.ui.ronda.Ronda

class RondaAdapter(
    private val rondasList: List<Ronda>,
    private val clickListener: (Ronda) -> Unit
) : RecyclerView.Adapter<RondaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RondaViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_ronda, parent, false)
        return RondaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RondaViewHolder, position: Int) {
        holder.bind(rondasList[position], clickListener)
    }

    override fun getItemCount(): Int = rondasList.size
}