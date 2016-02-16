package com.oneupdog.popularmovies.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nbjensen on 2/12/16.
 */
//{
//        "id": "55660928c3a3687ad7001db1",
//        "author": "Phileas Fogg",
//        "content": "Fabulous action movie. Lots of interesting characters. They don't make many movies like this. The whole movie from start to finish was entertaining I'm looking forward to seeing it again. I definitely recommend seeing it.",
//        "url": "http://j.mp/1HLTNzT"
//        },
public class Review {

    private String id;
    private String author;
    private String content;
    private String url;

    public Review(JSONObject json) throws JSONException {
        id = json.getString("id");
        author = json.getString("author");
        content = json.getString("content");
        url = json.getString("url");
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
