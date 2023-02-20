package app.lajusta.ui.bolson.extra

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.R
import app.lajusta.ui.verdura.Verdura
import app.lajusta.ui.verdura.extra.VerduraSelectViewHolder

class VerduraSelectAdapter(
    private val bolsonesList: List<Verdura>,
    private val clickListener: (Verdura) -> Unit
) : RecyclerView.Adapter<VerduraSelectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerduraSelectViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_bolson, parent, false)
        return VerduraSelectViewHolder(view)
    }

    override fun onBindViewHolder(holder: VerduraSelectViewHolder, position: Int) {
        holder.bind(bolsonesList[position], clickListener)
    }

    override fun getItemCount(): Int = bolsonesList.size
}