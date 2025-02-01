package com.example.appcitas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.appcitas.databinding.ActivityRegistrarUsuariosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrarUsuarios : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarUsuariosBinding
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val user by lazy { User() }
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrarUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("Clinica")
        binding.btnRegistrar.setOnClickListener {
            registerUser()
        }
        binding.buttonseleccionarimagen.setOnClickListener { buscarImagen() }
        binding.tvTengoCuenta.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun buscarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        register.launch(intent)
    }

    val register = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val intent = it.data
            val img = intent?.data
            Glide.with(this).load(img).into(binding.circleImageView)
            user.imagen = img.toString()

        }
    }

    fun registerUser() {
        if (validarEntradaDatos() != null) {
            binding.progress.visibility = View.VISIBLE
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val id = firebaseAuth.currentUser?.uid
                        id?.let {
                            user.id = it
                        }


                        databaseReference.child("$id").setValue(user).addOnSuccessListener {
                            Toast.makeText(this, "Usuario creado exitosamente", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(this, GestionarCitas::class.java))
                            finish()
                        }.addOnFailureListener {

                            Toast.makeText(
                                this,
                                "Error al crear el usuario ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    } else {
                        Toast.makeText(
                            this,
                            "Error al crear el usuario ${task.exception}",
                            Toast.LENGTH_SHORT
                        ).show()
                        println("error $task.exception")
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Error al crear el usuario${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

    }

    private fun validarEntradaDatos(): User? {
        val nombre = binding.nombreUsuario.text.toString().trim()
        val apellido = binding.apellidoUsuario.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        return if (!nombre.isNullOrEmpty() && !apellido.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            user.nombre = nombre
            user.apellido = apellido
            user.email = email
            user.password = password
            user
        } else {
            null
        }

    }

}