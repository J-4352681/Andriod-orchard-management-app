package app.lajusta.ui.bolson.modify

import android.content.Context
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import app.lajusta.R
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.verdura.Verdura

class VerduraBolsonAdapter(
    private val verduras: List<Verdura>,
    private val clickListener: (Int) -> Unit
) : Adapter<VerduraBolsonViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerduraBolsonViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_verdura_bolson, parent, false)
        return VerduraBolsonViewHolder(view)
    }

    override fun onBindViewHolder(holder: VerduraBolsonViewHolder, position: Int) {
        holder.bind(verduras[position], clickListener, position)
    }

    override fun getItemCount(): Int = verduras.size
}