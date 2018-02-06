package ui.anwesome.com.hlineview

/**
 * Created by anweshmishra on 06/02/18.
 */
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.*
class HLineView(ctx:Context):View(ctx) {
    val renderer = HLineRenderer(this)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
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
            canvas.translate(-w/8,0f)
            canvas.rotate(-90f*scales[0])
            canvas.drawLine(0f,0f,w/8,0f,paint)
            canvas.restore()
            if (j >= 1) {
                canvas.save()
                canvas.translate(0 - w / 8, 0 - w / 8)
                canvas.rotate(180f*(1-scales[1]))
                canvas.drawLine(0f, 0f, 0f, -w / 8, paint)
                canvas.restore()
            }
            if(j >= 2) {
                canvas.save()
                canvas.translate(0 - w / 8, 0f)
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
    data class HLineState(var j:Int = 0,var jDir:Int = 1,var dir:Float = 0f,var prevScale:Float = 0f) {
        val scales:Array<Float> = arrayOf(0f,0f,0f)
        fun update(stopcb:(Float)->Unit) {
            scales[j] += dir*0.1f
            if(Math.abs(scales[j]-prevScale) > 1) {
                scales[j] = prevScale + dir
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
    data class HLineContainer(var w:Float,var h:Float,var hlineJoint:HLineJoint = HLineJoint(w,h)) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            for(i in 0..3) {
                canvas.save()
                canvas.scale(1f-2*(i%2),1f-2*(i/2))
                hlineJoint.draw(canvas, paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update(stopcb:(Float)->Unit) {
            hlineJoint.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            hlineJoint.startUpdating(startcb)
        }
    }
    data class HLineAnimator(var view:View,var animated:Boolean = false) {
        fun animate(updatecb:()->Unit) {
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
    }
    data class HLineRenderer(var view:HLineView,var time:Int = 0) {
        val animator = HLineAnimator(view)
        var container:HLineContainer?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                container = HLineContainer(w,h)
                paint.color = Color.parseColor("#f44336")
                paint.strokeWidth = Math.min(w,h)/35
                paint.strokeCap = Paint.Cap.ROUND
            }
            canvas.drawColor(Color.parseColor("#212121"))
            container?.draw(canvas,paint)
            time++
            animator.animate {
                container?.update {
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            container?.startUpdating {
                animator.start()
            }
        }
    }
    companion object {
        fun create(activity:Activity):HLineView {
            val view = HLineView(activity)
            activity.setContentView(view)
            return view
        }
    }
}