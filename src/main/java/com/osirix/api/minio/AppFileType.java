package com.osirix.api.minio;

public enum AppFileType {
	FILES("files"), 
    MEDIA("media"); 

    private final String folderName;

    AppFileType(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public static AppFileType fromString(String text) {
        for (AppFileType b : AppFileType.values()) {
            if (b.folderName.equalsIgnoreCase(text) || b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }

}
