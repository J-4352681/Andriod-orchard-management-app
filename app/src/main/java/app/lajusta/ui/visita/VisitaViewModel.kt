package app.lajusta.ui.visita

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VisitaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is visitas Fragment"
    }
    val text: LiveData<String> = _text
}