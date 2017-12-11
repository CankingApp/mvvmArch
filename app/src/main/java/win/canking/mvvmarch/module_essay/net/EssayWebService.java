package win.canking.mvvmarch.module_essay.net;

import android.support.annotation.StringDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import win.canking.mvvmarch.module_essay.db.entity.ZhihuItemEntity;


/**
 * Created by changxing on 2017/12/4.
 */

public interface EssayWebService {
    public static final String DAY = "today";
    public static final String RANDOM = "random";


    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.SOURCE)
    @StringDef(value = {DAY, RANDOM})
    @interface EssayType {
    }


    @GET("article/{type}?dev=1")
    Call<ResponseBody> getEssay(@EssayType @Path("type") String type);

    @GET("news/{type}")
    Call<ZhihuItemEntity> getZhihuList(@Path("type") String type);

    @GET("users")
    Call<ResponseBody> getTest();
}
