package com.burntech.kyler.comicrss;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Pair;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Kyler J. Burnett on 4/4/2015.
 */
public class ComicDownloader {

    // Final declarations.
    private final String COVERS_DIR = "/Covers/";
    private final String COMICVINE_URL = "http://www.comicvine.com/new-comics/";

    private Context _context;
    private File coversFolder;
    private String _url;
    private String _publisher;
    private DBHelper _db;
    private String _week;

    // first == image URL. second == filePath + name + ".jpeg".
    private ArrayList<Pair<String, String>> downloadQueue = new ArrayList<Pair<String, String>>();

    public ComicDownloader(Context context) {
        _db = new DBHelper(context);
        _context = context;
        coversFolder = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + COVERS_DIR);
        if (!coversFolder.exists()) coversFolder.mkdir();
    }

    public void downloadComics(String publisher, String week) {
        _url = buildURL(publisher, week);
        _publisher = publisher;
        _week = week;
        rxDownload();
    }

    // Uses the RxJava Observable Module to easily call an outside Asynchronous thread (MUCH better than AsyncTask).
    // https://github.com/ReactiveX/RxJava/wiki/The-RxJava-Android-Module
    private void rxDownload() {
        Observable.create(new Observable.OnSubscribe<Document>() {

            @Override
            public void call(Subscriber<? super Document> subscriber) {
                try {
                    subscriber.onNext(runJsoup(_url));
                    subscriber.onCompleted();
                } catch (Exception e) {subscriber.onError(e);}
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Action1<Document>() {
                    @Override
                    public void call(Document document) {
                        processJsoupDocument(document);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable error) {
                    }
                });
    }// end rxDownload

    public Document runJsoup(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {e.printStackTrace();}
        return doc;
    }

    public void processJsoupDocument(Document doc) {
        // Query the necessary elements from the processed Jsoup HTML document.
        Elements src = doc.select("ul.issue-grid img");
        Elements names = doc.select("ul.issue-grid p.issue-number");
        Elements dates = doc.select("ul.issue-grid p.issue-date");

        int downloadCount = 0;
        for (int x = 0; x < src.size(); x++) {
            String name = names.get(x).text();
            String coverImage = formatIssue(name); // remove poorly formatted characters.
            File imageFile = new File(coversFolder.getAbsolutePath() + "/" + coverImage + ".jpeg");

            if (!imageFile.exists()) { // Then download the cover image.
                downloadCount++;
                String date = dates.get(x).text();
                String imgUrl = src.get(x).attr("abs:src");
                downloadQueue.add(new Pair(imgUrl, imageFile.getPath())); // Add comic cover image information to download queue.
                addComicToDatabase(imgUrl, imageFile.getPath(), date);
            }
            else System.out.println("Already Exists: " + imageFile.getName());
        }
        downloadCoverImages();

        // Download Report. /*DEBUG*/
        System.out.println("\nTotal files found     : " + src.size());
        System.out.println("Total files downloaded: " + downloadCount);
    }

    private void downloadCoverImages() {
        ArrayList<Thread> threads = new ArrayList<Thread>();
        // first  = image _url. second = file path & name.
        for (final Pair pair : downloadQueue) {
            Thread newThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(pair.second.toString());
                    //System.out.println("FILE PATH: " + file.getPath());
                    try {
                        file.createNewFile();
                        Bitmap bitmap = Picasso.with(_context).load(pair.first.toString()).get();
                        FileOutputStream outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                        outputStream.close();
                        addImageToGallery(pair.second.toString(), _context);
                    } catch (Exception e) {e.printStackTrace();}
                }
            });
            threads.add(newThread);
            newThread.start();
        }

        for(Thread thread : threads) { // Wait for all threads/downloads to finish.
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void addImageToGallery(final String filePath, final Context context) {
        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "Image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    private String formatIssue(String name) {

        name = name.replaceAll("\\/", "-");
        name = name.replaceAll(": ", " - ");
        name = name.replaceAll("\\?", "");
        name = name.replaceAll("\\\"", "'");

        return name;
    }

    private void addComicToDatabase(String imgUrl, String name, String date) {
        // Insert the comic date as a new row in 'comics' within SQLite database.
        File file = new File(name);
        String title = file.getName().substring(0, file.getName().length() - 4);


        _db.insertComic(title, _publisher, name, imgUrl, date, _week);
    }

    // example URL: http://www.comicvine.com/new-comics/?company=31&startWeek=04%2F05%2F2015
    private String buildURL(String publisher, String week) {
        PublisherIdentifier pubIder = PublisherIdentifier.getInstance();
        String publisherId = pubIder.getPublisherId(publisher);
        String startWeek = week;
        startWeek = startWeek.replaceAll("/", "%2F");

        String newURL = COMICVINE_URL + "?company=" + publisherId + "&startWeek=" + startWeek;
        System.out.println("\n=== Built new url ===");
        System.out.println(newURL);
        System.out.println("=====================\n");

        return newURL;
    }
}// end ComicDownloader
