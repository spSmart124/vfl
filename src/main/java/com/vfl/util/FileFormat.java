package com.vfl.util;

public enum FileFormat {
    JSON("JSON", ".json"),
    MARKDOWN("MARKDOWN", ".md");

    private final String format;
    private final String fileExtension;
    FileFormat(String format, String fileExtension) {
        this.format = format;
        this.fileExtension = fileExtension;
    }

    public String getFormat() {
        return this.format;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
