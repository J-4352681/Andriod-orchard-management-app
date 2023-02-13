package app.lajusta.ui.login

import app.lajusta.data.model.UserType

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
val displayName: String,
val userType: UserType,
    val id: String,
    //... other data fields that may be accessible to the UI
)