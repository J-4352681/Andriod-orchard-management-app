package app.lajusta.ui.bolson.edition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import app.lajusta.R
import app.lajusta.ui.verdura.Verdura

class VerduraBolsonAdapter(
    private val verduras: MutableList<Verdura>,
    private var active: Boolean
) : Adapter<VerduraBolsonViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerduraBolsonViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_verdura_bolson, parent, false)
        return VerduraBolsonViewHolder(view)
    }

    override fun onBindViewHolder(holder: VerduraBolsonViewHolder, position: Int) {
        holder.bind(verduras[position], active) {
            verduras -= it
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = verduras.size

    fun setActive(newState: Boolean) {
        active = newState
        notifyDataSetChanged()
    }
}