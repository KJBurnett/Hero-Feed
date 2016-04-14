package com.burntech.kyler.comicrss;

/**
 * Created by Kyler James Burnett on 3/25/2015.
 */
public class Comic {

    private String title;
    private String publisher;
    private String location;
    private String url;
    private String date;

    public Comic(String title, String publisher, String location, String url, String date) {
        this.title = title;
        this.publisher = publisher;
        this.location = location;
        this.url = url;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comic{" +
                "title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", location='" + location + '\'' +
                ", url='" + url + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getLocation() {
        return location;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }
}
