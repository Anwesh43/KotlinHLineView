package ui.anwesome.com.hlineview

/**
 * Created by anweshmishra on 06/02/18.
 */
import android.content.Context
import android.graphics.*
import android.view.*
class HLineView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}