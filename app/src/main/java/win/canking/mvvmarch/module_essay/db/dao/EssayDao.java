package win.canking.mvvmarch.module_essay.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import win.canking.mvvmarch.module_essay.db.entity.EssayDayEntity;

/**
 * Created by changxing on 2017/12/3.
 */
@Dao
public interface EssayDao {
    @Query("SELECT * FROM essays where dataCurr = :day")
    LiveData<EssayDayEntity> loadEssayDao(String day);

    @Query("SELECT * FROM essays  order by id desc")
    LiveData<EssayDayEntity> loadLastEssayDao();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveEssayItem(EssayDayEntity entity);
}
