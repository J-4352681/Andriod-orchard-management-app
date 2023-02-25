package app.lajusta.data.Preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferenceHelper {

    val LOGGED_IN = "LOGGED_IN"
    val USER_ID = "USER_ID"
    val USER_NAME = "USERNAME"
    var USER_TYPE = "USER_TYPE"
    var TOKEN = "TOKEN"

    fun getPreferences(context: Context): SharedPreferences =
        getPreferences(context)

    fun customPreference(context: Context, name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.loggedIn
        get() = getBoolean(LOGGED_IN, false)
        set(value) {
            editMe {
                it.putBoolean(LOGGED_IN, value)
            }
        }

    var SharedPreferences.userId
        get() = getInt(USER_ID, 0)
        set(value) {
            editMe {
                it.putInt(USER_ID, value)
            }
        }

    var SharedPreferences.username
        get() = getString(USER_NAME, "")
        set(value) {
            editMe {
                it.putString(USER_NAME, value)
            }
        }

    var SharedPreferences.userType
        get() = getInt(USER_TYPE, 0)
        set(value) {
            editMe {
                it.putInt(USER_TYPE, value)
            }
        }

    var SharedPreferences.token
        get() = getString(TOKEN, "")
        set(value) {
            editMe {
                it.putString(TOKEN, value)
            }
        }

    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
                it.clear()
            }
        }

}