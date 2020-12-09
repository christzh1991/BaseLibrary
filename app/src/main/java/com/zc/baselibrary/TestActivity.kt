package com.zc.baselibrary

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