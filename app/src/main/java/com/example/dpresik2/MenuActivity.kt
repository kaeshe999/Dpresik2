package com.example.dpresik2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dpresik2.databinding.ActivityMenuBinding // <--- Esto dará error hasta que creemos el XML
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val user = auth.currentUser

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Asegúrate de tener este string
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Mostrar bienvenida
        if (user != null) {
            binding.welcomeText.text = "Bienvenido,\n${user.displayName ?: user.email}" // Muestra email si no hay nombre
        }

        // --- Configurar Clicks de los Botones ---

        // Botón de Info de Cuenta
        binding.accountInfoButton.setOnClickListener {
            val intent = Intent(this, AccountInfoActivity::class.java) // <--- Esto dará error hasta que creemos AccountInfoActivity
            startActivity(intent)
        }

        // Botón de ESP32 (Visual)
        binding.esp32Button.setOnClickListener {
            val intent = Intent(this, Esp32Activity::class.java) // <--- Esto dará error hasta que creemos Esp32Activity
            startActivity(intent)
        }

        // Botón de Cerrar Sesión
        binding.menuSignOutButton.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        // Cerrar sesión de Firebase
        auth.signOut()

        // Cerrar sesión de Google
        googleSignInClient.signOut().addOnCompleteListener(this) {
            // Regresar a la pantalla de Login (MainActivity)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Toast.makeText(this, "Sesión cerrada.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}

