package com.vfl.structure;

public class Jira {
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
        return "Jira{" +
                "link='" + link + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
