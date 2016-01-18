package com.example.bhavya.ScrapBook;

import android.net.Uri;

/**
 * Created by chkee on 11/4/2015.
 */
public class PhotoItem {
    private Uri thumbnailUri;
    private Uri fullImageUri;
    public PhotoItem(Uri thumbnailUri,Uri fullImageUri) {
        this.thumbnailUri = thumbnailUri;
        this.fullImageUri = fullImageUri;
    }
    /**
     * Getters and setters
     */
    public Uri getThumbnailUri() {
        return thumbnailUri;
    }
    public void setThumbnailUri(Uri thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }
    public Uri getFullImageUri() {
        return fullImageUri;
    }
    public void setFullImageUri(Uri fullImageUri) {
        this.fullImageUri = fullImageUri;
    }
}
