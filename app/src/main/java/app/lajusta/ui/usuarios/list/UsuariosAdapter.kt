package app.lajusta.ui.usuarios.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.R
import app.lajusta.ui.usuarios.Usuario

class UsuariosAdapter(
    private val usuariosList: List<Usuario>,
    private val clickListener: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuariosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuariosViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return UsuariosViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuariosViewHolder, position: Int) {
        holder.bind(usuariosList[position], clickListener)
    }

    override fun getItemCount(): Int = usuariosList.size
}