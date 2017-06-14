package com.example.android.booklisting;

import java.util.ArrayList;

/**
 * A class that represents a book
 */
public class Book {

    private String mId;
    private String mTitle;
    private ArrayList<String> mAuthors;
    private String mPublisher;
    private String mPublishedDate; // ISO format
    private ArrayList<IndustryIdentifier> mIndustryidentifiers;
    private int mPageCount;
    private ArrayList<ImageLink> mImageLinks;
    private String mLanguage;
    private String mInfoLink;

    public Book(String id, String title, ArrayList<String> authors, String publisher,
                String publishedDate, ArrayList<IndustryIdentifier> industryidentifiers,
                int pageCount, ArrayList<ImageLink> imageLinks, String language, String infoLink) {
        this.mId = id;
        this.mTitle = title;
        this.mAuthors = authors;
        this.mPublisher = publisher;
        this.mPublishedDate = publishedDate;
        this.mIndustryidentifiers = industryidentifiers;
        this.mPageCount = pageCount;
        this.mImageLinks = imageLinks;
        this.mLanguage = language;
        this.mInfoLink = infoLink;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getId() {
        return mId;
    }

    public ArrayList<String> getAuthors() {
        return mAuthors;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public ArrayList<IndustryIdentifier> getIndustryidentifiers() {
        return mIndustryidentifiers;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public ArrayList<ImageLink> getImageLinks() {
        return mImageLinks;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public String getInfoLink() {
        return mInfoLink;
    }
}


