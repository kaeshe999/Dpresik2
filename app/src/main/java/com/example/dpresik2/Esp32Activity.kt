package com.example.dpresik2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dpresik2.databinding.ActivityEsp32Binding // Importa el binding de la pantalla ESP32

class Esp32Activity : AppCompatActivity() {

    private lateinit var binding: ActivityEsp32Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEsp32Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Listener para el Switch
        binding.esp32Switch.setOnCheckedChangeListener { _, isChecked ->
            // TODO: Más adelante, aquí enviaremos el dato a Firebase (Realtime Database o Firestore)
            if (isChecked) {
                binding.statusTextEsp32.text = "Estado: Activado"
            } else {
                binding.statusTextEsp32.text = "Estado: Desactivado"
            }
        }

        // Configurar el botón de "Volver"
        binding.backButtonEsp32.setOnClickListener {
            finish() // Cierra esta actividad y regresa al Menú
        }
    }
}
