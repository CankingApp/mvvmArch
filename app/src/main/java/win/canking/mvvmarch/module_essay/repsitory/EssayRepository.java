package win.canking.mvvmarch.module_essay.repsitory;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import win.canking.mvvmarch.AppExecutors;
import win.canking.mvvmarch.BasicApp;
import win.canking.mvvmarch.architecture.AbsDataSource;
import win.canking.mvvmarch.architecture.AbsRepository;
import win.canking.mvvmarch.architecture.IRequestApi;
import win.canking.mvvmarch.architecture.Resource;
import win.canking.mvvmarch.module_essay.db.dao.ZhuhuDao;
import win.canking.mvvmarch.module_essay.db.entity.ZhihuItemEntity;
import win.canking.mvvmarch.module_essay.model.Essay;
import win.canking.mvvmarch.module_essay.net.NetEngine;

import static win.canking.mvvmarch.module_essay.net.EssayWebService.DAY;

/**
 * Created by changxing on 2017/12/3.
 */

public class EssayRepository extends AbsRepository {
    private NetEngine webService;
    private ZhuhuDao zhuhuDao;
    private AppExecutors executor;

    public EssayRepository(Application app) {
        webService = NetEngine.getInstance();
        zhuhuDao = ((BasicApp) app).getDatabase().zhuhuDao();
        this.executor = ((BasicApp) app).getAppExecutors();
    }


    public LiveData<Resource<? extends Essay>> load() {
        return null;
    }

    public MediatorLiveData<Resource<ZhihuItemEntity>> update() {
        return loadEssayData();
    }

    public MediatorLiveData<Resource<ZhihuItemEntity>> loadEssayData() {
        return new AbsDataSource<ZhihuItemEntity, ZhihuItemEntity>() {

            @Override
            protected void saveCallResult(@NonNull ZhihuItemEntity item) {
                zhuhuDao.insertItem(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable ZhihuItemEntity data) {
                //TODO：Realize your own logic
                return true;
            }

            @NonNull
            @Override
            protected LiveData<ZhihuItemEntity> loadFromDb() {
                LiveData<ZhihuItemEntity> entity = zhuhuDao.loadZhuhu();
                return entity;
            }

            @NonNull
            @Override
            protected LiveData<IRequestApi<ZhihuItemEntity>> createCall() {
                final MediatorLiveData<IRequestApi<ZhihuItemEntity>> result = new MediatorLiveData<>();

                executor.networkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            LiveData<IRequestApi<ZhihuItemEntity>> netGet = webService.getEssay(DAY);
                            result.addSource(netGet, new Observer<IRequestApi<ZhihuItemEntity>>() {
                                @Override
                                public void onChanged(@Nullable IRequestApi<ZhihuItemEntity> essayDayEntityIRequestApi) {
                                    result.postValue(essayDayEntityIRequestApi);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            onFetchFailed();
                        }
                    }
                });


                return result;
            }

            @Override
            protected void onFetchFailed() {
                //TODO: update the UI
            }
        }.getAsLiveData();
    }

}
