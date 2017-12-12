package win.canking.mvvmarch.db_holder;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import win.canking.mvvmarch.AppExecutors;
import win.canking.mvvmarch.architecture.DbCallbackHelper;
import win.canking.mvvmarch.db_holder.converter.DateConverter;
import win.canking.mvvmarch.module_essay.db.dao.EssayDao;
import win.canking.mvvmarch.module_essay.db.dao.ZhuhuDao;
import win.canking.mvvmarch.module_essay.db.entity.EssayDayEntity;
import win.canking.mvvmarch.module_essay.db.entity.ZhihuItemEntity;

/**
 * Created by changxing on 2017/11/29.
 */


@Database(entities = {EssayDayEntity.class, ZhihuItemEntity.class}, version = 1, exportSchema = true)
@TypeConverters(DateConverter.class)
public abstract class AppDB extends RoomDatabase {

    private static AppDB sInstance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "canking.db";


    public abstract EssayDao essayDao();

    public abstract ZhuhuDao zhuhuDao();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDB getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppDB.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext(), executors);
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDB buildDatabase(final Context appContext,
                                       final AppExecutors executors) {

        return Room.databaseBuilder(appContext, AppDB.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull final SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(new Runnable() {
                            @Override
                            public void run() {

                                // Add a delay to simulate a long-running operation
                                addDelay();
                                // Generate the getDate for pre-population
                                AppDB database = AppDB.getInstance(appContext, executors);
                                // notify that the database was created and it's ready to be used

                                DbCallbackHelper.dispatchOnCreate(db);
                                database.setDatabaseCreated();
                            }
                        });
                    }
                }).addMigrations(DbCallbackHelper.getUpdateConfig()).build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true);
    }


    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
