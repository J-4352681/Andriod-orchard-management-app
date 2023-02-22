package app.lajusta.ui.login

import app.lajusta.data.model.UserType

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
val displayName: String?,
val userType: Int?,
    val id: Int?,
    val token: String?,
    //... other data fields that may be accessible to the UI
)