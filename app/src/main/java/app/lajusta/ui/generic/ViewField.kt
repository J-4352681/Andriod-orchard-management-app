package app.lajusta.ui.generic

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ViewField<T> (
    var data: @RawValue T? = null,
    val _blocked: Boolean = false
): Parcelable {
    fun isNull() = data == null
}