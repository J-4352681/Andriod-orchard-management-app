package app.lajusta.ui.bolson.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.R
import app.lajusta.ui.bolson.BolsonDataclass

class BolsonesAdapter(private val bolsonesList: List<BolsonDataclass>) : RecyclerView.Adapter<BolsonesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BolsonesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BolsonesViewHolder(layoutInflater.inflate(R.layout.item_bolson, parent, false))
    }

    override fun onBindViewHolder(holder: BolsonesViewHolder, position: Int) {
        val item = bolsonesList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = bolsonesList.size
}