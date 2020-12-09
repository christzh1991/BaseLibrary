package com.hh.hlibrary

import android.content.Context

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
abstract class HBaseActivity constructor() : RxAppCompatActivity() {
    protected var simpleLoadDialog: SimpleLoadDialog? = null

    /**
     * 布局
     */
    abstract fun getContentViewId(): Int
    protected var weakReference: WeakReference<Context?>? = null
    private val bundleTag: String? = "bundletag"
    protected fun initBeforeSetContview() {}
    @Override
    protected fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBeforeSetContview()
        setContentView(getContentViewId())
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    /**
     * 线程调度
     */
    protected fun <T> compose(lifecycle: LifecycleTransformer<T?>?): ObservableTransformer<T?, T?>? {
        return object : ObservableTransformer<T?, T?>() {
            @Override
            fun apply(observable: Observable<T?>?): ObservableSource<T?>? {
                return observable
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(object : Consumer<Disposable?>() {
                        @Override
                        @Throws(Exception::class)
                        fun accept(disposable: Disposable?) {
                            // 可添加网络连接判断等
                            if (!NetWorkUtils.isNetworkAvailable()) {
                                ToastUtils.toastShow("网络连接异常，请检查网络")
                            } else {
                                getzLoadingDialog().show()
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycle)
            }
        }
    }

    fun getzLoadingDialog(): SimpleLoadDialog? {
        if (simpleLoadDialog == null) {
            simpleLoadDialog = SimpleLoadDialog(this, true)
        }
        return simpleLoadDialog
    }

    @Override
    protected fun onStop() {
        super.onStop()
        if (simpleLoadDialog != null) {
            simpleLoadDialog.dismiss()
        }
    }

    fun jumpActivity(s: Class?) {
        startActivity(Intent(this, s))
    }

    fun jumpWithBundle(bundle: Bundle?, s: Class?) {
        val intent: Intent? = Intent(this, s)
        intent.putExtra(bundleTag, bundle)
        startActivity(intent)
    }

    fun getBundle(): Bundle? {
        return getIntent().getBundleExtra(bundleTag)
    }

    fun getWeakContext(): Context? {
        if (weakReference == null) {
            weakReference = WeakReference<Context?>(this)
        }
        if (weakReference.get() == null) {
            return this
        }
        return weakReference.get()
    }

    @Override
    protected fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}