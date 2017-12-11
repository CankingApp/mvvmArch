package win.canking.mvvmarch.module_essay.model;

/**
 * Created by changxing on 2017/12/4.
 */

public interface Essay {
    int getId();

    String getAuthor();

    String getTitle();

    String getDigest();

    String getContent();

    long getWc();
}
