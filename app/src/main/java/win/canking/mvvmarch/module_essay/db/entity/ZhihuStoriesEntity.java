package win.canking.mvvmarch.module_essay.db.entity;

import win.canking.mvvmarch.module_essay.model.IEssayItem;

/**
 * Created by changxing on 2017/12/5.
 */
public class ZhihuStoriesEntity implements IEssayItem {
    public String[] images;
    public int type;
    public long id;
    public String ga_grefix;
    public String title;
    private int zhuhuItemId;

    private String data;

    @Override
    public String getImageUrl() {
        if (images != null) {
            return images[0];
        } else {
            return "";
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDate() {
        return data;
    }

    @Override
    public String getAuthor() {
        return ga_grefix;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getGa_grefix() {
        return ga_grefix;
    }

    public void setGa_grefix(String ga_grefix) {
        this.ga_grefix = ga_grefix;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public int getZhuhuItemId() {
        return zhuhuItemId;
    }

    public void setZhuhuItemId(int zhuhuItemId) {
        this.zhuhuItemId = zhuhuItemId;
    }
}
