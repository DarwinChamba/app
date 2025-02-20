package com.example.appcitas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AppointmentsActiviry : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    //private lateinit var appointmentsAdapter: AppointmentsAdapter
    private lateinit var dateText: TextView
    /*
    companion object {
        const val EXTRA_DATE = "extra_date"

        fun start(context: Context, date: LocalDate) {
            val intent = Intent(context, AppointmentsActivity::class.java).apply {
                putExtra(EXTRA_DATE, date.toString())
            }
            context.startActivity(intent)
        }
    }

     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_appointments_activiry)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

       // val dateString = intent.getStringExtra(EXTRA_DATE)
       // val date = LocalDate.parse(dateString)

        //setupViews(date)
        //loadAppointments(date)
    }
    /*
    private fun setupViews(date: LocalDate) {
        dateText = findViewById(R.id.dateText)
        recyclerView = findViewById(R.id.appointmentsRecyclerView)

        dateText.text = date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))

        appointmentsAdapter = AppointmentsAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AppointmentsActivity)
            adapter = appointmentsAdapter
        }
    }

    private fun loadAppointments(date: LocalDate) {
        // Aquí cargarías las citas de tu base de datos o fuente de datos
        val appointments = getAppointmentsForDate(date)
        appointmentsAdapter.submitList(appointments)
    }

     */
}