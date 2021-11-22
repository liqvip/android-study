package cn.blogss.frame.permissionx

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import cn.blogss.frame.R
import cn.blogss.helper.base.BaseActivity
import com.permissionx.guolindev.PermissionX

/**
 * @description 郭霖 PermissionX 框架使用方法
 * @author LiQiang
 * @date 2020/12/4
 */
class PermissionXActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mUse1: Button
    private lateinit var mUse2: Button
    private lateinit var mUse3: Button

    companion object {
        private const val TAG = "PermissionXActivity"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_permissionx
    }

    override fun initView() {
        mUse1 = findViewById(R.id.bt_use1)
        mUse2 = findViewById(R.id.bt_use2)
        mUse3 = findViewById(R.id.bt_use3)

        mUse1.setOnClickListener(this)
        mUse2.setOnClickListener(this)
        mUse3.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTag(TAG)
        super.onCreate(savedInstanceState)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.bt_use1 -> {
                /**
                 * 第一种用法，用户直接选择允许或者拒绝。
                 */
                PermissionX.init(this).permissions(mutableListOf(Manifest.permission.CALL_PHONE))
                    .request { allGranted, grantedList, deniedList ->
                        if(allGranted)
                            Toast.makeText(this,"您允许了拨打电话权限",Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(this,"您拒绝了拨打电话权限",Toast.LENGTH_SHORT).show()
                }
            }
            R.id.bt_use2 -> {
                /**
                 * 第二种用法，用户拒绝了某个权限，在下次申请前弹出一个对话框向用户解释申请这个权限的原因
                 * 这种情况下，所有被用户拒绝的权限会优先进入onExplainRequestReason()方法进行处理，拒绝的权限都记录在deniedList参数当中。
                 * 接下来，我们只需要在这个方法中调用showRequestReasonDialog()方法，即可弹出解释权限申请原因的对话框
                 */
                PermissionX.init(this).permissions(mutableListOf(Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA,Manifest.permission.READ_CONTACTS))
                    .onExplainRequestReason {scope, deniedList ->
                        scope.showRequestReasonDialog(deniedList,"即将重新申请的权限是程序必须依赖的权限","我已明白","取消")
                    }
                    .request { allGranted, grantedList, deniedList ->
                        if(allGranted)
                            Toast.makeText(this,"所有申请的权限都已通过",Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(this,"您拒绝了如下权限：$deniedList",Toast.LENGTH_SHORT).show()
                    }
            }
            R.id.bt_use3 -> {
                /**
                 * 第三种用法，用户不理会我们的解释，仍然执意拒绝权限申请，并且还选择了拒绝且不再询问的选项
                 * PermissionX中还提供了一个onForwardToSettings()方法，专门用于监听那些被用户永久拒绝的权限
                 * 所有被用户选择了拒绝且不再询问的权限都会进行到这个方法中处理，拒绝的权限都记录在deniedList参数当中
                 * 你并不需要自己弹出一个Toast或是对话框来提醒用户手动去应用程序设置当中打开权限，而是直接调用showForwardToSettingsDialog()方法即可
                 */
                PermissionX.init(this).permissions(mutableListOf(Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA,Manifest.permission.READ_CONTACTS))
                    .onForwardToSettings { scope, deniedList ->
                        scope.showForwardToSettingsDialog(deniedList,"您需要去应用程序设置当中手动开启权限", "我已明白", "取消")
                    }
                    .onExplainRequestReason {scope, deniedList ->
                        scope.showRequestReasonDialog(deniedList,"即将重新申请的权限是程序必须依赖的权限","我已明白","取消")
                    }
                    .request { allGranted, grantedList, deniedList ->
                        if(allGranted)
                            Toast.makeText(this,"所有申请的权限都已通过",Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(this,"您拒绝了如下权限：$deniedList",Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}