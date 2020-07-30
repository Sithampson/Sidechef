package com.example.sidechef.Model;

public class Food {
    private String id;
    private String name;
    private String imageUrl;
    private byte[] image;

    public Food() {
    }

    public Food(String id, String name, byte[] image, String imageUrl) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
