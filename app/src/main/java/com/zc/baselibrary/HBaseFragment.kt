package com.zc.baselibrary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentTransaction
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.components.support.RxFragment
import com.zc.baselibrary.utils.NetWorkUtils
import com.zc.baselibrary.utils.ToastUtils
import com.zc.baselibrary.view.SimpleLoadDialog
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
abstract class HBaseFragment : RxFragment() {

    private var simpleLoadDialog: SimpleLoadDialog? = null
    protected var bundleTag: String? = "bundValue"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            val isSupportHidden: Boolean = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN)
            val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
            if (isSupportHidden) {
                ft.hide(this)
            } else {
                ft.show(this)
            }
            ft.commit()
        }
    }

    /**
     * 线程调度
     */
    protected fun <T> compose(lifecycle: LifecycleTransformer<T?>?): ObservableTransformer<T?, T?> {
        return ObservableTransformer<T?, T?> { observable ->
            observable
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { // 可添加网络连接判断等
                    if (!NetWorkUtils.isNetworkAvailable()) {
                        ToastUtils.toastShow("网络连接异常，请检查网络")
                    } else {
                        getLoadingDialog().show()
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun getLoadingDialog(): SimpleLoadDialog {
        if (simpleLoadDialog == null) {
            simpleLoadDialog = SimpleLoadDialog(activity, true)
        }
        return simpleLoadDialog!!
    }

    fun showLoadingDialog() {
        try {
            getLoadingDialog().show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun dismissLoadingDialog() {
        simpleLoadDialog?.dismiss()
    }

    fun showToast(msg: String?) {
        if (activity != null) {
            Toast.makeText(activity!!.applicationContext, msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun showToast(strId: Int) {
        if (activity != null) {
            Toast.makeText(
                activity!!.applicationContext,
                activity!!.applicationContext.resources.getText(strId),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * 公共跳转方法
     *
     * @return
     */
    protected fun jumpActivity(targetActivity: Class<out Activity?>) {
        val intent: Intent = Intent(activity, targetActivity)
        startActivity(intent)
        //        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public override fun onDestroyView() {
        super.onDestroyView()
    }

    protected fun scaleAnimation(view: View, duration: Int, scaleX: Float, scaleY: Float) {
        ViewCompat.animate(view)
            .setDuration(duration.toLong())
            .scaleX(scaleX)
            .scaleY(scaleY)
            .start()
    }

    /**
     * 公共跳转方法
     *
     * @return
     */
    //    protected void jumpBundleActivity(Class<? extends Activity> targetActivity, Bundle bundle) {
    //        Intent intent = new Intent(getActivity(), targetActivity);
    //        intent.putExtra(bundleTag, bundle);
    //        startActivityForResult(intent, REQUEST_CODE_TAG);
    //        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    //    }
    companion object {
        private val STATE_SAVE_IS_HIDDEN: String? = "STATE_SAVE_IS_HIDDEN"
    }
}