package com.ngb.colorflashscreen.datasource.model;

public class ScreenModel {
    private String name;
    private String image;
    private boolean isApply = false;

    public ScreenModel( String name, String image ) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public boolean isApply() {
        return isApply;
    }

    public void setApply( boolean apply ) {
        isApply = apply;
    }
}
