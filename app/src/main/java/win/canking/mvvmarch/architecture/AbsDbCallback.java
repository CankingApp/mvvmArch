package win.canking.mvvmarch.architecture;

import android.arch.persistence.db.SupportSQLiteDatabase;

/**
 * Created by changxing on 2017/12/3.
 */

public abstract class AbsDbCallback {
    public abstract void create(SupportSQLiteDatabase db);

    public abstract void open();

    public abstract void upgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion);
}
