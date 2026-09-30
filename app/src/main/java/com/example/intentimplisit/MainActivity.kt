package com.example.intentimplisit

import android.content.Intent
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
    }
}