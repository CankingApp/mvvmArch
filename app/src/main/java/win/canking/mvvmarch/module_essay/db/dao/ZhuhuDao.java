package win.canking.mvvmarch.module_essay.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import win.canking.mvvmarch.module_essay.db.entity.ZhihuItemEntity;

/**
 * Created by changxing on 2017/12/5.
 */
@Dao
public interface ZhuhuDao {
    @Query("SELECT * FROM zhuhulist  order by id desc, id limit 0,1")
    LiveData<ZhihuItemEntity> loadZhuhu();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(ZhihuItemEntity products);
}
