package app.lajusta.ui.bolson.list

import android.R
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.lajusta.databinding.ItemBolsonBinding
import app.lajusta.ui.bolson.Bolson
import app.lajusta.ui.bolson.model.BolsonCompleto
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.familia.api.FamiliaApi
import app.lajusta.ui.generic.ArrayedDate
import app.lajusta.ui.quinta.API.QuintaApi
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.rondas.Ronda
import app.lajusta.ui.rondas.api.RondaApi
import kotlinx.coroutines.*

class BolsonViewHolder(
    itemView: View
): ViewHolder(itemView) {

    private val binding = ItemBolsonBinding.bind(itemView)

    fun bind(bolsonItem: BolsonCompleto, clickListener: (BolsonCompleto) -> Unit) {

        binding.tvTitle.text = "Bolsón: " + bolsonItem.id_bolson.toString()
        binding.tvFamilia.text = "Familia: " + bolsonItem.familia.nombre
        binding.tvRonda.text = "Ronda del: " + ArrayedDate.toString(bolsonItem.ronda.fecha_inicio)
        binding.tvCantidad.text = "Cantidad: " + bolsonItem.cantidad.toString()

        itemView.setOnClickListener { clickListener(bolsonItem) }
    }

}