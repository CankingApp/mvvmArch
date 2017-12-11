package win.canking.mvvmarch.architecture;

/**
 * Created by changxing on 2017/12/3.
 */

public interface IRequestApi<ResultType> {
    ResultType getBody();
    String getErrorMsg();
    boolean isSuccessful();
}
