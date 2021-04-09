package edu.temple.bookshelf;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {


    private int id;
    private String coverURL;
    private String title;
    private String author;
    private int duration;


    public Book(String title, String author){
        this.title = title;
        this.author = author;
    }

    protected Book(Parcel in) {
        id = in.readInt();
        coverURL = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public int getId() {
        return id;
    }
    public int getDuration() {
        return duration;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(coverURL);
    }
}
