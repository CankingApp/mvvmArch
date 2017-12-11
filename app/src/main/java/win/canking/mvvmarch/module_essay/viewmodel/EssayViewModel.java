package win.canking.mvvmarch.module_essay.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import win.canking.mvvmarch.architecture.Resource;
import win.canking.mvvmarch.module_essay.db.entity.ZhihuItemEntity;
import win.canking.mvvmarch.module_essay.repsitory.EssayRepository;

/**
 * Created by changxing on 2017/12/4.
 */

public class EssayViewModel extends AndroidViewModel {
    private EssayRepository mRepository;
    private MediatorLiveData<Resource<ZhihuItemEntity>> mCache;

    public EssayViewModel(Application app) {
        super(app);
        mRepository = new EssayRepository(app);
    }

    public LiveData<Resource<ZhihuItemEntity>> getEssayData() {
        if (mCache == null) {
            mCache = mRepository.loadEssayData();
        }
        return mCache;
    }

    public void updateCache() {
        final LiveData<Resource<ZhihuItemEntity>> update = mRepository.update();
        mCache.addSource(update, new Observer<Resource<ZhihuItemEntity>>() {
            @Override
            public void onChanged(@Nullable Resource<ZhihuItemEntity> zhihuItemEntityResource) {
                mCache.setValue(zhihuItemEntityResource);
            }
        });

    }

    public void addMore() {
        //TODO: 加载更多
    }

}
