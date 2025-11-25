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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase
        auth = Firebase.auth
        firestore = Firebase.firestore

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
        // Sign up button
        signUpButton.setOnClickListener {
            performSignUp()
        }

        // Password visibility toggle
        passwordToggle.setOnClickListener {
            togglePasswordVisibility()
        }

        // Confirm password visibility toggle
        confirmPasswordToggle.setOnClickListener {
            toggleConfirmPasswordVisibility()
        }

        // Login link - Navigate back to MainActivity
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

        // Validation
        if (fullName.isEmpty()) {
            Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show()
            return
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
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

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (!termsCheckbox.isChecked) {
            Toast.makeText(this, "Please agree to the Terms & Conditions", Toast.LENGTH_SHORT).show()
            return
        }

        // Disable button during registration
        signUpButton.isEnabled = false
        signUpButton.text = "Creating account..."

        // Firebase registration
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                // Save user data to Firestore
                val userId = result.user?.uid ?: run {
                    signUpButton.isEnabled = true
                    signUpButton.text = "Sign up"
                    return@addOnSuccessListener
                }

                val userData = mapOf(
                    "fullName" to fullName,
                    "email" to email,
                    "createdAt" to System.currentTimeMillis()
                )

                firestore.collection("users").document(userId)
                    .set(userData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                        clearInputs()
                        // Navigate to login
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error saving user data: ${e.message}", Toast.LENGTH_SHORT).show()
                        signUpButton.isEnabled = true
                        signUpButton.text = "Sign up"
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
                signUpButton.isEnabled = true
                signUpButton.text = "Sign up"
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