package com.oneupdog.popularmovies.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nbjensen on 2/12/16.
 */
//{
//        "id": 76341,
//        "results": [
//        {
//        "id": "5644f7c7c3a36870d6006846",
//        "iso_639_1": "en",
//        "key": "2h6IKpgFixg",
//        "name": "Mad Max Fury Road Official Main Trailer",
//        "site": "YouTube",
//        "size": 1080,
//        "type": "Trailer"
//        }
//        ]
//        }
public class Trailer {

    private String id;
    private String lang;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public Trailer(JSONObject json) throws JSONException {
        id = json.getString("id");
        lang = json.getString("iso_639_1");
        key = json.getString("key");
        name = json.getString("name");
        site = json.getString("site");
        size = json.getInt("size");
        type = json.getString("type");
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getLang() {
        return lang;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }


}
