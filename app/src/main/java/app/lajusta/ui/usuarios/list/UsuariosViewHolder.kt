package app.lajusta.ui.usuarios.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.lajusta.databinding.ItemUsuarioBinding
import app.lajusta.ui.usuarios.Usuario

class UsuariosViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private val binding = ItemUsuarioBinding.bind(itemView)

    fun bind(usuarioItem: Usuario, clickListener: (Usuario) -> Unit) {
        binding.tvTitle.text = usuarioItem.nombre + " " + usuarioItem.apellido
        binding.tvRoles.text = Usuario.rolNumberToName(usuarioItem.roles)

        itemView.setOnClickListener { clickListener(usuarioItem) }
    }
}