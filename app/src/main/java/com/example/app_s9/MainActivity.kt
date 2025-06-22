package com.example.app_s9

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Switch
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout



class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var editTextUsername: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonLoad: Button
    private lateinit var buttonClear: Button
    private lateinit var buttonResetCounter: Button
    private lateinit var buttonGoToProfile: Button
    private lateinit var switchTheme: Switch
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ✅ Inicializar SharedPreferencesHelper ANTES del setContentView
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        // ✅ setContentView ANTES de usar findViewById
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ✅ Inicializar vistas y aplicar tema después de tener acceso a ellas
        initViews()
        applySavedTheme()

        setupListeners()
        checkFirstTime()
        incrementVisitCount()
    }

    private fun initViews() {
        editTextUsername = findViewById(R.id.editTextUsername)
        buttonSave = findViewById(R.id.buttonSave)
        buttonLoad = findViewById(R.id.buttonLoad)
        buttonClear = findViewById(R.id.buttonClear)
        buttonResetCounter = findViewById(R.id.buttonResetCounter)
        textViewResult = findViewById(R.id.textViewResult)
        buttonGoToProfile = findViewById(R.id.buttonGoToProfile)
        switchTheme = findViewById(R.id.switchTheme)
    }

    private fun setupListeners() {
        buttonSave.setOnClickListener {
            saveData()
        }

        buttonLoad.setOnClickListener {
            loadData()
        }

        buttonClear.setOnClickListener {
            clearAllData()
        }

        buttonResetCounter.setOnClickListener {
            resetVisitCount()
        }

        buttonGoToProfile.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferencesHelper.saveBoolean(SharedPreferencesHelper.KEY_THEME_MODE, isChecked)
            applySavedTheme()
        }
    }

    private fun saveData() {
        val username = editTextUsername.text.toString().trim()

        if (username.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa un nombre", Toast.LENGTH_SHORT).show()
            return
        }

        sharedPreferencesHelper.saveString(SharedPreferencesHelper.KEY_USERNAME, username)
        sharedPreferencesHelper.saveBoolean(SharedPreferencesHelper.KEY_IS_FIRST_TIME, false)
        sharedPreferencesHelper.saveInt(SharedPreferencesHelper.KEY_USER_ID, (1000..9999).random())

        Toast.makeText(this, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show()
        editTextUsername.setText("")
    }

    private fun loadData() {
        updateResultText()
    }

    private fun clearAllData() {
        sharedPreferencesHelper.clearAll()
        textViewResult.text = ""
        editTextUsername.setText("")
        Toast.makeText(this, "Todas las preferencias han sido eliminadas", Toast.LENGTH_SHORT).show()
    }

    private fun checkFirstTime() {
        val isFirstTime = sharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_IS_FIRST_TIME, true)

        if (isFirstTime) {
            Toast.makeText(this, "¡Bienvenido por primera vez!", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateResultText() {
        val username = sharedPreferencesHelper.getString(SharedPreferencesHelper.KEY_USERNAME, "Sin nombre")
        val isFirstTime = sharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_IS_FIRST_TIME, true)
        val userId = sharedPreferencesHelper.getInt(SharedPreferencesHelper.KEY_USER_ID, 0)
        val visitCount = sharedPreferencesHelper.getInt(SharedPreferencesHelper.KEY_VISIT_COUNT, 1)

        val profileName = sharedPreferencesHelper.getString("user_name", "N/D")
        val profileAge = sharedPreferencesHelper.getInt("user_age", 0)
        val profileEmail = sharedPreferencesHelper.getString("user_email", "N/D")

        val result = """
            Usuario: $username
            ID: $userId
            Primera vez: ${if (isFirstTime) "Sí" else "No"}
            Veces que abriste la app: $visitCount

            --- Perfil Guardado ---
            Nombre: $profileName
            Edad: $profileAge
            Email: $profileEmail
        """.trimIndent()

        textViewResult.text = result
    }

    private fun incrementVisitCount() {
        val currentCount = sharedPreferencesHelper.getInt(SharedPreferencesHelper.KEY_VISIT_COUNT, 0)
        val newCount = currentCount + 1
        sharedPreferencesHelper.saveInt(SharedPreferencesHelper.KEY_VISIT_COUNT, newCount)
        updateResultText()
    }

    private fun resetVisitCount() {
        sharedPreferencesHelper.saveInt(SharedPreferencesHelper.KEY_VISIT_COUNT, 0)
        Toast.makeText(this, "Contador de visitas reiniciado", Toast.LENGTH_SHORT).show()
        updateResultText()
    }

    private fun applySavedTheme() {
        val isDarkMode = sharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_THEME_MODE, false)

        val mainLayout = findViewById<ConstraintLayout>(R.id.main)
        val containerLayout = findViewById<LinearLayout>(R.id.container)

        // Fondo total (ConstraintLayout)
        mainLayout.setBackgroundColor(if (isDarkMode) 0xFF424242.toInt() else 0xFFFFFFFF.toInt())

        // Fondo interno también, por si acaso
        containerLayout.setBackgroundColor(if (isDarkMode) 0xFF424242.toInt() else 0xFFFFFFFF.toInt())

        // Color del texto
        textViewResult.setTextColor(if (isDarkMode) 0xFFFFFFFF.toInt() else 0xFF000000.toInt())

        // Sincronizar estado del switch
        switchTheme.isChecked = isDarkMode
    }


}
