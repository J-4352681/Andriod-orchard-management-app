package app.lajusta.ui.familia.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import app.lajusta.R
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.FamiliaCompleta

class FamiliaAdapter(
    private val familiasList: List<FamiliaCompleta>,
    private val clickListener: (FamiliaCompleta) -> Unit
) : Adapter<FamiliaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamiliaViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_familia, parent, false)
        return FamiliaViewHolder(view)
    }

    override fun onBindViewHolder(holder: FamiliaViewHolder, position: Int) {
        holder.bind(familiasList[position], clickListener)
    }

    override fun getItemCount(): Int = familiasList.size
}