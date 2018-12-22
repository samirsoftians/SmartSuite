package com.transworldtechnology.crm.domain;

/**
 * Created by root on 21/1/16.
 */
public class MeAddContact {
    private int contact_id;
    private int customerCode;
    private int salesCustomerCode;
    private String companyName;
    private String contactPerson;
    private String salutation;
    private String firstName;
    private String middleName;
    private String lastName;
    private String mobileNo;
    private String email;
    private String phone;
    private String fax;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String designation;
    private String mobileno1;
    private String mobileno2;
    private String alternetNo;
    private String email1;
    private String email2;
    private String entryBy;
    private Integer companyMasterId;

    public MeAddContact() {
    }

    public MeAddContact(int contact_id, int customerCode, int salesCustomerCode, String companyName, String contactPerson, String salutation, String firstName, String middleName, String lastName, String mobileNo, String email, String phone, String fax, String address, String city, String state, String country, String zipcode, String designation, String mobileno1, String mobileno2, String alternetNo, String email1, String email2, String entryBy, Integer companyMasterId) {
        this.contact_id = contact_id;
        this.customerCode = customerCode;
        this.salesCustomerCode = salesCustomerCode;
        this.companyName = companyName;
        this.contactPerson = contactPerson;
        this.salutation = salutation;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.mobileNo = mobileNo;
        this.email = email;
        this.phone = phone;
        this.fax = fax;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
        this.designation = designation;
        this.mobileno1 = mobileno1;
        this.mobileno2 = mobileno2;
        this.alternetNo = alternetNo;
        this.email1 = email1;
        this.email2 = email2;
        this.entryBy = entryBy;
        this.companyMasterId = companyMasterId;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public int getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(int customerCode) {
        this.customerCode = customerCode;
    }

    public int getSalesCustomerCode() {
        return salesCustomerCode;
    }

    public void setSalesCustomerCode(int salesCustomerCode) {
        this.salesCustomerCode = salesCustomerCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMobileno1() {
        return mobileno1;
    }

    public void setMobileno1(String mobileno1) {
        this.mobileno1 = mobileno1;
    }

    public String getMobileno2() {
        return mobileno2;
    }

    public void setMobileno2(String mobileno2) {
        this.mobileno2 = mobileno2;
    }

    public String getAlternetNo() {
        return alternetNo;
    }

    public void setAlternetNo(String alternetNo) {
        this.alternetNo = alternetNo;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEntryBy() {
        return entryBy;
    }

    public void setEntryBy(String entryBy) {
        this.entryBy = entryBy;
    }

    public Integer getCompanyMasterId() {
        return companyMasterId;
    }

    public void setCompanyMasterId(Integer companyMasterId) {
        this.companyMasterId = companyMasterId;
    }

    @Override
    public String toString() {
        return "MeAddContact{" +
                "contact_id=" + contact_id +
                ", customerCode=" + customerCode +
                ", salesCustomerCode=" + salesCustomerCode +
                ", companyName='" + companyName + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", salutation='" + salutation + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", designation='" + designation + '\'' +
                ", mobileno1='" + mobileno1 + '\'' +
                ", mobileno2='" + mobileno2 + '\'' +
                ", alternetNo='" + alternetNo + '\'' +
                ", email1='" + email1 + '\'' +
                ", email2='" + email2 + '\'' +
                ", entryBy='" + entryBy + '\'' +
                ", companyMasterId=" + companyMasterId +
                '}';
    }
}
