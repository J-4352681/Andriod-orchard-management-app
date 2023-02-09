package app.lajusta.ui.bolson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BolsonViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Bolsón Fragment"
    }
    val text: LiveData<String> = _text
}