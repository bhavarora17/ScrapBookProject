package com.example.bhavya.ScrapBook;

/**
 * Created by chkee on 11/6/2015.
 */
//Description of Photo
public class Photo {
    int id;
    String image_id;
    Boolean value;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

