package app.lajusta.ui.generic

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*
import retrofit2.Response

open class BaseFragment : Fragment() {
    private val jobs = mutableListOf<Job>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            runBlocking { jobs.forEach { it.cancel() } }
            findNavController().popBackStack()
        }
    }

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
        jobs += CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = apiEffectiveCall()
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
        jobs += CoroutineScope(Dispatchers.Main).launch {
            try {
                apiEffectiveCall()
                activity!!.runOnUiThread {
                    apiCallUIBlock()
                }
            }
            catch (e: Exception) {
                activity!!.runOnUiThread { shortToast(failureMessage) }
            }
        }
    }
}