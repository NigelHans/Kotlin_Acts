package com.example.shareh

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var passwordToggle: ImageButton
    private lateinit var loginButton: Button
    private lateinit var rememberMeCheckbox: CheckBox
    private lateinit var forgotPasswordText: TextView
    private lateinit var signUpText: TextView

    private lateinit var auth: FirebaseAuth
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Initialize views
        initializeViews()

        // Set up click listeners
        setupClickListeners()
    }

    private fun initializeViews() {
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        passwordToggle = findViewById(R.id.passwordToggle)
        loginButton = findViewById(R.id.loginButton)
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox)
        forgotPasswordText = findViewById(R.id.forgotPasswordText)
        signUpText = findViewById(R.id.signUpText)
    }

    private fun setupClickListeners() {
        // Login button
        loginButton.setOnClickListener {
            performLogin()
        }

        // Password visibility toggle
        passwordToggle.setOnClickListener {
            togglePasswordVisibility()
        }

        // Forgot password
        forgotPasswordText.setOnClickListener {
            Toast.makeText(this, "Forgot password clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to forgot password screen
        }

        // Sign up - Navigate to Register Activity
        signUpText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun performLogin() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        // Validation
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        // Disable button during login
        loginButton.isEnabled = false
        loginButton.text = "Logging in..."

        // Firebase login
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                clearInputs()
                loginButton.isEnabled = true
                loginButton.text = "Login"
                // TODO: Navigate to home screen
                // Example: startActivity(Intent(this, HomeActivity::class.java))
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                loginButton.isEnabled = true
                loginButton.text = "Login"
            }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            // Show password
            passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            // Hide password
            passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        // Move cursor to end
        passwordInput.setSelection(passwordInput.text.length)
    }

    private fun clearInputs() {
        emailInput.text.clear()
        passwordInput.text.clear()
        rememberMeCheckbox.isChecked = false
    }
}