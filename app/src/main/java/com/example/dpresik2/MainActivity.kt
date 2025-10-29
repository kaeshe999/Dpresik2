package com.example.dpresik2

import android.content.Intent // AÑADIR: Import para navegar
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.dpresik2.databinding.ActivityMainBinding // IMPORTANTE: Importa el ViewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // Tag para logs
    private val TAG = "Auth" // Cambiado a "Auth" (más general)

    // Componentes de Firebase y Google Sign-In
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    // ViewBinding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configura ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Inicializar Firebase Auth
        auth = Firebase.auth

        // 2. Configurar Google Sign-In Options (GSO)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // 3. Crear el Google Sign-In Client
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 4. Configurar Listeners de Clicks
        binding.googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        // AÑADIR: Listeners para Email/Contraseña
        binding.emailLoginButton.setOnClickListener {
            signInWithEmail()
        }
        binding.emailRegisterButton.setOnClickListener {
            registerWithEmail()
        }

        // ELIMINADO: El listener para signOutButton ya no va aquí.
    }

    // ----------------------------------------------------------------------
    // CICLO DE VIDA
    // ----------------------------------------------------------------------

    // Se ejecuta al iniciar o reanudar la actividad
    override fun onStart() {
        super.onStart()
        // MODIFICADO: Revisa si el usuario ya ha iniciado sesión
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d(TAG, "Usuario ya logueado (${currentUser.email}). Navegando al menú.")
            goToMenuActivity()
        }
        // Si no, el usuario se queda en esta pantalla de login.
    }

    // ----------------------------------------------------------------------
    // NAVEGACIÓN
    // ----------------------------------------------------------------------

    // AÑADIR: Función para ir al Menú Principal
    private fun goToMenuActivity() {
        val intent = Intent(this, MenuActivity::class.java)
        // Limpiamos el historial para que el usuario no pueda "volver" a la pantalla de login
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Cierra MainActivity para que no quede en el historial
    }

    // ----------------------------------------------------------------------
    // LOGIN CON EMAIL Y CONTRASEÑA
    // ----------------------------------------------------------------------

    // AÑADIR: Función para iniciar sesión con Email
    private fun signInWithEmail() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa email y contraseña.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Intentando iniciar sesión con Email...")
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Inicio de sesión con Email exitoso.")
                    goToMenuActivity() // Navega al menú
                } else {
                    Log.w(TAG, "Inicio de sesión con Email fallido.", task.exception)
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // AÑADIR: Función para registrar con Email
    private fun registerWithEmail() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa email y contraseña.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Intentando registrar con Email...")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Registro con Email exitoso.")
                    // Firebase inicia sesión automáticamente después de registrar
                    goToMenuActivity() // Navega al menú
                } else {
                    Log.w(TAG, "Registro con Email fallido.", task.exception)
                    Toast.makeText(this, "Error de registro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // ----------------------------------------------------------------------
    // LOGIN CON GOOGLE
    // ----------------------------------------------------------------------

    // Lanza la actividad de inicio de sesión de Google
    private fun signInWithGoogle() {
        Log.d(TAG, "Lanzando Intent de Google Sign-In...")
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    // Activity Result Launcher: El método moderno para manejar resultados
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        Log.d(TAG, "Resultado de Google Sign-In recibido.")
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign-In fue exitoso, obtén la cuenta.
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "Google Sign-In exitoso. ID Token: " + account.idToken)
                // Ahora autentica con Firebase
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign-In falló.
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "Falló el inicio de sesión de Google: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Log.w(TAG, "Google sign in cancelled by user.")
            Toast.makeText(this, "Inicio de sesión cancelado.", Toast.LENGTH_SHORT).show()
        }
    }

    // Autentica con Firebase usando el ID Token de Google
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "Iniciando autenticación con Firebase...")
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        // Deshabilitar el botón mientras se procesa
        binding.googleSignInButton.isEnabled = false

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                // Habilitar de nuevo
                binding.googleSignInButton.isEnabled = true

                if (task.isSuccessful) {
                    // Autenticación de Firebase exitosa
                    Log.d(TAG, "Autenticación de Firebase (Google) exitosa.")

                    // MODIFICADO: Reemplazamos el TODO
                    goToMenuActivity() // Navega al menú

                } else {
                    // Si falla el inicio de sesión
                    Log.w(TAG, "Autenticación de Firebase (Google) fallida.", task.exception)
                    Toast.makeText(this, "Error de Autenticación de Firebase.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // ELIMINADO: La función signOut() ya no está aquí.
    // ELIMINADO: La función updateUI() ya no es necesaria aquí.
}

