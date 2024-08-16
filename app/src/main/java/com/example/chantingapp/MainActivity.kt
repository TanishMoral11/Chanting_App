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

    // Declare SharedPreferences for storing and retrieving persistent key-value pairs
    private lateinit var sharedPreferences: SharedPreferences

    // Declare UI components
    private lateinit var countTextView: TextView
    private lateinit var roundsTextView: TextView
    private lateinit var streakTextView: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: BackgroundPagerAdapter

    // Variables to store chant counts, rounds, and streaks
    private var count = 0
    private var rounds = 0
    private var streak = 0

    // Array of background images
    private val backgroundImages = intArrayOf(
        R.drawable.pxfuel,
        R.drawable.imageone,
        R.drawable.imagetwo,
        R.drawable.three
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences with the name "MyPrefs"
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Initialize UI components
        countTextView = findViewById(R.id.countTextView)
        roundsTextView = findViewById(R.id.roundsTextView)
        streakTextView = findViewById(R.id.streakTextView)
        val resetButton: Button = findViewById(R.id.resetButton)
        viewPager = findViewById(R.id.viewPager)


        // Initialize the ViewPager adapter
        adapter = BackgroundPagerAdapter(this, backgroundImages.toList(), this)
        viewPager.adapter = adapter

        // Initialize MediaPlayer with a sound resource
        mediaPlayer = MediaPlayer.create(this, R.raw.sound)

        // Set up the reset button to reset count and rounds
        resetButton.setOnClickListener {
            count = 0
            rounds = 0
            updateCount()
        }

        // Start the ViewPager at the middle position
        viewPager.currentItem = Integer.MAX_VALUE / 2
    }

    override fun onResume() {
        super.onResume()
        // Load counts and check streak when the activity resumes
        loadCounts()
        checkStreak()
        updateCount()
    }

    override fun onPause() {
        super.onPause()
        // Save counts when the activity is paused
        saveCounts()
    }

    // Handle image clicks in the ViewPager
    override fun onImageClick(position: Int) {
        count++
        // When count reaches 108, increment rounds and reset count
        if (count == 108) {
            rounds++
            count = 0
            updateStreak()
        }
        updateCount()
    }

    // Update the count, rounds, and streak text views
    private fun updateCount() {
        // Play sound when count is reset after completing a round
        if (count == 0 && rounds > 0) {
            mediaPlayer.start()
        }
        countTextView.text = count.toString()
        roundsTextView.text = rounds.toString()
        streakTextView.text = "Streak: $streak"
    }

    // Update the streak based on the last chant date
    private fun updateStreak() {
        val currentDate = getCurrentDate()
        val lastChantDate = sharedPreferences.getString("lastChantDate", null)
        if (lastChantDate != null) {
            val daysPassed = getDaysPassed(lastChantDate, currentDate)
            streak = if (daysPassed == 1) minOf(streak + 1, 100) else streak
        } else {
            streak = 1 // Set initial streak to 1
        }
        // Save the current date as the last chant date
        sharedPreferences.edit().putString("lastChantDate", currentDate).apply()
    }

    // Check if the streak should be reset
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

    // Load counts, rounds, and streak from SharedPreferences
    private fun loadCounts() {
        count = sharedPreferences.getInt("count", 0)
        rounds = sharedPreferences.getInt("rounds", 0)
        streak = sharedPreferences.getInt("streak", 0)
    }

    // Save counts, rounds, and streak to SharedPreferences
    private fun saveCounts() {
        sharedPreferences.edit()
            .putInt("count", count)
            .putInt("rounds", rounds)
            .putInt("streak", streak)
            .apply()
    }

    // Get the current date in "yyyy-MM-dd" format
    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    // Calculate the number of days passed between two dates
    private fun getDaysPassed(lastDate: String, currentDate: String): Int {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            val last = dateFormat.parse(lastDate)?.time ?: 0L
            val current = dateFormat.parse(currentDate)?.time ?: 0L
            val dayspassed = ((current - last) / (1000 * 60 * 60 * 24)).toInt()

            if(dayspassed>=2){
                0
            }
            else {
                dayspassed
            }

        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the MediaPlayer resource when the activity is destroyed
        mediaPlayer.release()
    }
}
