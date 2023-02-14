package app.lajusta.ui.bolson.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.R
import app.lajusta.ui.bolson.Bolson

class BolsonAdapter(
    private val bolsonesList: List<Bolson>,
    private val clickListener: (Bolson) -> Unit
) : RecyclerView.Adapter<BolsonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BolsonViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_bolson, parent, false)
        return BolsonViewHolder(view)
    }

    override fun onBindViewHolder(holder: BolsonViewHolder, position: Int) {
        holder.bind(bolsonesList[position], clickListener)
    }

    override fun getItemCount(): Int = bolsonesList.size
}