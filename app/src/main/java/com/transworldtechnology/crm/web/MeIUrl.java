package com.transworldtechnology.crm.web;

/**
 * Created by root on 15/1/16.
 */
public interface MeIUrl
{
    String PROTOCOL = "http";
    String SERVER = "173.234.153.82";
    // String AMAZON_SERVER = "ec2-52-36-158-236.us-west-2.compute.amazonaws.com";
    String PORT = "7878";
   // String PORT = "7575";
    String APP_NAME = "transworldcrm";
    String URL_BASE = PROTOCOL + "://" + SERVER + ":" + PORT + "/" + APP_NAME + "/";
    String URL_LOGIN = URL_BASE + "user/login";
    String URL_SEARCH_COMPANY = URL_BASE + "searchCompany/search/";
    String URL_WEEK_APPOINTMENT_LIST = URL_BASE + "followup/listOfAppointmentsWithinWeekByType/";
    String URL_MONTH_APPOINTMENT_LIST = URL_BASE + "followup/listOfAppointmentsWithinMonthByType/";
    String URL_APPOINTMENT_LIST__TYPE = URL_BASE + "followup/listOfAppointmentTodayByType/";
    String URL_ADD_CONTACT = URL_BASE + "addContact/addNewContact/";
    String URL_ADD_CONTACT_SPOKEN_TO = URL_BASE + "addContact/spokenToList/";
    String URL_SPECIAL_EMAIL_LIST = URL_BASE + "user/listOfSpecialEmails/";
    String URL_ADD_NEW_FOLLOW_UP = URL_BASE + "followup/addNewFollowUp";
    String URL_UPLOAD_TO_FTP_SERVER = URL_BASE + "followup/uploadToFtpServer/";
    String URL_APPOINTMENT_LIST_DETAILS = URL_BASE + "followup/getAppointmentData/";
    String URL_ASSIGN_TO_LIST = URL_BASE + "user/listOfUserNames/";
    String URL_LAST_FOLLOW_UP = URL_BASE + "followup/listOfLastFollowUp/";
    String URL_LAST_TRANSACTION = URL_BASE + "followup/getLastTransaction/";
    String URL_LEDGER_STATEMENT = URL_BASE + "followup/getLedgerStatements/";
    String URL_LAST_INVOICE = URL_BASE + "followup/getLastInvoice/";
    //SYNC Data
    String URL_SYNC_SEARCH_COMPANY = URL_BASE + "syncData/syncSearchCompany/";
    String URL_SYNC_ADD_CONTACT_SPOKEN_TO = URL_BASE + "syncData/syncSpokenToList/";
    String URL_SYNC_APPOINTMENT = URL_BASE + "syncData/syncAppointmentList/";
    String URL_SYNC_ASSIGN_TO = URL_BASE + "syncData/syncAssignToList/";
    String URL_SYNC_SPECIAL_EMAILS = URL_BASE + "syncData/syncSpecialEmails";
    //UPLOAD Data
    String URL_UPLOAD_CONTACTS_TO_SERVER = URL_BASE + "syncData/uploadNewContactToServer";
    String URL_UPLOAD_FOLLOW_UPS_TO_SERVER = URL_BASE + "syncData/uploadNewFollowUpToServer";
    String URL_UPLOAD_LOCATIONS_TO_SERVER = URL_BASE + "syncData/uploadLocationsToServer";
    String URL_TAKE_FOLLOWUP_FROM_COMPANY_NAME = URL_BASE + "searchCompany/takeFollowUp/";
    //Add Customer
    String URL_ADD_CUSTOMER = URL_BASE + "addCustomer/addNewCustomer/";
    String URL_GET_COUNTRY = URL_BASE + "addCustomer/selectCountry/";
    String URL_GET_STATE = URL_BASE + "addCustomer/selectState/";
    String URL_GET_CITY = URL_BASE + "addCustomer/selectCity/";
    String URL_ADD_COUNTRY = URL_BASE + "addCustomer/addCountry/";
    String URL_ADD_STATE = URL_BASE + "addCustomer/addState/";
    String URL_ADD_CITY = URL_BASE + "addCustomer/addCity/";
}
