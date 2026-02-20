package com.vfl.dto;

import com.vfl.structure.Documentation;
import com.vfl.structure.Jira;

public class Engagement implements ITemplate {
    private String number;
    private String name;
    private String account;
    private String description;
    private String csaComments;
    private String closingNotes;
    private final Class[] structures = {Jira.class, Documentation.class};

    private String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String getNo() {
        return getNumber();
    }

    @Override
    public Class[] findStructures() {
        return structures;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCsaComments() {
        return csaComments;
    }

    public void setCsaComments(String csaComments) {
        this.csaComments = csaComments;
    }

    public String getClosingNotes() {
        return closingNotes;
    }

    public void setClosingNotes(String closingNotes) {
        this.closingNotes = closingNotes;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\"number\": " + "\"" + number + "\",\n" +
                "\"name\": "  + name + "\",\n" +
                "\"account\": "  + account + "\",\n" +
                "\"description\": "  + description + "\",\n" +
                "\"CSAComments\": "  + csaComments + "\",\n" +
                "\"closingNotes\": "  + closingNotes + "\"\n" +
                '}';
    }
}
