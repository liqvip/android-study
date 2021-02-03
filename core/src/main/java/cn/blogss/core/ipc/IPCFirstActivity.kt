package cn.blogss.core.ipc

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import cn.blogss.core.R
import cn.blogss.core.base.BaseActivity
import cn.blogss.core.ipc.aidl.Book
import cn.blogss.core.ipc.aidl.BookManagerService
import cn.blogss.core.ipc.aidl.IBookManager
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*

class IPCFirstActivity : BaseActivity(), View.OnClickListener {
    private lateinit var btGo: AppCompatButton

    private lateinit var iBookManager: IBookManager

    companion object {
        private const val TAG = "IPCFirstActivity"
    }

    private val mConnection = object: ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iBookManager = IBookManager.Stub.asInterface(service)   // 获取 binder 代理类
            try {
                // 查询服务端图书列表
                var list = iBookManager.bookList
                Log.i(TAG, "Query book list, list type: "+list.javaClass.canonicalName)
                Log.i(TAG, "Query book list: "+ list[list.size-1].bookName)
                // 向服务端添加图书
                iBookManager.addBook(Book(3,"Android 开发艺术探索"))
                list = iBookManager.bookList
                Log.i(TAG, "Query book list: "+ list[list.size-1].bookName)
            } catch (e: RemoteException){
                e.printStackTrace()
            }
        }

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
        val intent = Intent(this, BookManagerService::class.java)
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bt_go_second -> {
                startActivity(Intent(this,IPCSecondActivity::class.java))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mConnection)
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