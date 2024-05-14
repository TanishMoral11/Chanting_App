package com.example.chantingapp
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity(), BackgroundPagerAdapter.OnImageClickListener {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var countTextView: TextView
    private lateinit var roundsTextView: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: BackgroundPagerAdapter
    private var count = 0
    private var rounds = 0
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
        // Load count and round count from SharedPreferences
        count = sharedPreferences.getInt("count", 0)
        rounds = sharedPreferences.getInt("rounds", 0)
        updateCount()
    }

    override fun onPause() {
        super.onPause()
        // Save count and round count to SharedPreferences
        sharedPreferences.edit().apply {
            putInt("count", count)
            putInt("rounds", rounds)
            apply()
        }
    }

    override fun onImageClick(position: Int) {
        count++
        if (count == 108) {
            rounds++
            count = 0
        }
        updateCount()
    }

    private fun updateCount() {
        if (count == 0 && rounds > 0) {
            mediaPlayer.start()
        }
        countTextView.text = count.toString()
        roundsTextView.text = rounds.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
