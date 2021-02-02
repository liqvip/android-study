package cn.blogss.core.ipc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import cn.blogss.core.R
import cn.blogss.core.base.BaseActivity
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class IPCFirstActivity : BaseActivity(), View.OnClickListener {
    private lateinit var btGo: AppCompatButton

    companion object {
        private const val TAG = "IPCFirstActivity"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_ipc_first
    }

    override fun initView() {
        btGo = findViewById(R.id.bt_go_second)
        btGo.setOnClickListener(this)

        Log.d(TAG, "sUserId: "+User.sUserID)
        User.sUserID++
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTag(TAG)
        super.onCreate(savedInstanceState)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bt_go_second -> {
                startActivity(Intent(this,IPCSecondActivity::class.java))
            }
        }
    }

    fun serializeUser(): Unit{
        val userSerial = UserSerial(1,"001",false)
        val out = ObjectOutputStream(FileOutputStream("serial.txt"))
        out.writeObject(userSerial)
        out.close()
    }

    /**
     * 反序列化恢复后的对象和原对象的内容是一样的，但两者并不是同一个对象
     * @return Unit
     */
    fun deSerializeUser(): Unit{
        val ins = ObjectInputStream(FileInputStream("serial.txt"))
        val user = ins.readObject() as UserSerial
        ins.close()
    }
}