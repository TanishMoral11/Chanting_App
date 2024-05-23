package com.example.chantingapp

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity(), BackgroundPagerAdapter.OnImageClickListener {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var countTextView: TextView
    private lateinit var roundsTextView: TextView
    private lateinit var streakTextView: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: BackgroundPagerAdapter
    private var count = 0
    private var rounds = 0
    private var streak = 0
    private val backgroundImages = listOf(
        R.drawable.pxfuel,
        R.drawable.imageone,
        R.drawable.imagetwo,
        R.drawable.three
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        countTextView = findViewById(R.id.countTextView)
        roundsTextView = findViewById(R.id.roundsTextView)
        streakTextView = findViewById(R.id.streakTextView)
        val resetButton = findViewById<Button>(R.id.resetButton)
        viewPager = findViewById(R.id.viewPager)
        adapter = BackgroundPagerAdapter(this, backgroundImages, this)
        viewPager.adapter = adapter

        mediaPlayer = MediaPlayer.create(this, R.raw.sound)

        resetButton.setOnClickListener {
            count = 0
            rounds = 0

            updateCount()
        }

        viewPager.currentItem = Int.MAX_VALUE / 2
    }

    override fun onResume() {
        super.onResume()
        loadCounts()
        checkStreak()
        updateCount()
    }

    override fun onPause() {
        super.onPause()
        saveCounts()
    }

    override fun onImageClick(position: Int) {
        count++
        if (count == 108) {
            rounds++
            count = 0
            updateStreak()
        }
        updateCount()
    }

    private fun updateCount() {
        if (count == 0 && rounds > 0) {
            mediaPlayer.start()
        }
        countTextView.text = count.toString()
        roundsTextView.text = rounds.toString()
        streakTextView.text = "Streak: $streak"
    }

    private fun updateStreak() {
        val currentDate = getCurrentDate()
        val lastChantDate = sharedPreferences.getString("lastChantDate", null)
        if (lastChantDate != null) {
            val daysPassed = getDaysPassed(lastChantDate, currentDate)
            streak = if (daysPassed == 1) streak + 1 else 1
        } else {
            streak = 1
        }
        sharedPreferences.edit().putString("lastChantDate", currentDate).apply()
    }

    private fun checkStreak() {
        val currentDate = getCurrentDate()
        val lastChantDate = sharedPreferences.getString("lastChantDate", null)
        if (lastChantDate != null) {
            val daysPassed = getDaysPassed(lastChantDate, currentDate)
            if (daysPassed > 1) {
                streak = 0
            }
        }
    }

    private fun loadCounts() {
        count = sharedPreferences.getInt("count", 0)
        rounds = sharedPreferences.getInt("rounds", 0)
        streak = sharedPreferences.getInt("streak", 0)
    }

    private fun saveCounts() {
        sharedPreferences.edit().apply {
            putInt("count", count)
            putInt("rounds", rounds)
            putInt("streak", streak)
            apply()
        }
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun getDaysPassed(lastDate: String, currentDate: String): Int {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val last = dateFormat.parse(lastDate)
        val current = dateFormat.parse(currentDate)
        val difference = current.time - last.time
        return (difference / (1000 * 60 * 60 * 24)).toInt()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
