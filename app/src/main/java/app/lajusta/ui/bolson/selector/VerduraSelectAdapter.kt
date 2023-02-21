package app.lajusta.ui.bolson.selector

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView.Adapter
import app.lajusta.R
import app.lajusta.ui.verdura.Verdura

class VerduraSelectAdapter(
    private val verdurasList: List<Verdura>,
    private val preseleccionadas: List<Int>,
    private val clickListenerIfChecked: (CheckBox) -> Unit,
    private val clickListenerIfNotChecked: (CheckBox) -> Unit
) : Adapter<VerduraSelectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerduraSelectViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_verdura_select, parent, false)
        return VerduraSelectViewHolder(view)
    }

    override fun onBindViewHolder(holder: VerduraSelectViewHolder, position: Int) {
        val verdura = verdurasList[position]
        holder.bind(
            verdura,
            preseleccionadas.contains(verdura.id_verdura),
            clickListenerIfChecked,
            clickListenerIfNotChecked
        )
    }

    override fun getItemCount(): Int = verdurasList.size
}