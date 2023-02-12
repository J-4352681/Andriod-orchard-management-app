package app.lajusta.ui.bolson

/* import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.lajusta.ui.bolson.api.BolsonesAPIService
import app.lajusta.ui.bolson.api.BolsonesReposiory

class BolsonesViewModel() : ViewModel() {

    private val repository = BolsonesReposiory(BolsonesAPIService())
    private val _bolsones = MutableLiveData<List<Bolson>>()
    val bolsones : LiveData<List<Bolson>>
        get() = _bolsones

    suspend fun getBolsones() {
        _bolsones.value = repository.getBolsones()
    }

    private fun filter(query:String) {
        // A completar
    }
} */