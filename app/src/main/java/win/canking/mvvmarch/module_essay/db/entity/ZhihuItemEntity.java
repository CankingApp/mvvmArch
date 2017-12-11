package win.canking.mvvmarch.module_essay.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Created by changxing on 2017/12/5.
 */
@Entity(tableName = "zhuhulist")
public class ZhihuItemEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public String date;

    public List<ZhihuStoriesEntity> stories;
    public List<ZhihuStoriesEntity> top_stories;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ZhihuStoriesEntity> getStories() {
        return stories;
    }

    public void setStories(List<ZhihuStoriesEntity> stories) {
        this.stories = stories;
    }

    public List<ZhihuStoriesEntity> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<ZhihuStoriesEntity> top_stories) {
        this.top_stories = top_stories;
    }
}
