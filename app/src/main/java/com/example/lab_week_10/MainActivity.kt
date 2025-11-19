package com.example.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {
    private val db by lazy { prepareDatabase() }
    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    companion object {
        const val ID: Long = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeValueFromDatabase()
        prepareViewModel()
    }

    // Tampilkan Tanggal di onStart (sesuai instruksi bonus)
    override fun onStart() {
        super.onStart()
        val totalList = db.totalDao().getTotal(ID)
        if (totalList.isNotEmpty()) {
            val date = totalList.first().total.date
            Toast.makeText(this, date, Toast.LENGTH_LONG).show()
        }
    }

    override fun onPause() {
        super.onPause()
        // Update ke DB dengan Waktu Sekarang
        val currentDate = Date().toString()
        val currentTotalValue = viewModel.total.value ?: 0

        // Simpan dengan TotalObject
        val dataToSave = Total(
            id = ID,
            total = TotalObject(value = currentTotalValue, date = currentDate)
        )

        db.totalDao().update(dataToSave)
    }

    private fun prepareDatabase(): TotalDatabase {
        // Note: Jika Anda mengubah skema DB tanpa uninstall app, ini mungkin crash.
        // Sebaiknya uninstall app di emulator dulu atau naikkan version DB dan sediakan migrasi.
        // Untuk lab ini, uninstall app cukup.
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java, "total-database"
        ).allowMainThreadQueries().build()
    }

    private fun initializeValueFromDatabase() {
        val totalList = db.totalDao().getTotal(ID)
        if (totalList.isEmpty()) {
            // Insert data awal dengan tanggal sekarang
            val initialData = Total(
                id = 1,
                total = TotalObject(value = 0, date = Date().toString())
            )
            db.totalDao().insert(initialData)
        } else {
            // Akses value melalui embedded object
            viewModel.setTotal(totalList.first().total.value)
        }
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text = getString(R.string.text_total, total)
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this) { total ->
            updateText(total)
        }
        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }
}