package com.zc.baselibrary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.zc.baselibrary.utils.NetWorkUtils
import com.zc.baselibrary.utils.ToastUtils
import com.zc.baselibrary.view.SimpleLoadDialog
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import java.lang.ref.WeakReference

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
abstract class HBaseActivity : RxAppCompatActivity() {

    private var simpleLoadDialog: SimpleLoadDialog? = null

    /**
     * 布局
     */
    abstract fun getContentViewId(): Int
    private var weakReference: WeakReference<Context?>? = null
    private val bundleTag: String = "bundletag"
    private fun initBeforeSetContentView() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBeforeSetContentView()
        setContentView(getContentViewId())
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    /**
     * 线程调度
     */
    protected fun <T> compose(lifecycle: LifecycleTransformer<T?>): ObservableTransformer<T?, T?> {
        return ObservableTransformer<T?, T?> { observable ->
            observable
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(Consumer<Disposable?> {
                    @Throws(Exception::class)
                    fun accept(disposable: Disposable) {
                        // 可添加网络连接判断等
                        if (!NetWorkUtils.isNetworkAvailable()) {
                            ToastUtils.toastShow("网络连接异常，请检查网络")
                        } else {
                            getLoadingDialog().show()
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle)
        }
    }

    private fun getLoadingDialog(): SimpleLoadDialog {
        if (simpleLoadDialog == null) {
            simpleLoadDialog = SimpleLoadDialog(this, true)
        }
        return simpleLoadDialog!!
    }

    override fun onStop() {
        super.onStop()
        if (simpleLoadDialog != null) {
            simpleLoadDialog!!.dismiss()
        }
    }

    fun jumpActivity(s: Class<*>?) {
        startActivity(Intent(this, s))
    }

    fun jumpWithBundle(bundle: Bundle?, s: Class<*>?) {
        val intent: Intent = Intent(this, s)
        intent.putExtra(bundleTag, bundle)
        startActivity(intent)
    }

    fun getBundle(): Bundle? {
        return intent.getBundleExtra(bundleTag)
    }

    fun getWeakContext(): Context? {
        if (weakReference == null) {
            weakReference = WeakReference(this)
        }
        if (weakReference?.get() == null) {
            return this
        }
        return weakReference?.get()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}