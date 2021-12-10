package cn.blogss.android_study.discovery.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View

class XferView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    lateinit var paint: Paint
    lateinit var rectF: RectF
    lateinit var xFerMode: PorterDuffXfermode
    lateinit var innerPath: Path
    lateinit var outerPath: Path

    private val radii = floatArrayOf(50f, 50f, 50f, 50f, 50f, 50f, 50f, 50f)
    
    companion object {
        private const val TAG = "XferView"
    }

    init {
        init()
    }

    private fun init() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL
        rectF = RectF()
        xFerMode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        innerPath = Path()
        outerPath = Path()
    }

    override fun draw(canvas: Canvas) {
        canvas.saveLayer(0f,0f, width.toFloat(),height.toFloat(), null)
        super.draw(canvas)


        paint.color = Color.parseColor("blue")
        canvas.drawRect(0f, 0f,width / 2f ,height / 2f, paint)

        /*paint.color = Color.parseColor("red")
        canvas.drawCircle(rect.right.toFloat(), rect.bottom.toFloat(), 100f, paint)*/
        paint.xfermode = xFerMode
        rectF.set(0f, 0f, width.toFloat(), height.toFloat())
        outerPath.addRect(rectF, Path.Direction.CCW)
        innerPath.addRoundRect(rectF, radii, Path.Direction.CCW)
        outerPath.op(innerPath, Path.Op.DIFFERENCE)
        canvas.drawPath(outerPath, paint)
        paint.xfermode = null

        canvas.restore()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.i(TAG, "onSizeChanged: w = $w, h = $h, oldw = $oldw, oldh = $oldh")
    }

    
}


