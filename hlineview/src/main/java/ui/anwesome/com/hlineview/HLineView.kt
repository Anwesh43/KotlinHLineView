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
    data class HLineJoint(var w:Float,var h:Float) {
        val state = HLineState()
        fun draw(canvas:Canvas,paint:Paint) {
            val scales = state.scales
            val j = state.j
            canvas.save()
            canvas.translate(w/2-w/8,h/2)
            canvas.rotate(-90f*scales[0])
            canvas.drawLine(0f,0f,w/8,0f,paint)
            canvas.restore()
            if (j >= 1) {
                canvas.save()
                canvas.translate(w / 2 - w / 8, h / 2 - w / 4)
                canvas.rotate(180f*(1-scales[1]))
                canvas.drawLine(0f, 0f, 0f, -w / 8, paint)
                canvas.restore()
            }
            if(j >= 2) {
                canvas.save()
                canvas.translate(w / 2 - w / 8, h / 2)
                canvas.rotate(90f*scales[2])
                canvas.drawLine(0f, 0f, 0f, -w / 8, paint)
                canvas.restore()
            }
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class HLineState(var j:Int = 0,var jDir:Int = 0,var dir:Float = 0f,var prevScale:Float = 0f) {
        val scales:Array<Float> = arrayOf(0f,0f,0f)
        fun update(stopcb:(Float)->Unit) {
            scales[j] += dir*0.1f
            if(Math.abs(scales[j]-prevScale) > 1) {
                scales[j] = prevScale + dir
                dir = 0f
                j += jDir
                if(j == scales.size || j == -1) {
                    jDir *= -1
                    j += jDir
                    prevScale = scales[j]
                    dir = 0f
                    stopcb(prevScale)
                }
            }
        }
        fun startUpdating(startcb:()->Unit) {
            if(dir == 0f) {
                dir = 1 - 2*prevScale
                startcb()
            }
        }
    }
}