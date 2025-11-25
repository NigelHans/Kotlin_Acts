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

class RegisterActivity : AppCompatActivity() {

    private lateinit var fullNameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var passwordToggle: ImageButton
    private lateinit var confirmPasswordToggle: ImageButton
    private lateinit var signUpButton: Button
    private lateinit var termsCheckbox: CheckBox
    private lateinit var loginText: TextView

    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize views
        initializeViews()

        // Set up click listeners
        setupClickListeners()
    }

    private fun initializeViews() {
        fullNameInput = findViewById(R.id.fullNameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput)
        passwordToggle = findViewById(R.id.passwordToggle)
        confirmPasswordToggle = findViewById(R.id.confirmPasswordToggle)
        signUpButton = findViewById(R.id.signUpButton)
        termsCheckbox = findViewById(R.id.termsCheckbox)
        loginText = findViewById(R.id.loginText)
    }

    private fun setupClickListeners() {
        signUpButton.setOnClickListener {
            performSignUp()
        }

        passwordToggle.setOnClickListener {
            togglePasswordVisibility()
        }

        confirmPasswordToggle.setOnClickListener {
            toggleConfirmPasswordVisibility()
        }

        loginText.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun performSignUp() {
        val fullName = fullNameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        val confirmPassword = confirmPasswordInput.text.toString().trim()

        // Validation using when expression for cleaner code
        when {
            fullName.isEmpty() -> {
                Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show()
                fullNameInput.requestFocus()
            }
            email.isEmpty() -> {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                emailInput.requestFocus()
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                emailInput.requestFocus()
            }
            password.isEmpty() -> {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                passwordInput.requestFocus()
            }
            password.length < 6 -> {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                passwordInput.requestFocus()
            }
            confirmPassword.isEmpty() -> {
                Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show()
                confirmPasswordInput.requestFocus()
            }
            password != confirmPassword -> {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                confirmPasswordInput.requestFocus()
            }
            !termsCheckbox.isChecked -> {
                Toast.makeText(this, "Please agree to the Terms & Conditions", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // All validations passed - registration successful
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                clearInputs()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        passwordInput.setSelection(passwordInput.text.length)
    }

    private fun toggleConfirmPasswordVisibility() {
        isConfirmPasswordVisible = !isConfirmPasswordVisible
        if (isConfirmPasswordVisible) {
            confirmPasswordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            confirmPasswordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        confirmPasswordInput.setSelection(confirmPasswordInput.text.length)
    }

    private fun clearInputs() {
        fullNameInput.text.clear()
        emailInput.text.clear()
        passwordInput.text.clear()
        confirmPasswordInput.text.clear()
        termsCheckbox.isChecked = false
    }
}