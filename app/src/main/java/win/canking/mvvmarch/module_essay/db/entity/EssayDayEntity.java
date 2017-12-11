package win.canking.mvvmarch.module_essay.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import win.canking.mvvmarch.module_essay.model.Essay;

/**
 * Created by changxing on 2017/12/3.
 */
@Entity(tableName = "essays")
public class EssayDayEntity implements Essay {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String dataCurr;
    private String author;
    private String title;
    private String digest;
    private String content;
    private long wc;//字数


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataCurr() {
        return dataCurr;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDigest() {
        return digest;
    }

    public String getContent() {
        return content;
    }

    @Override
    public long getWc() {
        return wc;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDataCurr(String dataCurr) {
        this.dataCurr = dataCurr;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public void setWc(long wc) {
        this.wc = wc;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
