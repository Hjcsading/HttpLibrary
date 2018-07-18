# HttpLibrary
网络加载module, retrofit+okhttp+rxjava，有日志输出，加载图为西瓜，可自己修改颜色啥的，很快会加上二级缓存

使用方式如下：
0、更改ParamsUtil中的mBaseUrl内容，一半为接口地址的固定头部

1、创建一个interface文件，用于声明和管理接口信息,如：
        
public interface TestRequest {
    @GET("/area/query_area.json")
    Observable<BaseWebDto<WebProvinceDto>> getProvinces(@Query("areaId") String areaId);
}

2、创建请求对象，如：
        
public class GetProvinceRequest extends BaseRequest<WebProvinceDto>{
    private String mAreaId;

    public GetProvinceRequest(RxAppCompatActivity activity, HttpCallback httpCallback, String areaId) {
        super(activity, httpCallback);
        mAreaId = areaId;
    }

    @Override
    public Observable call(Retrofit retrofit) {
        TestRequest request = retrofit.create(TestRequest.class);
        return request.getProvinces(mAreaId);
    }
}

3、声明回调，网络调用：
        
 GetProvinceRequest request = new GetProvinceRequest(this, new HttpCallback<WebProvinceDto>() {
            @Override
            public void onNext(WebProvinceDto o) {
            }

            @Override
            public void onError(String e) {
            }
        }, null);
  HttpManager.getInstance().request(request);
