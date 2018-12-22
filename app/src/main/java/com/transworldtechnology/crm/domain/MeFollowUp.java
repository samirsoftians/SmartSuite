package com.transworldtechnology.crm.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.File;

/**
 * Created by root on 22/1/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeFollowUp {

    private Integer contactId;
    private Integer prospCustCode;//no use
    private String followUpDate; //by Default current date
    private String followUpTime; //by Default current time
    private String status; //eg bill submission
    private String followUpType; //eg email
    private String followUpInOut; //CAPS IN/OUT
    private String comments;
    private String nextFollowUpDate; //user will be select
    private String nextFollowUpTime; //user will be select
    private String nextFollowUpType;
    private String preparation;
    private String noOfSalesQuo; //no use
    private String noOfMailerToCust; //no use
    private Integer theSrNo;    //no use
    private String followUpSubject; //no use
    private Integer marketingRepCode;
    private String city;
    private String contactPerson;
    private String designation;
    private String model; //no use
    private String qty;//no use
    private String value;//no use
    private String address;
    private String prospCustName;
    private String theGroupCode;//no use
    private String makeCode;//no use
    private String paymentFollowUp;
    private String spokenTo;
    private String amountExpected;
    private String amountExpectedOn;
    private String remarks;
    private String reply;
    private String documentType;
    private String documentName;
    private String documentCount;
    private String documentStatus;
    private String collectionStaffName;
    private String nextCollectionStaffName;
    private Integer companyMasterId;
    private File[] multipartFile;
    private Double locLat;
    private Double locLong;
    private String locSource;
    private String timeZone;
    private String assignedToUserName;
    private String assignedToUserId;

    public String getAssignedToUserName() {
        return assignedToUserName;
    }

    public void setAssignedToUserName(String assignedToUserName) {
        this.assignedToUserName = assignedToUserName;
    }

    public Double getLocLat() {
        return locLat;
    }

    public void setLocLat(Double locLat) {
        this.locLat = locLat;
    }

    public Double getLocLong() {
        return locLong;
    }

    public void setLocLong(Double locLong) {
        this.locLong = locLong;
    }

    public String getLocSource() {
        return locSource;
    }

    public void setLocSource(String locSource) {
        this.locSource = locSource;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getAssignedToUserId() {
        return assignedToUserId;
    }

    public void setAssignedToUserId(String assignedToUserId) {
        this.assignedToUserId = assignedToUserId;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(String followUpDate) {
        this.followUpDate = followUpDate;
    }

    public Integer getProspCustCode() {
        return prospCustCode;
    }

    public void setProspCustCode(Integer prospCustCode) {
        this.prospCustCode = prospCustCode;
    }

    public String getFollowUpTime() {
        return followUpTime;
    }

    public void setFollowUpTime(String followUpTime) {
        this.followUpTime = followUpTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFollowUpType() {
        return followUpType;
    }

    public void setFollowUpType(String followUpType) {
        this.followUpType = followUpType;
    }

    public String getFollowUpInOut() {
        return followUpInOut;
    }

    public void setFollowUpInOut(String followUpInOut) {
        this.followUpInOut = followUpInOut;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getNextFollowUpDate() {
        return nextFollowUpDate;
    }

    public void setNextFollowUpDate(String nextFollowUpDate) {
        this.nextFollowUpDate = nextFollowUpDate;
    }

    public String getNextFollowUpTime() {
        return nextFollowUpTime;
    }

    public void setNextFollowUpTime(String nextFollowUpTime) {
        this.nextFollowUpTime = nextFollowUpTime;
    }

    public String getNextFollowUpType() {
        return nextFollowUpType;
    }

    public void setNextFollowUpType(String nextFollowUpType) {
        this.nextFollowUpType = nextFollowUpType;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public String getNoOfSalesQuo() {
        return noOfSalesQuo;
    }

    public void setNoOfSalesQuo(String noOfSalesQuo) {
        this.noOfSalesQuo = noOfSalesQuo;
    }

    public String getNoOfMailerToCust() {
        return noOfMailerToCust;
    }

    public void setNoOfMailerToCust(String noOfMailerToCust) {
        this.noOfMailerToCust = noOfMailerToCust;
    }

    public Integer getTheSrNo() {
        return theSrNo;
    }

    public void setTheSrNo(Integer theSrNo) {
        this.theSrNo = theSrNo;
    }

    public String getFollowUpSubject() {
        return followUpSubject;
    }

    public void setFollowUpSubject(String followUpSubject) {
        this.followUpSubject = followUpSubject;
    }

    public Integer getMarketingRepCode() {
        return marketingRepCode;
    }

    public void setMarketingRepCode(Integer marketingRepCode) {
        this.marketingRepCode = marketingRepCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProspCustName() {
        return prospCustName;
    }

    public void setProspCustName(String prospCustName) {
        this.prospCustName = prospCustName;
    }

    public String getTheGroupCode() {
        return theGroupCode;
    }

    public void setTheGroupCode(String theGroupCode) {
        this.theGroupCode = theGroupCode;
    }

    public String getMakeCode() {
        return makeCode;
    }

    public void setMakeCode(String makeCode) {
        this.makeCode = makeCode;
    }

    public String getPaymentFollowUp() {
        return paymentFollowUp;
    }

    public void setPaymentFollowUp(String paymentFollowUp) {
        this.paymentFollowUp = paymentFollowUp;
    }

    public String getSpokenTo() {
        return spokenTo;
    }

    public void setSpokenTo(String spokenTo) {
        this.spokenTo = spokenTo;
    }

    public String getAmountExpected() {
        return amountExpected;
    }

    public void setAmountExpected(String amountExpected) {
        this.amountExpected = amountExpected;
    }

    public String getAmountExpectedOn() {
        return amountExpectedOn;
    }

    public void setAmountExpectedOn(String amountExpectedOn) {
        this.amountExpectedOn = amountExpectedOn;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentCount() {
        return documentCount;
    }

    public void setDocumentCount(String documentCount) {
        this.documentCount = documentCount;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getCollectionStaffName() {
        return collectionStaffName;
    }

    public void setCollectionStaffName(String collectionStaffName) {
        this.collectionStaffName = collectionStaffName;
    }

    public String getNextCollectionStaffName() {
        return nextCollectionStaffName;
    }

    public void setNextCollectionStaffName(String nextCollectionStaffName) {
        this.nextCollectionStaffName = nextCollectionStaffName;
    }

    public Integer getCompanyMasterId() {
        return companyMasterId;
    }

    public void setCompanyMasterId(Integer companyMasterId) {
        this.companyMasterId = companyMasterId;
    }

    public File[] getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(File[] multipartFile) {
        this.multipartFile = multipartFile;
    }

    @Override
    public String toString() {
        return "MeFollowUp{" +
                "contactId=" + contactId +
                ", prospCusCode=" + prospCustCode +
                ", followUpDate='" + followUpDate + '\'' +
                ", followUpTime='" + followUpTime + '\'' +
                ", status='" + status + '\'' +
                ", followUpType='" + followUpType + '\'' +
                ", followUpInOut='" + followUpInOut + '\'' +
                ", comments='" + comments + '\'' +
                ", nextFollowUpDate='" + nextFollowUpDate + '\'' +
                ", nextFollowUpTime='" + nextFollowUpTime + '\'' +
                ", nextFollowUpType='" + nextFollowUpType + '\'' +
                ", preparation='" + preparation + '\'' +
                ", noOfSalesQuo='" + noOfSalesQuo + '\'' +
                ", noOfMailerToCust='" + noOfMailerToCust + '\'' +
                ", theSrNo=" + theSrNo +
                ", followUpSubject='" + followUpSubject + '\'' +
                ", marketingRepCode=" + marketingRepCode +
                ", city='" + city + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", designation='" + designation + '\'' +
                ", model='" + model + '\'' +
                ", qty='" + qty + '\'' +
                ", value='" + value + '\'' +
                ", address='" + address + '\'' +
                ", prospCustName='" + prospCustName + '\'' +
                ", theGroupCode='" + theGroupCode + '\'' +
                ", makeCode='" + makeCode + '\'' +
                ", paymentFollowUp='" + paymentFollowUp + '\'' +
                ", spokenTo='" + spokenTo + '\'' +
                ", amountExpected='" + amountExpected + '\'' +
                ", amountExceptedOn='" + amountExpectedOn + '\'' +
                ", remarks='" + remarks + '\'' +
                ", reply='" + reply + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentName='" + documentName + '\'' +
                ", documentCount='" + documentCount + '\'' +
                ", documentStatus='" + documentStatus + '\'' +
                ", collectionStaffName='" + collectionStaffName + '\'' +
                ", nextCollectionStaffName='" + nextCollectionStaffName + '\'' +
                ", companyMasterId=" + companyMasterId +
                ", multipartFile=" + multipartFile +
                '}';
    }
}
