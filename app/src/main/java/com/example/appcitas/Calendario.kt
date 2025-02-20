package com.example.appcitas

import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import com.example.CalendarAdapter
import com.example.appcitas.databinding.ActivityCalendarioBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class Calendario : AppCompatActivity() {
    private lateinit var calendarGrid: GridView
    private lateinit var monthYearText: TextView
    private lateinit var appointmentsRecyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var binding:ActivityCalendarioBinding
    private var currentMonth = YearMonth.now()
    private var days: List<CalendarDay> = emptyList()
    private val appointments = mutableListOf<Appointment>()
    // Guardamos la fecha mínima (hoy)
    private  lateinit var reference:DatabaseReference
    private val minDate = LocalDate.now()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityCalendarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupViews()
        setupCalendar()
        updateCalendarForMonth(currentMonth)
        val data =FirebaseDatabase.getInstance()
        reference=data.getReference("Citas")
        binding.btnFirebase.setOnClickListener{
            reference.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(a in snapshot.children){

                        }
                    }else{
                        Toast.makeText(this@Calendario, "no encontardo", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

    }
    private fun setupViews() {
        calendarGrid = findViewById(R.id.calendarGrid)
        monthYearText = findViewById(R.id.monthYearText)

        // Botón mes anterior - se deshabilita si estamos en el mes actual
        findViewById<ImageButton>(R.id.previousMonth).apply {
            setOnClickListener {
                val previousMonth = currentMonth.minusMonths(1)
                if (previousMonth.year >= minDate.year &&
                    previousMonth.monthValue >= minDate.monthValue) {
                    currentMonth = previousMonth
                    updateCalendarForMonth(currentMonth)
                }
            }
            // Deshabilitar si estamos en el mes actual
            isEnabled = false  // Inicialmente deshabilitado
        }

        // Botón mes siguiente
        findViewById<ImageButton>(R.id.nextMonth).setOnClickListener {
            currentMonth = currentMonth.plusMonths(1)
            updateCalendarForMonth(currentMonth)
        }
    }

    private fun setupCalendar() {
        calendarAdapter = CalendarAdapter(this) { selectedDay ->
            if (selectedDay.isSelectable) {
                val intent = Intent(this, DayDetailsActivity::class.java).apply {
                   putExtra(DayDetailsActivity.EXTRA_DATE, selectedDay.date.toString())
                }
                startActivity(intent)
            }
        }
        calendarGrid.adapter = calendarAdapter
    }

    private fun updateCalendarForMonth(yearMonth: YearMonth) {
        monthYearText.text = yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))

        // Actualizar estado del botón "mes anterior"
        findViewById<ImageButton>(R.id.previousMonth).isEnabled =
            yearMonth.year > minDate.year ||
                    (yearMonth.year == minDate.year && yearMonth.monthValue > minDate.monthValue)

        val newDays = mutableListOf<CalendarDay>()

        val firstDayOfMonth = yearMonth.atDay(1)
        val lastDayOfMonth = yearMonth.atEndOfMonth()

        // Días del mes anterior
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value
        if (firstDayOfWeek > 1) {
            for (i in firstDayOfWeek - 1 downTo 1) {
                val date = firstDayOfMonth.minusDays(i.toLong())
                newDays.add(CalendarDay(
                    date = date,
                    isCurrentMonth = false,
                    isSelectable = date >= minDate // Solo seleccionable si es después de hoy
                ))
            }
        }

        // Días del mes actual
        for (dayOfMonth in 1..lastDayOfMonth.dayOfMonth) {
            val date = yearMonth.atDay(dayOfMonth)
            newDays.add(CalendarDay(
                date = date,
                isCurrentMonth = true,
                isSelectable = date >= minDate // Solo seleccionable si es después de hoy
            ))
        }

        // Días del mes siguiente
        val remainingDays = 42 - newDays.size
        if (remainingDays > 0) {
            for (i in 1..remainingDays) {
                val date = lastDayOfMonth.plusDays(i.toLong())
                newDays.add(CalendarDay(
                    date = date,
                    isCurrentMonth = false,
                    isSelectable = true // Siempre seleccionable ya que es futuro
                ))
            }
        }

        days = newDays
        calendarAdapter.updateDays(days)
    }
}


