package cn.blogss.jetpack.viewmodel

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelActivity: AppCompatActivity() {
    private lateinit var vm: ViewModel

    companion object {
        private const val TAG = "ViewModelActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")

        vm = ViewModelProvider(this).get(MyViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
    }

    class MyViewModel: ViewModel() {

    }

}