package win.canking.mvvmarch.architecture;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static win.canking.mvvmarch.architecture.Resource.Status.ERROR;
import static win.canking.mvvmarch.architecture.Resource.Status.LOADING;
import static win.canking.mvvmarch.architecture.Resource.Status.MORE_ADD;
import static win.canking.mvvmarch.architecture.Resource.Status.SUCCEED;

/**
 * Created by changxing on 2017/12/3.
 */

public class Resource<T> {
    public enum Status {
        LOADING, MORE_ADD, SUCCEED, ERROR
    }

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCEED, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }

    public static <T> Resource<T> moreSucceed(@Nullable T data) {
        return new Resource<>(MORE_ADD, data, null);
    }
}
