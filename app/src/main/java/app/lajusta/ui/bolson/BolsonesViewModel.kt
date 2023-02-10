package app.lajusta.ui.bolson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.lajusta.ui.bolson.recyclerview.BolsonDataclass
import app.lajusta.ui.bolson.recyclerview.BolsonesReposiory

class BolsonesViewModel(
    private val reposiory: BolsonesReposiory
) : ViewModel() {

    private val _bolsones = MutableLiveData<List<BolsonDataclass>>()
    val bolsones : LiveData<List<BolsonDataclass>>
        get() = _bolsones

    suspend fun getBolsones() {
        val bolsones = reposiory.getBolsones()
        _bolsones.value = bolsones
    }
}