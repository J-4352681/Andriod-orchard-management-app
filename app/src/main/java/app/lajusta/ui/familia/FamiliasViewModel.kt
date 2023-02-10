package app.lajusta.ui.familia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FamiliasViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is familias Fragment"
    }
    val text: LiveData<String> = _text
}