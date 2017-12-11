package win.canking.mvvmarch.module_essay.net;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.WorkerThread;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import win.canking.mvvmarch.architecture.IRequestApi;
import win.canking.mvvmarch.module_essay.db.entity.ZhihuItemEntity;
import win.canking.mvvmarch.net.NetConstant;

/**
 * Created by changxing on 2017/12/4.
 */

public class NetEngine {
    private Retrofit mRetrofit;
    private volatile NetEngine mInstance;

    private static class Holder {
        static NetEngine netEngine = new NetEngine();
    }


    private NetEngine() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(NetConstant.URL_BASE)
                .client(getFreeClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private OkHttpClient getFreeClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        X509TrustManager[] trustManager = new X509TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws
                            CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws
                            CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManager, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            if (sslSocketFactory != null)
                builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    public static NetEngine getInstance() {
        return Holder.netEngine;
    }

    @WorkerThread
    public <ResultType> LiveData<IRequestApi<ResultType>> getEssay(@EssayWebService.EssayType String type) throws IOException {
        EssayWebService api = mRetrofit.create(EssayWebService.class);

        Call<ZhihuItemEntity> essayCall = api.getZhihuList("latest");
        MediatorLiveData<IRequestApi<ResultType>> result = new MediatorLiveData<>();
        final Response<ZhihuItemEntity> response = essayCall.execute();

        IRequestApi<ResultType> requestApi = new IRequestApi<ResultType>() {
            @Override
            public ResultType getBody() {
                ZhihuItemEntity entity = response.body();
                return (ResultType) entity;
            }

            @Override
            public String getErrorMsg() {
                return response.message();
            }

            @Override
            public boolean isSuccessful() {
                return response.isSuccessful();
            }
        };
        result.postValue(requestApi);


        return result;
    }


}
