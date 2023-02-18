package app.lajusta.ui.generic

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
                if(!response.isSuccessful) throw Exception(response.code().toString())
            }
            catch(e: Exception) { activity!!.runOnUiThread {
                shortToast(failureMessage)
                Log.e("RETROFIT ERROR", e.message!!) } }
            finally { activity!!.runOnUiThread { activity!!.onBackPressed() } }
        }
    }

    protected fun apiCall(
        apiEffectiveCall: suspend () -> Unit,
        failureMessage: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {apiEffectiveCall() }
            catch (e: Exception) {
                activity!!.runOnUiThread { shortToast(failureMessage) }
            }
        }
    }
}