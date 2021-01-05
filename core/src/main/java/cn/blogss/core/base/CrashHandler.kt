package cn.blogss.core.base

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Process
import android.util.Log
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author: Thatcher Li
 * @Date: 2021/1/5
 * @LastEditors: Thatcher Li
 * @LastEditTime: 2021/1/5
 * @Descripttion: 应用崩溃处理类，收集崩溃信息
 */
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {
    private lateinit var mContext: Context

    private lateinit var mDefaultCrashHandler: Thread.UncaughtExceptionHandler

    companion object {
        private const val TAG = "CrashHandler"
        private const val DEBUG = true
        var PATH = Environment.getExternalStorageDirectory().path + "/crash/log/"
        private const val FILE_NAME = "crash"
        private const val FILE_NAME_SUFFIX = ".trace"
        private val mInstance = CrashHandler()

        fun getInstance(): CrashHandler{
            return mInstance
        }
    }

    fun init(context: Context){
        mContext = context.applicationContext
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        try {
            // 导出异常信息到 sd 卡中
            dumpExceptionToSDCard(e)
            // 这里可以上传异常信息到服务器,便于开发人员分析日志从而解决bug
            uploadExceptionToServer()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        e.printStackTrace()

        // 如果系统提供了默认的异常处理器，则交给系统去结束程序，否则就由自己结束自己
        if(mDefaultCrashHandler != null){
            mDefaultCrashHandler.uncaughtException(t,e)
        }else{
            Process.killProcess(Process.myPid())
        }
    }

    private fun uploadExceptionToServer() {

    }

    private fun dumpExceptionToSDCard(e: Throwable) {
        if(Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED){
            if (DEBUG){
                Log.w(TAG, "sdcard unmounted, skip dump exception")
                return
            }
        }

        val dir = File(PATH)
        if(!dir.exists()){
            dir.mkdirs()
        }
        val current = System.currentTimeMillis()
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(current))
        val file = File(PATH+ FILE_NAME+time+ FILE_NAME_SUFFIX)

        val pw = PrintWriter(BufferedWriter(FileWriter(file)))
        pw.println(time)
        dumpPhoneInfo(pw)
        pw.println()
        e.printStackTrace(pw)
        pw.close()
    }

    private fun dumpPhoneInfo(pw: PrintWriter) {
        val pm = mContext.packageManager
        val pi = pm.getPackageInfo(mContext.packageName,PackageManager.GET_ACTIVITIES)
        pw.print("APP Version: ")
        pw.print(pi.versionName)
        pw.print("_")
        pw.println(pi.versionCode)

        // Android 版本号
        pw.print("OS Version: ")
        pw.print(Build.VERSION.RELEASE)
        pw.print("_")
        pw.println(Build.VERSION.SDK_INT)

        // 手机制造商
        pw.print("Vendor: ")
        pw.println(Build.MANUFACTURER)

        // 手机型号
        pw.print("Model: ")
        pw.println(Build.MODEL)

        // CPU 架构
        pw.print("CPU ABI: ")
        pw.println(Build.CPU_ABI)
    }
}