package com.cyberfox21.tinkoffmessanger

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val customEmojiView = findViewById<CustomEmojiView>(R.id.custom_emoji_view)
//        customEmojiView.count = "2"
//        customEmojiView.emoji = "\uD83D\uDE02"
//        customEmojiView.setOnClickListener {
//            it.isSelected = !it.isSelected
//        }

        val flexBoxLayout = findViewById<FlexBoxLayout>(R.id.flexbox_layout)
        val btnAdd = findViewById<ImageView>(R.id.iv_add_emoji)
        btnAdd.setOnClickListener {
            flexBoxLayout.addView(CustomEmojiView(this).apply {
                count = "8"
                emoji = "\uD83D\uDE02"
                onEmojiClickListener = object : CustomEmojiView.OnEmojiClickListener {
                    override fun onEmojiClick(view: CustomEmojiView) {
                        view.isSelected = !view.isSelected
                    }
                }
            })
            Log.d("MainActivity", "viewAdded")
        }
    }
}
