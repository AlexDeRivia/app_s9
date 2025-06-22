package com.example.app_s9

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.LinearLayout


class UserProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var editTextName: EditText
    private lateinit var editTextAge: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonLoad: Button
    private lateinit var textViewProfileResult: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        applySavedTheme()
        editTextName = findViewById(R.id.editTextName)
        editTextAge = findViewById(R.id.editTextAge)
        editTextEmail = findViewById(R.id.editTextEmail)
        buttonSave = findViewById(R.id.buttonSaveProfile)
        buttonLoad = findViewById(R.id.buttonLoadProfile)
        textViewProfileResult = findViewById(R.id.textViewProfileResult)


        buttonSave.setOnClickListener {
            saveProfile()
        }

        buttonLoad.setOnClickListener {
            loadProfile()
        }
    }

    private fun saveProfile() {
        val name = editTextName.text.toString().trim()
        val age = editTextAge.text.toString().trim()
        val email = editTextEmail.text.toString().trim()

        if (name.isEmpty() || age.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        sharedPreferencesHelper.saveString("user_name", name)
        sharedPreferencesHelper.saveInt("user_age", age.toInt())
        sharedPreferencesHelper.saveString("user_email", email)

        Toast.makeText(this, "Perfil guardado", Toast.LENGTH_SHORT).show()
    }

    private fun loadProfile() {
        val name = sharedPreferencesHelper.getString("user_name", "Sin nombre")
        val age = sharedPreferencesHelper.getInt("user_age", 0)
        val email = sharedPreferencesHelper.getString("user_email", "Sin correo")
        val result = """
            Nombre: $name
            Edad: $age
            Email: $email
        """.trimIndent()

        textViewProfileResult.text = result


        editTextName.setText(name)
        editTextAge.setText(age.toString())
        editTextEmail.setText(email)
    }

    private fun applySavedTheme() {
        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        val isDarkMode = sharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_THEME_MODE, false)

        val rootLayout = findViewById<LinearLayout>(R.id.userProfileLayout)
        rootLayout.setBackgroundColor(if (isDarkMode) 0xFF424242.toInt() else 0xFFFFFFFF.toInt())

        // Cambiar colores de texto manualmente
        val textViewResult = findViewById<TextView>(R.id.textViewProfileResult)
        textViewResult.setTextColor(if (isDarkMode) 0xFFFFFFFF.toInt() else 0xFF000000.toInt())
    }


}
