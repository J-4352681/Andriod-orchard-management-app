package app.lajusta.ui.quinta.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.R
import app.lajusta.ui.quinta.model.QuintaCompleta

class QuintaAdapter(
    private val quintasList: List<QuintaCompleta>,
    private val clickListener: (QuintaCompleta) -> Unit
) : RecyclerView.Adapter<QuintaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuintaViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_quinta, parent, false)
        return QuintaViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuintaViewHolder, position: Int) {
        holder.bind(quintasList[position], clickListener)
    }

    override fun getItemCount(): Int = quintasList.size
}