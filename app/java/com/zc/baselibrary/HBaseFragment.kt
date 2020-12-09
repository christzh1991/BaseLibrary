package com.hh.hlibrary

import android.app.Activity

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
abstract class HBaseFragment constructor() : RxFragment() {
    protected var simpleLoadDialog: SimpleLoadDialog? = null
    protected var bundleTag: String? = "bundValue"
    @Override
    fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            val isSupportHidden: Boolean = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN)
            val ft: FragmentTransaction? = getFragmentManager().beginTransaction()
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

    @Override
    fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden())
    }

    @Override
    fun onDestroy() {
        super.onDestroy()
    }

    fun getzLoadingDialog(): SimpleLoadDialog? {
        if (simpleLoadDialog == null) {
            simpleLoadDialog = SimpleLoadDialog(getActivity(), true)
        }
        return simpleLoadDialog
    }

    fun showLoadingDialog() {
        try {
            getzLoadingDialog().show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun dismissLoadingDialog() {
        try {
            if (simpleLoadDialog != null) {
                simpleLoadDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showToast(msg: String?) {
        if (getActivity() != null) {
            Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun showToast(strId: Int) {
        if (getActivity() != null) {
            Toast.makeText(
                getActivity().getApplicationContext(),
                getActivity().getApplicationContext().getResources().getText(strId),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * 公共跳转方法
     *
     * @return
     */
    protected fun jumpActivity(targetActivity: Class<out Activity?>?) {
        val intent: Intent? = Intent(getActivity(), targetActivity)
        startActivity(intent)
        //        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    fun onDestroyView() {
        super.onDestroyView()
    }

    protected fun scaleAnimation(view: View?, duration: Int, scaleX: Float, scaleY: Float) {
        ViewCompat.animate(view)
            .setDuration(duration)
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