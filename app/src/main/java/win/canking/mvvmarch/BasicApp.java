package win.canking.mvvmarch;

import android.app.Application;

import win.canking.mvvmarch.db_holder.AppDB;

public class BasicApp extends Application {
    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public AppDB getDatabase() {
        return AppDB.getInstance(this, mAppExecutors);
    }

    public AppExecutors getAppExecutors() {
        return mAppExecutors;
    }
}
