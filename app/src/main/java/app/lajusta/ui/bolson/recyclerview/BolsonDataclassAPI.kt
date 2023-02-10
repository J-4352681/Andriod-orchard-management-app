package app.lajusta.ui.bolson.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.R
import app.lajusta.databinding.ItemBolsonBinding
import app.lajusta.ui.api.SafeAPIRequest
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class BolsonDataclass(
    val id_bolson: Int,
    val cantidad: Int,
    val id_fp: Int,
    val idRonda: Int,
)


interface BolsonesAPIService {
    @GET("bolson")
    suspend fun getBolsones(): Response<BolsonDataclass>

    companion object {
        operator fun invoke(): BolsonesAPIService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(R.string.url_base.toString())
                .build()
                .create(BolsonesAPIService::class.java)
        }
    }
}


class BolsonesReposiory(
    private val api: BolsonesAPIService
): SafeAPIRequest() {
    suspend fun getBolsones() = apiRequest { api.getBolsones() }
}


class BolsonesViewHolder(view: View): RecyclerView.ViewHolder(View) {

    val binding = ItemBolsonBinding.bind(view)

    fun render(bolsonItem: BolsonDataclass) {
        binding.tvId.text = bolsonItem.id_bolson.toString()
        binding.tvFamilia.text = bolsonItem.id_fp.toString()
        binding.tvRonda.text = bolsonItem.idRonda.toString()
        binding.tvTitle.text = "Bolson"
    }
}


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
