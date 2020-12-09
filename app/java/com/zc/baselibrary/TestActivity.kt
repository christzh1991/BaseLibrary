package com.hh.hlibrary

import com.google.gson.annotations.SerializedName
import com.zc.baselibrary.http.BaseEntity
import com.zc.baselibrary.http.ExceptionHandle.ResponseException
import com.zc.baselibrary.http.ExceptionHandle.ERROR
import com.zc.baselibrary.http.ExceptionHandle
import com.google.gson.JsonParseException
import org.json.JSONException
import kotlin.jvm.JvmOverloads
import androidx.appcompat.widget.AppCompatButton
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.Drawable
import android.content.res.TypedArray
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import android.widget.Checkable
import android.annotation.TargetApi
import android.os.Build
import com.zc.baselibrary.view.SwitchButton
import com.zc.baselibrary.view.SwitchButton.ViewState
import android.animation.ValueAnimator
import android.view.View.MeasureSpec
import android.view.View.OnLongClickListener
import android.graphics.RectF
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.Gravity
import android.app.Activity
import com.zc.baselibrary.view.SimpleLoadDialog
import com.zc.baselibrary.view.CustomDialog.ClickCallBack
import android.widget.TextView
import android.os.Bundle
import io.reactivex.ObservableTransformer
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlin.Throws
import com.zc.baselibrary.cache.cache.ICache
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.zc.baselibrary.cache.cache.CacheManager
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableEmitter
import com.zc.baselibrary.cache.converter.IConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zc.baselibrary.cache.RxCache
import com.zc.baselibrary.event.BaseEventBean
import com.zc.baselibrary.utils.ACache.ACacheManager
import org.json.JSONObject
import org.json.JSONArray
import com.zc.baselibrary.utils.ACache.xFileOutputStream
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import com.zc.baselibrary.utils.ACache
import com.zc.baselibrary.utils.DirUtils
import android.content.pm.ApplicationInfo
import android.annotation.SuppressLint
import android.net.wifi.WifiManager
import android.net.wifi.WifiInfo
import com.zc.baselibrary.utils.DeviceUtil
import com.zc.baselibrary.utils.CompatUtils
import android.os.StatFs
import com.zc.baselibrary.utils.GsonUtils
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.zc.baselibrary.utils.HLogUtils
import android.telephony.TelephonyManager
import android.os.Build.VERSION
import kotlin.jvm.Synchronized
import android.widget.Toast
import com.zc.baselibrary.utils.ToastUtils
import android.util.DisplayMetrics
import android.view.WindowManager
import com.zc.baselibrary.utils.DisplayUtil
import android.text.Editable
import android.text.TextUtils
import com.zc.baselibrary.utils.NetWorkUtils
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.content.SharedPreferences
import com.zc.baselibrary.utils.AppPreferences
import com.zc.baselibrary.utils.KeyPairGenUtil
import org.apache.commons.codec.binary.Hex
import com.zc.baselibrary.utils.DeviceUuidFactory
import android.provider.Settings.Secure
import android.preference.PreferenceManager
import com.orhanobut.logger.PrettyFormatStrategy
import com.orhanobut.logger.AndroidLogAdapter
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import org.greenrobot.eventbus.EventBus
import com.trello.rxlifecycle3.LifecycleTransformer
import android.content.Intent
import com.zc.baselibrary.HBaseFragment
import androidx.core.view.ViewCompat
import com.hh.hlibrary.http.ExceptionHandle.ResponseException
import com.hh.hlibrary.http.ExceptionHandle.ERROR
import com.hh.hlibrary.http.ExceptionHandle
import com.hh.hlibrary.view.SwitchButton
import com.hh.hlibrary.view.SwitchButton.ViewState
import ValueAnimator.AnimatorUpdateListener
import com.hh.hlibrary.view.SimpleLoadDialog
import com.hh.hlibrary.view.CustomDialog.ClickCallBack
import com.hh.hlibrary.cache.cache.CacheManager
import com.hh.hlibrary.cache.RxCache
import ACache.ACacheManager
import ACache.xFileOutputStream
import com.hh.hlibrary.utils.DirUtils
import com.hh.hlibrary.utils.GsonUtils
import com.hh.hlibrary.utils.HLogUtils
import com.hh.hlibrary.utils.DeviceUtil
import com.hh.hlibrary.utils.ToastUtils
import com.hh.hlibrary.utils.CompatUtils
import com.hh.hlibrary.utils.DisplayUtil
import com.hh.hlibrary.utils.NetWorkUtils
import com.hh.hlibrary.utils.AppPreferences
import com.hh.hlibrary.utils.KeyPairGenUtil
import com.hh.hlibrary.utils.DeviceUuidFactory
import com.hh.hlibrary.HBaseFragment

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class TestActivity constructor() { //
    //    RecyclerView recyclerView;
    //    BaseAdapter baseAdapter;
    //    private List<User> users;
    //
    //    @Override
    //    protected void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        setContentView(R.layout.activity_main);
    //        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    ////                test();
    //            }
    //        });
    //        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
    //        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    //        users=new ArrayList<User>();
    //    }
    //
    //    private void test(){
    //        final Map<String,Object> map=new HashMap<>();
    //        map.put("telephone","18512582957");
    //        map.put("password","123456");
    //        map.put("role","1");
    //        map.put("appDeviceType","2");
    //        map.put("devId","");
    //        Observable<BaseEntity<User>> observable = RetrofitFactory.getInstance().login(map);
    //        observable.compose(compose(this.<BaseEntity<User>>bindToLifecycle()))
    //                .subscribe(new BaseObserver<User>(getzLoadingDialog()) {
    //                               @Override
    //                               protected void onHandleSuccess(User user) {
    //                                   // 保存用户信息等操作
    //                                   Log.v("HLibrary",user.toString());
    //                                   for (int i=0;i<7;i++){
    //                                       users.add(user);
    //                                   }
    //                                    if(baseAdapter==null){
    //                                        baseAdapter=new BaseAdapter(R.layout.list_item_test,users);
    //                                        recyclerView.setAdapter(baseAdapter);
    //                                    }else{
    //                                        baseAdapter.notifyDataSetChanged();
    //                                    }
    //                                   Logger.d(map);
    //                               }
    //
    //                               @Override
    //                               public void onError(Throwable e) {
    //                                   super.onError(e);
    //                               }
    //                           }
    //
    //                );
    //    }
    //
    //    private void test2(){
    //        Map<String,Object> map=new HashMap<>();
    //        map.put("pageNumber","1");
    //        Observable<BaseEntity<List<WSPartyMediation>>> observable = RetrofitFactory.getInstance().queryMyMediation(map);
    //        observable.compose(compose(this.<BaseEntity<List<WSPartyMediation>>>bindToLifecycle()))
    //                .subscribe(new BaseObserver<List<WSPartyMediation>>(getzLoadingDialog()) {
    //                               @Override
    //                               protected void onHandleSuccess(List<WSPartyMediation> user) {
    //                                   Logger.d(user);
    //                               }
    //
    //                               @Override
    //                               public void onError(Throwable e) {
    //                                   super.onError(e);
    //                               }
    //                           }
    //
    //                );
    //    }
}