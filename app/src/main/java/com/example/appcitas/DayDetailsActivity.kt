package com.example.appcitas

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DayDetailsActivity : AppCompatActivity() {
    companion object {
        // Definimos la constante para el extra
        const val EXTRA_DATE = "selected_date"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_day_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Recuperar la fecha que fue enviada
        val dateString = intent.getStringExtra(EXTRA_DATE)
        // Convertir el string a LocalDate
        val selectedDate = LocalDate.parse(dateString)

        // Ejemplo de cómo usar la fecha
        val dateTextView = findViewById<TextView>(R.id.dateText)
        dateTextView.text = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }
}