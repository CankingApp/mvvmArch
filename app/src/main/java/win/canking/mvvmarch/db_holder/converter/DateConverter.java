/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package win.canking.mvvmarch.db_holder.converter;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import win.canking.mvvmarch.module_essay.db.entity.ZhihuStoriesEntity;

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static List<ZhihuStoriesEntity> toString(String timestamp) {
        List<ZhihuStoriesEntity> tmp = new ArrayList<>();
        Gson gson = new GsonBuilder().create();

        try {
            JSONArray jsonArray = new JSONArray(timestamp);
            for (int i = 0, j = jsonArray.length(); i < j; i++) {
                ZhihuStoriesEntity entity = gson.fromJson(jsonArray.getJSONObject(i).toString(), ZhihuStoriesEntity.class);
                tmp.add(entity);
            }
        } catch (JSONException e) {

        }

        return tmp;
    }

    @TypeConverter
    public static String toZhihuStoriesEntity(List<ZhihuStoriesEntity> list) {
        StringBuffer res = new StringBuffer("[");
        Gson gson = new GsonBuilder().create();

        try {
            for (ZhihuStoriesEntity e : list) {
                res.append(gson.toJson(e) + ",");
            }
        } catch (Exception e) {

        }
        if (res.length() > 1) {
            res.replace(res.length() - 1, res.length(), "]");
        } else {
            res.append("]");
        }
        Log.d("changxing", res.toString());

        return res.toString();
    }
}
