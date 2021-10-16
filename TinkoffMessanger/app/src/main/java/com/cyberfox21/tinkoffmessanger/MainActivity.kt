package com.cyberfox21.tinkoffmessanger

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cyberfox21.tinkoffmessanger.databinding.ActivityMainBinding
import com.cyberfox21.tinkoffmessanger.databinding.EmojiMessageViewgroupBinding
import com.cyberfox21.tinkoffmessanger.databinding.FlexboxlayoutWithAddButtonBinding

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var emojiMessageLayoutBinding: EmojiMessageViewgroupBinding
    private lateinit var flexBoxLayoutBinding: FlexboxlayoutWithAddButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        emojiMessageLayoutBinding =
            EmojiMessageViewgroupBinding.bind(activityMainBinding.emojiMessageLayout)
        flexBoxLayoutBinding =
            FlexboxlayoutWithAddButtonBinding.bind(emojiMessageLayoutBinding.emojiLayout)
        setContentView(activityMainBinding.root)
        var i = 8
        emojiMessageLayoutBinding.name.text = resources.getString(R.string.example_name)
        emojiMessageLayoutBinding.time.text = resources.getString(R.string.example_time)
        emojiMessageLayoutBinding.description.text = resources.getString(R.string.example_message)
        emojiMessageLayoutBinding.imageView.setImageResource(R.drawable.ed)
        flexBoxLayoutBinding.ivAddEmoji.setOnClickListener {
            emojiMessageLayoutBinding.emojiLayout.addView(CustomEmojiView(this).apply {
                count = i.toString()
                emoji = "\uD83D\uDE02"
                onEmojiClickListener = object : CustomEmojiView.OnEmojiClickListener {
                    override fun onEmojiClick(view: CustomEmojiView) {
                        view.isSelected = !view.isSelected
                    }
                }
                i += 10
            })
            Log.d("MainActivity", "viewAdded")
        }
    }
}
