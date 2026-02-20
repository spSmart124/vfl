package com.vfl.dto;


import com.vfl.structure.Force;
import com.vfl.structure.Kibana;

public class Case implements ITemplate{
    private String caseNo;
    private String orgId;
    private String pod;
    private String runtimeEnvironment;
    private String osFamily;
    private String issue;
    private String description;
    private String observation;
    private final Class[] structures = {Kibana.class, Force.class};


    private String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    @Override
    public String getNo() {
        return getCaseNo();
    }

    @Override
    public Class[] findStructures() {
        return structures;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getRuntimeEnvironment() {
        return runtimeEnvironment;
    }

    public void setRuntimeEnvironment(String runtimeEnvironment) {
        this.runtimeEnvironment = runtimeEnvironment;
    }

    public String getOsFamily() {
        return osFamily;
    }

    public void setOsFamily(String osFamily) {
        this.osFamily = osFamily;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\"caseNo\": " + "\"" + caseNo + "\",\n" +
                "\"orgId\": " + "\"" + orgId + "\",\n" +
                "\"pod\": " + "\"" + pod + "\",\n" +
                "\"runtimeEnvironment\": " + "\"" + runtimeEnvironment + "\",\n" +
                "\"osFamily\": " + "\"" + osFamily + "\",\n" +
                "\"issue\": " + "\"" + issue + "\",\n" +
                "\"description\": " + "\"" + description + "\",\n" +
                "\"observation\": " + "\"" + observation + "\"\n" +
                '}';
    }
}
