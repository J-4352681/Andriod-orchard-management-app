package app.lajusta.ui.generic

import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import retrofit2.Response

open class BaseFragment : Fragment() {
    protected fun shortToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    protected fun longToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    protected fun returnSimpleApiCall(
        apiEffectiveCall: suspend () -> Response<out Any>,
        failureMessage: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiEffectiveCall()
                println(response.code())
                if(!response.isSuccessful) throw Exception(response.code().toString())
            }
            catch(e: Exception) { activity!!.runOnUiThread {
                shortToast(failureMessage)
            } } finally { activity!!.runOnUiThread { activity!!.onBackPressed() } }
        }
    }

    protected fun apiCall(
        apiEffectiveCall: suspend () -> Unit,
        apiCallUIBlock: () -> Unit,
        failureMessage: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                apiEffectiveCall()
                GlobalScope.launch(Dispatchers.Main) {
                    apiCallUIBlock()
                }
            }
            catch (e: Exception) {
                activity!!.runOnUiThread { shortToast(failureMessage) }
            }
        }
    }
}