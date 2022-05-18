package cn.blogss.core.test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.blogss.core.databinding.ActivityTestBinding
import cn.blogss.helper.base.jetpack.BaseActivity
import cn.blogss.helper.base.jetpack.BaseViewModel

/**
 * @author: Little Bei
 * @Date: 2022/2/16
 */

class MyViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
}

class MyViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
}


abstract class MyAdapter<T>(val data: ArrayList<T>, val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder1 {
        val itemView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return MyViewHolder1(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        convert(holder, position, data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(position % 2 == 0){
            android.R.layout.simple_list_item_1
        }else{
            android.R.layout.simple_list_item_2
        }
    }

    abstract fun convert(holder: RecyclerView.ViewHolder, position: Int, itemData: T)

}

class TestActivity: BaseActivity<ActivityTestBinding, BaseViewModel>() {

    companion object {
        private const val TAG = "TestActivity"
    }

    val myData = arrayListOf("0", "1", "2", "3", "4", "5")

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityTestBinding {
        return ActivityTestBinding.inflate(inflater)
    }

    override fun initView() {
    }

    override fun bindObserver() {
    }

    override fun initData() {
    }

/*    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: one params, savedInstanceState = $savedInstanceState")
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.i(TAG, "onCreate: savedInstanceState = $savedInstanceState, persistentState = $persistentState")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState: outState = $outState")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.i(TAG, "onSaveInstanceState: outState = $outState, outPersistentState = $outPersistentState")
    }

    override fun onStateNotSaved() {
        super.onStateNotSaved()
        Log.i(TAG, "onStateNotSaved: ")
    }

    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return super.onRetainCustomNonConfigurationInstance()
        Log.i(TAG, "onRetainCustomNonConfigurationInstance: ")
    }*/

}

