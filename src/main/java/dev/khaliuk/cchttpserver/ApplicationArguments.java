package dev.khaliuk.cchttpserver;

import jakarta.inject.Singleton;

@Singleton
public class ApplicationArguments {
    private String fileDirectoryName;

    public String getFileDirectoryName() {
        return fileDirectoryName;
    }

    public void setFileDirectoryName(String fileDirectoryName) {
        if (this.fileDirectoryName == null) {
            this.fileDirectoryName = fileDirectoryName;
        }
    }
}
