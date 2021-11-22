package cn.blogss.core.window

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import cn.blogss.core.R
import cn.blogss.helper.base.BaseActivity
import cn.blogss.helper.createWindow
import cn.blogss.helper.delWindow
import cn.blogss.helper.getWindowLayoutParamsInstance
import cn.blogss.helper.updateWindow

class MainActivity : BaseActivity(), View.OnClickListener, View.OnTouchListener {
    private lateinit var btAppCreate: Button
    private lateinit var btSubCreate: Button
    private lateinit var btSysCreate: Button
    private lateinit var btDel: Button

    private lateinit var floatingAppButton: Button
    private lateinit var floatingSubButton: Button
    private lateinit var floatingSysButton: Button

    private var windowViewList = mutableListOf<Button>()

    private var layoutParamsAppType = getWindowLayoutParamsInstance(200,300,WindowManager.LayoutParams.TYPE_APPLICATION)
    private var layoutParamsSubType = getWindowLayoutParamsInstance(200,300,WindowManager.LayoutParams.FIRST_SUB_WINDOW)
    private var layoutParamsSysType = getWindowLayoutParamsInstance(200,300,WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)

    override fun getLayoutId(): Int {
        return R.layout.activity_window
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        btAppCreate = findViewById(R.id.bt_create_application_window)
        btSubCreate = findViewById(R.id.bt_create_sub_window)
        btSysCreate = findViewById(R.id.bt_create_sys_window)
        btDel = findViewById(R.id.bt_del_window)

        btAppCreate.setOnClickListener(this)
        btSubCreate.setOnClickListener(this)
        btSysCreate.setOnClickListener(this)
        btDel.setOnClickListener(this)

        floatingAppButton = Button(this)
        floatingSubButton = Button(this)
        floatingSysButton = Button(this)
        floatingAppButton.setOnTouchListener(this)
        floatingSubButton.setOnTouchListener(this)
        floatingSysButton.setOnTouchListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bt_create_application_window -> {
                if(windowViewList.contains(floatingAppButton))
                    return
                windowViewList.add(floatingAppButton)
                floatingAppButton.text = "应用 Window"
                createWindow(floatingAppButton,layoutParamsAppType,this)
            }
            R.id.bt_create_sub_window -> {
                if(windowViewList.contains(floatingSubButton))
                    return
                windowViewList.add(floatingSubButton)
                floatingSubButton.text = "子 Window"
                createWindow(floatingSubButton,layoutParamsSubType,this)
            }
            R.id.bt_create_sys_window -> {
                if(windowViewList.contains(floatingSysButton))
                    return
                windowViewList.add(floatingSysButton)
                floatingSysButton.text = "系统 Window"
                createWindow(floatingSysButton,layoutParamsSysType,this)
            }
            R.id.bt_del_window -> {
                windowViewList.forEach {
                    delWindow(it,this)
                }
                windowViewList.clear()
            }
        }
    }

    /**
     * 实现 window 的拖动，拦截 ACTION_MOVE 事件更新 Window 的 layoutParams 即可
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val rawX = event!!.rawX.toInt()
        val rawY = event.rawY.toInt()
        when(event.action){
            MotionEvent.ACTION_MOVE -> {
                when(v){/*不能用 v.id 判断，都为 -1。 并不是从布局文件中获取的 View 实例*/
                    floatingAppButton -> {
                        layoutParamsAppType.x = rawX
                        layoutParamsAppType.y = rawY
                        updateWindow(v, this, layoutParamsAppType)
                    }
                    floatingSubButton -> {
                        layoutParamsSubType.x = rawX
                        layoutParamsSubType.y = rawY
                        updateWindow(v, this, layoutParamsSubType)
                    }
                    floatingSysButton -> {
                        layoutParamsSysType.x = rawX
                        layoutParamsSysType.y = rawY
                        updateWindow(v, this, layoutParamsSysType)
                    }
                }
                /**
                 * 返回 true，则 onTouchEvent 不会调用
                 */
                return true
            }
        }
        return false
    }
}