package com.vfl.structure;

public class Documentation {
    private String link;
    private String subject;

    public String getLink() {
        return link;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return "Documentation{" +
                "link='" + link + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
//
//
//    public static String toString(FileFormat ff) {
//        if (ff.equals(FileFormat.JSON)) {
//
//        } else if (ff.equals(FileFormat.MARKDOWN)) {
//
//        } else {
//
//        }
//
//        return "";
//    }
}
