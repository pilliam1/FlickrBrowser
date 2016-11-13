package com.example.william.flickrbrowser;

/**
 * Created by William on 12/11/2016.
 */

//holding data relating to a photo
class Photo {
    private String mTitle;
    private String mAuthor;
    private String mAutherId;
    private String mLink;
    private String mTags;
    private String mImage;

    public Photo(String title, String author, String autherId, String link, String tags, String image) {
        mTitle = title;
        mAuthor = author;
        mAutherId = autherId;
        mLink = link;
        mTags = tags;
        mImage = image;
    }

    String getTitle() {
        return mTitle;
    }

    String getAuthor() {
        return mAuthor;
    }

    String getAutherId() {
        return mAutherId;
    }

    String getLink() {
        return mLink;
    }

    String getTags() {
        return mTags;
    }

    String getImage() {
        return mImage;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mAutherId='" + mAutherId + '\'' +
                ", mLink='" + mLink + '\'' +
                ", mTags='" + mTags + '\'' +
                ", mImage='" + mImage + '\'' +
                '}';
    }
}
