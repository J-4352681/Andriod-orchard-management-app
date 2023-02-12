package app.lajusta.api

import android.content.Context
import android.widget.Toast
import retrofit2.Response
import java.io.IOException

abstract class SafeApiRequest {
    suspend fun <T: Any> apiRequest(call: suspend() -> Response<T>): T {
        val response = call.invoke()
        if(response.isSuccessful) {
            return response.body()!!
        } else {
            //Toast.makeText(context, "Error al obtener los datos.", Toast.LENGTH_SHORT).show()
            throw ApiException(response.code().toString())
        }
    }
}

class ApiException(message: String): IOException(message)