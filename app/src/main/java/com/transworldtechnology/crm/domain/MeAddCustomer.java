package com.transworldtechnology.crm.domain;

/**
 * Created by melayer on 16/5/16.
 */
public class MeAddCustomer {
    private int customerCode;
    private String companyName ;
    private String address;
    private String city;
    private String zip;
    private String state;
    private String country;
    private String contactPerson;
    private String phone;
    private String fax;
    private String eMail;



    private String webSite;
    private String intrestedProduct;
   // private String custEnteredByRep;
    private String weeklyOffon;
   // private Date addedOnDate;
   // private Date prospCustAddedOn;
    private int salesCustomerCode;
   // private int isDataValid;
    private String category;
    private String stdCode;
    private String mobileNo;
   // private String resiNo;
    private String telephoneNo2;
    private String comments;
    private Double potentialValue;
    private int noOfUnits;
    //private String vendorCode;
    //private String assighnedby;
    private String leadRef;
   // private int f1;
   // private int allocatedto;
    //private Date updatedatetime;
   // private String salesTax ;
    //private String VATNo;
    private String mobileNo1;
    private String mobileNo2;
    private int opportunityCode ;
    //private String opportunityName;
    //private int OEM ;
    //private String branchName;
   // private String groupOfCompany;
    //private String typeOfBuisness;
    //private String dealInProduct;
   // private String payTerms;
    //private String riskInvolved;
   // private String colloectionStaffName;
   // private String custCategory;

    private Integer companyMasterId;

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
    public int getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(int customerCode) {
        this.customerCode = customerCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }



    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getIntrestedProduct() {
        return intrestedProduct;
    }

    public void setIntrestedProduct(String intrestedProduct) {
        this.intrestedProduct = intrestedProduct;
    }

    public String getWeeklyOffon() {
        return weeklyOffon;
    }

    public void setWeeklyOffon(String weeklyOffon) {
        this.weeklyOffon = weeklyOffon;
    }

    public int getSalesCustomerCode() {
        return salesCustomerCode;
    }

    public void setSalesCustomerCode(int salesCustomerCode) {
        this.salesCustomerCode = salesCustomerCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStdCode() {
        return stdCode;
    }

    public void setStdCode(String stdCode) {
        this.stdCode = stdCode;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getTelephoneNo2() {
        return telephoneNo2;
    }

    public void setTelephoneNo2(String telephoneNo2) {
        this.telephoneNo2 = telephoneNo2;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Double getPotentialValue() {
        return potentialValue;
    }

    public void setPotentialValue(Double potentialValue) {
        this.potentialValue = potentialValue;
    }

    public int getNoOfUnits() {
        return noOfUnits;
    }

    public void setNoOfUnits(int noOfUnits) {
        this.noOfUnits = noOfUnits;
    }

    public String getLeadRef() {
        return leadRef;
    }

    public void setLeadRef(String leadRef) {
        this.leadRef = leadRef;
    }

    public String getMobileNo1() {
        return mobileNo1;
    }

    public void setMobileNo1(String mobileNo1) {
        this.mobileNo1 = mobileNo1;
    }

    public String getMobileNo2() {
        return mobileNo2;
    }

    public void setMobileNo2(String mobileNo2) {
        this.mobileNo2 = mobileNo2;
    }

    public int getOpportunityCode() {
        return opportunityCode;
    }

    public void setOpportunityCode(int opportunityCode) {
        this.opportunityCode = opportunityCode;
    }


    public Integer getCompanyMasterId() {
        return companyMasterId;
    }

    public void setCompanyMasterId(Integer companyMasterId) {
        this.companyMasterId = companyMasterId;
    }

    @Override
    public String toString() {
        return "MeAddCustomer{" +
                "customerCode=" + customerCode +
                ", companyName='" + companyName + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                ", EMail='" + eMail + '\'' +
                ", webSite='" + webSite + '\'' +
                ", intrestedProduct='" + intrestedProduct + '\'' +
                ", weeklyOffon='" + weeklyOffon + '\'' +
                ", salesCustomerCode=" + salesCustomerCode +
                ", category='" + category + '\'' +
                ", stdCode='" + stdCode + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", telephoneNo2='" + telephoneNo2 + '\'' +
                ", comments='" + comments + '\'' +
                ", potentialValue=" + potentialValue +
                ", noOfUnits=" + noOfUnits +
                ", leadRef='" + leadRef + '\'' +
                ", mobileNo1='" + mobileNo1 + '\'' +
                ", mobileNo2='" + mobileNo2 + '\'' +
                ", opportunityCode=" + opportunityCode +
                ", companyMasterId=" + companyMasterId +
                '}';
    }
}
