package com.example.android.codelab.animation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.android.codelab.animation.ui.AnimationCodelabTheme
import com.example.android.codelab.animation.ui.home.Home

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationCodelabTheme {
                Home()
            }
        }
    }
}
