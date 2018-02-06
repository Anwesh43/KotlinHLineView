package ui.anwesome.com.kotlinhlineview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.hlineview.HLineView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = HLineView.create(this)
    }
}
