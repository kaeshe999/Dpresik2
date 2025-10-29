package com.example.dpresik2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dpresik2.databinding.ActivityAccountInfoBinding // Importa el binding de la nueva pantalla
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountInfoBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth
        auth = Firebase.auth
        val user = auth.currentUser

        // Rellenar los campos de texto con la info del usuario
        if (user != null) {
            binding.nameTextView.text = user.displayName ?: "No disponible"
            binding.emailTextView.text = user.email ?: "No disponible"
            binding.uidTextView.text = user.uid
        }

        // Configurar el botón de "Volver"
        binding.backButtonAccount.setOnClickListener {
            // 'finish()' cierra esta actividad y regresa a la anterior (MenuActivity)
            finish()
        }
    }
}
