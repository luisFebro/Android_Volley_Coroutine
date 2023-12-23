package com.febro.volleycoroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.UiThread
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.febro.volleycoroutine.databinding.ActivityMainBinding
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

public val LifecycleOwner.lifecycleScope: LifecycleCoroutineScope
    get() = lifecycle.coroutineScope

// ref: https://stackoverflow.com/questions/53486087/how-can-i-use-coroutines-with-volley-so-that-my-code-can-be-written-like-sychron
class MainActivity : AppCompatActivity() {
    // private val scope = MainScope
    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        // NOT WORKING
        b.button.setOnClickListener {
            launch {
                val data = getData()
                b.textView.text = data
            }
        }
    }

    @UiThread
    suspend fun getData() = suspendCoroutine { continuation ->
        val queue = Volley.newRequestQueue(this)
        val url = "http://www.google.com"

        val request = StringRequest(Request.Method.GET, url,
            { continuation.resume("Response is: ${it.substring(0, 500)}") },
            { continuation.resume("Something went wrong!. Details: $it") })

        queue.add(request)
    }
}