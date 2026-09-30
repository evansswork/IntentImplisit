package com.example.intentimplisit

import android.app.DatePickerDialog
import android.content.Intent
import java.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.TimeZone
import android.app.TimePickerDialog // Tambahkan ini
import android.provider.CalendarContract // Tambahkan ini
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnKirimPesan = findViewById<Button>(R.id.btnKirimPesan)

        btnKirimPesan.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra("address","0811234")
                putExtra("sms_body","ISI SMS")
                type = "text/plain"
            }

            if (sendIntent.resolveActivity(packageManager) != null) {
                //startActivity(sendIntent)
                startActivity(Intent.createChooser(sendIntent, "PILIH APLIKASI"))
            }
        }

        val btnSetAlarm = findViewById<Button>(R.id.btnSetAlarm)
        btnSetAlarm.setOnClickListener {
            val alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, "COBA ALARM")
                putExtra(AlarmClock.EXTRA_HOUR, 20)
                putExtra(AlarmClock.EXTRA_MINUTES, 15)
                putExtra(AlarmClock.EXTRA_SKIP_UI,true)
            }

            startActivity(alarmIntent)
        }

        val btnSetTimer = findViewById<Button>(R.id.btnSetTimer)
        btnSetTimer.setOnClickListener {
            val timerIntent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, "COBA TIMER")
                putExtra(AlarmClock.EXTRA_LENGTH, 20)
                putExtra(AlarmClock.EXTRA_SKIP_UI,true)
            }

            startActivity(timerIntent)
        }

        var webURL = findViewById<EditText>(R.id.etURL)
        var buttonOpenURL = findViewById<Button>(R.id.btnOpenURL)

        buttonOpenURL.setOnClickListener {
            var webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://"+webURL.text.toString())
            )

            if(intent.resolveActivity(packageManager) != null){
                startActivity(webIntent)
            }else{
                Toast.makeText(
                    this,
                    "Tidak ada aplikasi browser ditemukan",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        var setEvent = findViewById<Button>(R.id.btnSetEvent)
        setEvent.setOnClickListener {
            val calender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"))
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH)
            val day = calender.get(Calendar.DAY_OF_MONTH)

            val hour = calender.get(Calendar.HOUR_OF_DAY)
            val minute = calender.get(Calendar.MINUTE)

            val datePickerDialog =
                DatePickerDialog(this,
                    {
                        _, selectedYear, selectedMonth, selectedDay ->
                        val timePickerDialog = TimePickerDialog(this,{
                            _,
                            selectedHour,
                            selectedMinute ->

                            val selectedDateTime = Calendar.getInstance().apply {
                                set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)
                            }

                            val endTime = selectedDateTime.clone() as Calendar
                            endTime.add(Calendar.HOUR_OF_DAY, 1)

                            val eventIntent = Intent(Intent.ACTION_INSERT).apply {
                                data = CalendarContract.Events.CONTENT_URI
                                putExtra(CalendarContract.Events.TITLE, "Meeting")
                                putExtra(CalendarContract.Events.EVENT_LOCATION, "Kantor")
                                putExtra(CalendarContract.Events.DESCRIPTION, "Deskripsi Meeting")
                                putExtra(CalendarContract.Events.ALL_DAY, false)
                                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, selectedDateTime.timeInMillis)
                                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.timeInMillis)
                            }
                            startActivity(eventIntent)

                            }, hour, minute, true)
                        timePickerDialog.show()
                    }, year, month, day)

            datePickerDialog.show()
        }

        var ivHasil = findViewById<ImageView>(R.id.ivHasil)

        val cameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicturePreview()
        ){ bitmap ->
            if (bitmap != null){
                ivHasil.setImageBitmap(bitmap)
            }
        }
        var getPhoto = findViewById<Button>(R.id.btnGetPhoto)

        getPhoto.setOnClickListener {
            cameraLauncher.launch(null)
        }

        var bukaMaps = findViewById<Button>(R.id.btnBukaMaps)
        bukaMaps.setOnClickListener {
            val latitude = "-7.24611"
            val longitude = "112.73750"
            val labelTempat = "Tugu Pahlawan"

            val gmnIntentUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($labelTempat)")

            var mapIntent = Intent(Intent.ACTION_VIEW, gmnIntentUri).apply{
                setPackage("com.google.android.apps.maps")
            }

            if(mapIntent.resolveActivity(packageManager) != null){
                startActivity(mapIntent)
            }else{
                Toast.makeText(
                    this,
                    "Tidak ada aplikasi maps ditemukan",
                    Toast.LENGTH_SHORT).show()

                val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
                val webIntent = Intent(Intent.ACTION_VIEW, webUri)

                try{
                    startActivity(webIntent)
                }catch (e2 : Exception){
                    Toast.makeText(
                        this,
                        "Tidak bisa membuka maps",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }



        }

    }
}