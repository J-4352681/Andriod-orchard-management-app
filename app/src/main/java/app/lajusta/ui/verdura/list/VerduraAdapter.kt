package app.lajusta.ui.verdura.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.R
import app.lajusta.ui.verdura.Verdura

class VerduraAdapter(
    private val verdurasList: List<Verdura>,
    private val clickListener: (Verdura) -> Unit
) : RecyclerView.Adapter<VerduraViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerduraViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_verdura, parent, false)
        return VerduraViewHolder(view)
    }

    override fun onBindViewHolder(holder: VerduraViewHolder, position: Int) {
        holder.bind(verdurasList[position], clickListener)
    }

    override fun getItemCount(): Int = verdurasList.size
}