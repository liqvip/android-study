package cn.blogss.core.window

import android.view.View
import android.view.WindowManager
import android.widget.Button
import cn.blogss.core.R
import cn.blogss.core.base.BaseActivity
import cn.blogss.helper.createWindow
import cn.blogss.helper.delWindow

class MainActivity : BaseActivity(), View.OnClickListener {
    private lateinit var btAppCreate : Button
    private lateinit var btSubCreate : Button
    private lateinit var btSysCreate : Button
    private lateinit var btDel : Button

    private lateinit var floatingAppButton: Button
    private lateinit var floatingSubButton: Button
    private lateinit var floatingSysButton: Button

    private var windowViewList = mutableListOf<Button>()

    override fun getLayoutId(): Int {
        return R.layout.activity_window
    }

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
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bt_create_application_window -> {
                if(windowViewList.contains(floatingAppButton))
                    return
                windowViewList.add(floatingAppButton)
                floatingAppButton.text = "应用 Window"
                createWindow(floatingAppButton,200,300,WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW,this)
            }
            R.id.bt_create_sub_window -> {
                if(windowViewList.contains(floatingSubButton))
                    return
                windowViewList.add(floatingSubButton)
                floatingSubButton.text = "子 Window"
                createWindow(floatingSubButton,200,300,WindowManager.LayoutParams.FIRST_SUB_WINDOW,this)
            }
            R.id.bt_create_sys_window -> {
                if(windowViewList.contains(floatingSysButton))
                    return
                windowViewList.add(floatingSysButton)
                floatingSysButton.text = "系统 Window"
                createWindow(floatingSysButton,200,300,WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,this)
            }
            R.id.bt_del_window -> {
                windowViewList.forEach {
                    delWindow(it,this)
                }
                windowViewList.clear()
            }
        }
    }
}