package com.burntech.kyler.comicrss;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Kyler J. Burnett on 4/10/2015.
 */
public class PublisherIdentifier {

    private static PublisherIdentifier _publisherIdentifier;

    private ArrayList<String> _publisherList = new ArrayList<String>();
    private ArrayList<Publisher> _publishers = new ArrayList<Publisher>();
    private DBHelper _db = null;

    private String COMICVINE_URL = "http://www.comicvine.com/new-comics/";

    private PublisherIdentifier() {
    }

    public static PublisherIdentifier getInstance() {
        if (_publisherIdentifier != null)
            return _publisherIdentifier;
        else {
            _publisherIdentifier = new PublisherIdentifier();
            return _publisherIdentifier;
        }
    }

    public void getPublishers(DBHelper db, ArrayList<String> list, PublisherRecyclerAdapter adapter) {
        if (_db == null)
            _db = db;

        // Set the recycler view's adapter pointer to the current class's.
        _publisherList = list;
        _publisherList.clear();

        if (_db.publishersEmpty()) {
            System.out.println("publishers table empty. Downloading publishers and adding to database.");
            rxDownload(adapter);
        }
        else {
            System.out.println("Publishers already in database, good! Do it faster, makes us stronger.");

            _publishers = db.getPublishers();
            System.out.println("_publishers size: " + _publishers.size());
            for (Publisher pub : _publishers) {
                _publisherList.add(pub.getName());
            }
            adapter.notifyDataSetChanged();
        }
    }

    public String getPublisherId(String publisher) {
        int index = _publisherList.indexOf(publisher);
        return _publishers.get(index).getId();
    }

    private void rxDownload(final PublisherRecyclerAdapter adapter) {
        Observable.create(new Observable.OnSubscribe<Document>() {

            @Override
            public void call(Subscriber<? super Document> subscriber) {
                try {
                    subscriber.onNext(runJsoup(COMICVINE_URL));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Action1<Document>() {
                    @Override
                    public void call(Document document) {
                        processJsoupDocument(document);
                        adapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable error) {
                    }
                });
    }// end rxDownload

    private Document runJsoup(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {e.printStackTrace();}
        return doc;

    }

    private void processJsoupDocument(Document doc) {
        Elements pubs = doc.select("select > option");
        _publisherList.clear();
        _publishers.clear();
        for (int x = 1; x < pubs.size(); x++) {
            if (pubs.get(x).text().equals("Select Week"))
                break;
            String id = pubs.get(x).attr("value");
            String pub = pubs.get(x).text();
            System.out.println("id: " + id + " pub: " + pub);
            _publisherList.add(pub);
            _publishers.add(new Publisher(id, pub));
            _db.insertPublisher(id, pub);
        }
    }
}// end PublisherIdentifier
