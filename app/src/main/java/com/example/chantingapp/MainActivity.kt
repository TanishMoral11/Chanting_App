package com.example.chantingapp
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity(), BackgroundPagerAdapter.OnImageClickListener {

    private var count = 0
    private lateinit var countTextView: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: BackgroundPagerAdapter
    private val backgroundImages = listOf(
        R.drawable.pxfuel,
        R.drawable.imageone,
        R.drawable.imagetwo,
        R.drawable.three
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        countTextView = findViewById(R.id.countTextView)
        val resetButton = findViewById<Button>(R.id.resetButton)
        viewPager = findViewById(R.id.viewPager)
        adapter = BackgroundPagerAdapter(this, backgroundImages, this)
        viewPager.adapter = adapter

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.sound)

        // Set initial count
        updateCount()

        // Set click listener for reset button
        resetButton.setOnClickListener {
            // Reset count to 0
            count = 0
            updateCount()
        }

        // Set current item to a large number to enable looping effect
        viewPager.currentItem = Int.MAX_VALUE / 2
    }

    override fun onImageClick(position: Int) {
        // Increment count and update TextView
        count++
        updateCount()
    }

    private fun updateCount() {
        if (count == 108) {
            // Play sound
            mediaPlayer.start()
            // Reset count
            count = 0
        }
        countTextView.text = count.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release MediaPlayer resources
        mediaPlayer.release()
    }
}
