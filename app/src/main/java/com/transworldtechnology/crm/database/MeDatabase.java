package com.transworldtechnology.crm.database;

import android.util.Log;

/**
 * Created by root on 22/1/16.
 */
public class MeDatabase
{

    public static final String DB_NAME = "transworld";
    public static final Integer DB_VERSION = 1;

    public static class MeTableLogin {

        public static final String TABLE_NAME = "tab_login";
        public static final String COL_USER_NAME = "user_name";
        public static final String COL_PASSWORD = "password";
        public static final String COL_EMPLOYEE_CODE = "employee_code";   //      "empcode": 277,
        public static final String COL_COMPANY_MASTER_ID = "company_master_id";
        public static final String COL_ROLE = "role";
        public static final String COL_USER_TYPE_CODE = "user_type_code";
        public static final String COL_ACTIVE_STATUS = "active_status";
        public static final String COL_IMEI = "imei";
        public static final String COL_USER_LEVEL = "userLevel"; //"userlevel": "Level5",
        public static final String COL_COMPANY_CODE = "companyCode";
        public static final String COL_EMP_EMAIL_ID = "empEmailId"; //  "email": "s_shah@transworld-compressor.com"
        public static final String COL_EMPLOYEE_NAME = "employee_name"; //   "empname": "Snehil Shah",


        public static final String COL_FLAG = "flag";


        public static String createTable() {
            StringBuilder query = new StringBuilder();
            query.append("CREATE TABLE " + TABLE_NAME + "(");
            query.append(COL_USER_NAME + " " + "text" + ",");
            query.append(COL_PASSWORD + " " + "text" + ",");
            query.append(COL_EMPLOYEE_CODE + " " + "integer" + ",");
            query.append(COL_COMPANY_MASTER_ID + " " + "integer" + ",");
            query.append(COL_ROLE + " " + "text" + ",");
            query.append(COL_IMEI + " " + "text" + ",");
            query.append(COL_USER_TYPE_CODE + " " + "integer" + ",");
            query.append(COL_ACTIVE_STATUS + " " + "text" + ",");
            query.append(COL_USER_LEVEL + " " + "text" + ",");
            query.append(COL_COMPANY_CODE + " " + "text"+",");
            query.append(COL_EMP_EMAIL_ID +" "+ "text" + ",");
            query.append(COL_EMPLOYEE_NAME + " " + "text" + ",");
            query.append(COL_FLAG + " " + "integer DEFAULT 1");
            query.append(" )");
            Log.i("@Transworld", "Query Create Table - " + query.toString());
            return query.toString();
        }
    }

    public static class MeTableProspectiveCustMaster {
        public static final String TABLE_NAME = "tab_prospective_cust_master";
        public static final String COL_OPPORTUNITY_CODE = "opportunityCode";
        public static final String COL_COMPANY_NAME = "companyName";
        public static final String COL_CUSTOMER_CODE = "customerCode";
        public static final String COL_SALES_CUSTOMER_CODE = "salesCustomerCode";
        public static final String COL_ADDRESS = "Address";
        public static final String COL_CITY = "City";
        public static final String COL_ZIP = "Zip";
        public static final String COL_STATE = "State";
        public static final String COL_COUNTRY = "Country";
        public static final String COL_CONTACT_PERSON = "ContactPerson";
        public static final String COL_PHONE = "Phone";
        public static final String COL_FAX = "Fax";
        public static final String COL_EMAIL = "Email";
        public static final String COL_WEBSITE = "Website";
        public static final String COL_INTERESTED_PRODUCT = "IntrestedProduct";
        public static final String COL_CUST_ENTERED_BY_REP = "CustEnteredByRep";
        public static final String COL_WEEKLY_OFF_ON = "WeeklyOffon";
        public static final String COL_ADDED_ON_DATE = "AddedOnDate";
        public static final String COL_PROSP_CUST_ADDED_ON = "ProspCustAddedOn";
        public static final String COL_IS_DATA_VALID = "IsDataValid";
        public static final String COL_CATEGORY = "Category";
        public static final String COL_STD_CODE = "STDCode";
        public static final String COL_MOBILE_NO = "MobileNo";
        public static final String COL_RESI_NO = "ResiNo";
        public static final String COL_TELEPHONE_NO2 = "TelephoneNo2";
        public static final String COL_COMMENTS = "Comments";
        public static final String COL_POTENTIAL_VALUE = "PotentialValue";
        public static final String COL_NO_OF_UNITS = "NoOfUnits";
        public static final String COL_VENDOR_CODE = "VendorCode";
        public static final String COL_ASSIGNED_BY = "Assighnedby";
        public static final String COL_LEAD_REF = "leadRef";
        public static final String COL_ALLOCATED_TO = "Allocatedto";
        public static final String COL_UPDATED_DATETIME = "Updatedatetime";
        public static final String COL_SALES_TAX = "SalesTax";
        public static final String COL_VAT_NO = "VATNo";
        public static final String COL_MOBILE_NO_1 = "MobileNo1";
        public static final String COL_MOBILE_NO_2 = "MobileNo2";
        public static final String COL_OPPORTUNITY_NAME = "OpportunityName";
        public static final String COL_OEM = "OEM";
        public static final String COL_BRANCH_NAME = "BranchName";
        public static final String COL_GROUP_OF_COMPANY = "GroupOfCompany";
        public static final String COL_TYPE_OF_BUSSINESS = "TypeOfBuisness";
        public static final String COL_DEAL_IN_PRODUCT = "DealInProduct";
        public static final String COL_PAYTERMS = "PayTerms";
        public static final String COL_RISK_INVOLVED = "RiskInvolved";
        public static final String COL_COLLECTION_STAFF_NAME = "ColloectionStaffName";
        public static final String COL_FLAG = "flag";

        public static String createTable() {
            StringBuilder query = new StringBuilder();
            query.append("CREATE TABLE " + TABLE_NAME + "(");
            query.append(COL_OPPORTUNITY_CODE + " " + "integer DEFAULT 0" + ",");
            query.append(COL_COMPANY_NAME + " " + "text DEFAULT NULL" + ",");
            query.append(COL_CUSTOMER_CODE + " " + "integer NOT NULL DEFAULT 0" + ",");
            query.append(COL_SALES_CUSTOMER_CODE + " " + "integer DEFAULT 0" + ",");
            query.append(COL_ADDRESS + " " + "text DEFAULT NULL" + ",");
            query.append(COL_ZIP + " " + "text DEFAULT NULL" + ",");
            query.append(COL_STATE + " " + "text DEFAULT NULL" + ",");
            query.append(COL_COUNTRY + " " + "text DEFAULT NULL" + ",");
            query.append(COL_PHONE + " " + "text DEFAULT NULL" + ",");
            query.append(COL_FAX + " " + "text DEFAULT NULL" + ",");
            query.append(COL_EMAIL + " " + "text DEFAULT NULL" + ",");
            query.append(COL_WEBSITE + " " + "text DEFAULT NULL" + ",");
            query.append(COL_INTERESTED_PRODUCT + " " + "text DEFAULT NULL" + ",");
            query.append(COL_COMMENTS + " " + "text DEFAULT NULL" + ",");
            query.append(COL_CUST_ENTERED_BY_REP + " " + "text DEFAULT NULL" + ",");
            query.append(COL_WEEKLY_OFF_ON + " " + "text DEFAULT NULL" + ",");
            query.append(COL_ADDED_ON_DATE + " " + "text DEFAULT NULL" + ",");
            query.append(COL_PROSP_CUST_ADDED_ON + " " + "text DEFAULT NULL" + ",");
            query.append(COL_IS_DATA_VALID + " " + "integer DEFAULT 1" + ",");
            query.append(COL_CITY + " " + "text DEFAULT NULL" + ",");
            query.append(COL_CONTACT_PERSON + " " + "text DEFAULT NULL" + ",");
            query.append(COL_CATEGORY + " " + "text DEFAULT 1" + ",");
            query.append(COL_STD_CODE + " " + "text DEFAULT NULL" + ",");
            query.append(COL_MOBILE_NO + " " + "text DEFAULT 0" + ",");
            query.append(COL_RESI_NO + " " + "text DEFAULT 0" + ",");
            query.append(COL_TELEPHONE_NO2 + " " + "text DEFAULT 0" + ",");
            query.append(COL_POTENTIAL_VALUE + " " + "real DEFAULT 0" + ",");
            query.append(COL_NO_OF_UNITS + " " + "integer DEFAULT 0" + ",");
            query.append(COL_VENDOR_CODE + " " + "text DEFAULT NULL" + ",");
            query.append(COL_ASSIGNED_BY + " " + "text" + ",");
            query.append(COL_LEAD_REF + " " + "text" + ",");
            query.append(COL_ALLOCATED_TO + " " + "integer" + ",");
            query.append(COL_UPDATED_DATETIME + " " + "text DEFAULT CURRENT_TIMESTAMP" + ",");
            query.append(COL_SALES_TAX + " " + "text" + ",");
            query.append(COL_VAT_NO + " " + "text" + ",");
            query.append(COL_MOBILE_NO_1 + " " + "text DEFAULT 0" + ",");
            query.append(COL_MOBILE_NO_2 + " " + "text DEFAULT 0" + ",");
            query.append(COL_OPPORTUNITY_NAME + " " + "text" + ",");
            query.append(COL_OEM + " " + "integer DEFAULT 0" + ",");
            query.append(COL_BRANCH_NAME + " " + "text" + ",");
            query.append(COL_GROUP_OF_COMPANY + " " + "text" + ",");
            query.append(COL_TYPE_OF_BUSSINESS + " " + "text" + ",");
            query.append(COL_DEAL_IN_PRODUCT + " " + "text" + ",");
            query.append(COL_PAYTERMS + " " + "text" + ",");
            query.append(COL_RISK_INVOLVED + " " + "text" + ",");
            query.append(COL_COLLECTION_STAFF_NAME + " " + "text" + ",");
            query.append(COL_FLAG + " " + "integer DEFAULT 1");
            query.append(")");
            Log.i("@Transworld", "Query Create Table Search Company - " + query.toString());
            return query.toString();
        }
    }

    public static class MeTableFollowUp {
        public static final String TABLE_NAME = "tab_followup";
        public static final String COL_SR_NO = "TheSrno";
        //public static final String COL_TEMP_SR_NO = "tempSrno";
        public static final String COL_PROSP_CUST_CODE = "ProspCustCode";
        public static final String COL_FOLLOW_UP_DATE = "FollowUpDate";
        public static final String COL_FOLLOW_UP_TIME = "FollowUpTime";
        public static final String COL_STATUS = "Status";
        public static final String COL_FOLLOW_UP_TYPE = "FollowUpType";
        public static final String COL_FOLLOW_UP_IN_OUT = "FollowUpInOut";
        public static final String COL_NEXT_FOLLOW_UP_DATE = "NextFollowUpDate";
        public static final String COL_NEXT_FOLLOW_UP_TIME = "NextFollowUpTime";
        public static final String COL_NEXT_FOLLOW_UP_TYPE = "NextFollowUpType";
        public static final String COL_COMMENTS = "Comments";
        public static final String COL_PREPARATION = "Preparation";
        public static final String COL_NO_OF_SALES_QUO = "NoOfSalesQuo";
        public static final String COL_NO_OF_MAILER_TO_CUST = "NoOfMailerToCust";
        public static final String COL_FOLLOW_UP_SUBJECT = "FollowUpSubject";
        public static final String COL_MARKETING_REP_CODE = "MarketingRepcode";
        public static final String COL_CITY = "City";
        public static final String COL_CONTACT_PERSON = "ContactPerson";
        public static final String COL_DESIGNATION = "Designation";
        public static final String COL_MODEL = "Model";
        public static final String COL_QTY = "Qty";
        public static final String COL_VALUE = "Value";
        public static final String COL_ADDRESS = "Address";
        public static final String COL_PROSP_CUST_NAME = "ProspCustName";
        public static final String COL_THE_GROUP_CODE = "TheGroupCode";
        public static final String COL_MAKE_CODE = "MakeCode";
        public static final String COL_PAYMENT_FOLLOW_UP = "PaymentFollowup";
        public static final String COL_SPOKEN_TO = "SpokenTo";
        public static final String COL_AMOUNT_EXPECTED = "AmountExpected";
        public static final String COL_AMOUNT_EXPECTED_ON = "AmountExpectedOn";
        public static final String COL_REMARKS = "Remarks";
        public static final String COL_REPLY = "Reply";
        public static final String COL_DOCUMENT_TYPE = "DocumentType";
        public static final String COL_DOCUMENT_NAME = "DocumentName";
        public static final String COL_DOCUMENT_COUNT = "DocumentCount";
        public static final String COL_DOCUMENT_STATUS = "DocumentStatus";
        public static final String COL_COLLECTION_STAFF_NAME = "ColloectionStaffName";
        public static final String COL_NEXT_COLLECTION_STAFF_NAME = "NextColloectionStaffName";
        public static final String COL_CONTACT_ID = "contact_id";
        public static final String COL_LOC_LAT = "loc_lat";
        public static final String COL_LOC_LONG = "loc_long";
        public static final String COL_LOC_SOURCE = "loc_source";
        public static final String COL_TIMEZONE = "timezone";
        public static final String COL_ASSIGN_TO_USERNAME = "assigntousername";
        public static final String COL_ASSIGN_TO_USER_ID = "assigntouserid";
        public static final String COL_DOC_PATH_1 = "docpath1";
        public static final String COL_DOC_PATH_2 = "docpath2";
        public static final String COL_DOC_PATH_3 = "docpath3";
        public static final String COL_FLAG = "flag";

        public static String createTable() {
            StringBuilder query = new StringBuilder();
            query.append("CREATE TABLE " + TABLE_NAME + "(");
            query.append(COL_SR_NO + " " + "integer primary key AUTOINCREMENT,");
            //  query.append(COL_TEMP_SR_NO + " " + "integer primary key AUTOINCREMENT,");
            query.append(COL_PROSP_CUST_CODE + " " + "integer NOT NULL DEFAULT 0" + ",");
            query.append(COL_FOLLOW_UP_DATE + " " + "text NOT NULL" + ",");
            query.append(COL_FOLLOW_UP_TIME + " " + "text" + ",");
            query.append(COL_STATUS + " " + "text DEFAULT NULL" + ",");
            query.append(COL_FOLLOW_UP_TYPE + " " + "text DEFAULT NULL" + ",");
            query.append(COL_FOLLOW_UP_IN_OUT + " " + "text DEFAULT OUT" + ",");
            query.append(COL_NEXT_FOLLOW_UP_DATE + " " + "text DEFAULT NULL" + ",");
            query.append(COL_NEXT_FOLLOW_UP_TIME + " " + "text" + ",");
            query.append(COL_NEXT_FOLLOW_UP_TYPE + " " + "text" + ",");
            query.append(COL_COMMENTS + " " + "text DEFAULT NULL" + ",");
            query.append(COL_PREPARATION + " " + "text DEFAULT NULL" + ",");
            query.append(COL_NO_OF_SALES_QUO + " " + "text DEFAULT NULL" + ",");
            query.append(COL_NO_OF_MAILER_TO_CUST + " " + "text DEFAULT NULL" + ",");
            query.append(COL_FOLLOW_UP_SUBJECT + " " + "text DEFAULT General" + ",");
            query.append(COL_MARKETING_REP_CODE + " " + "integer DEFAULT 0" + ",");
            query.append(COL_CITY + " " + "text" + ",");
            query.append(COL_CONTACT_PERSON + " " + "text" + ",");
            query.append(COL_DESIGNATION + " " + "text" + ",");
            query.append(COL_MODEL + " " + "text" + ",");
            query.append(COL_QTY + " " + "text" + ",");
            query.append(COL_VALUE + " " + "text" + ",");
            query.append(COL_ADDRESS + " " + "text" + ",");
            query.append(COL_PROSP_CUST_NAME + " " + "text" + ",");
            query.append(COL_THE_GROUP_CODE + " " + "text" + ",");
            query.append(COL_MAKE_CODE + " " + "text" + ",");
            query.append(COL_PAYMENT_FOLLOW_UP + " " + "text DEFAULT NO" + ",");
            query.append(COL_SPOKEN_TO + " " + "text" + ",");
            query.append(COL_AMOUNT_EXPECTED + " " + "text DEFAULT 0" + ",");
            query.append(COL_AMOUNT_EXPECTED_ON + " " + "text" + ",");
            query.append(COL_REMARKS + " " + "text" + ",");
            query.append(COL_REPLY + " " + "text DEFAULT Yes" + ",");
            query.append(COL_DOCUMENT_TYPE + " " + "text" + ",");
            query.append(COL_DOCUMENT_NAME + " " + "text" + ",");
            query.append(COL_DOCUMENT_COUNT + " " + "text" + ",");
            query.append(COL_DOCUMENT_STATUS + " " + "text" + ",");
            query.append(COL_COLLECTION_STAFF_NAME + " " + "text DEFAULT NULL" + ",");
            query.append(COL_NEXT_COLLECTION_STAFF_NAME + " " + "text DEFAULT NULL" + ",");
            query.append(COL_CONTACT_ID + " " + "integer DEFAULT 0" + ",");
            query.append(COL_LOC_LAT + " " + "real DEFAULT NULL" + ",");
            query.append(COL_LOC_LONG + " " + "real DEFAULT NULL" + ",");
            query.append(COL_LOC_SOURCE + " " + "text DEFAULT NULL" + ",");
            query.append(COL_TIMEZONE + " " + "text DEFAULT NULL" + ",");
            query.append(COL_ASSIGN_TO_USERNAME + " " + "text NOT NULL DEFAULT NULL" + ",");
            query.append(COL_ASSIGN_TO_USER_ID + " " + "text NOT NULL DEFAULT NULL" + ",");
            query.append(COL_DOC_PATH_1 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_PATH_2 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_PATH_3 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_FLAG + " " + "integer DEFAULT 1");
            query.append(")");
            Log.i("@Transworld", "Query Create Table Follow Up - " + query.toString());
            return query.toString();
        }


    }

    public static class MeTableContactDet {
        public static final String TABLE_NAME ="tab_contactdet";
        public static final String COL_CONTACT_ID = "contact_id";
        public static final String COL_CUSTOMER_CODE ="CustomerCode";
        public static final String COL_SALES_CUSTOMER_CODE="SalesCustomerCode";
        public static final String COL_COMPANY_NAME ="CompanyName";
        public static final String COL_CONTACT_PERSON ="ContactPerson";
        public static final String COL_SALUTATION ="Salutation";
        public static final String COL_FIRST_NAME ="FirstName";
        public static final String COL_MIDDLE_NAME ="MiddleName";
        public static final String COL_LAST_NAME ="LastName";
        public static final String COL_MOBILE_NO ="MobileNo";
        public static final String COL_EMAIL ="Email";
        public static final String COL_PHONE="Phone";
        public static final String COL_FAX ="Fax";
        public static final String COL_ADDRESS ="Address";
        public static final String COL_CITY ="City";
        public static final String COL_STATE ="State";
        public static final String COL_COUNTRY ="Country";
        public static final String COL_ZIPCODE ="Zipcode";
        public static final String COL_DESIGNATION ="Designation";
        public static final String COL_MOBILE_NO_1 ="MobileNo1";
        public static final String COL_MOBILE_NO_2 ="MobileNo2";
        public static final String COL_ALTERNET_NO ="AlternetNo";
        public static final String COL_EMAIL_1 ="Email1";
        public static final String COL_EMAIL_2 ="Email2";
        public static final String COL_ENTRY_BY ="EntryBy";
        public static final String COL_FLAG = "flag";

        public static String createTable()
        {
            StringBuilder query =new StringBuilder();
            query.append("CREATE TABLE " + TABLE_NAME + "(");
            query.append(COL_CONTACT_ID + " " + "integer, ");
            query.append(COL_CUSTOMER_CODE + " " + "integer" + ",");
            query.append(COL_SALES_CUSTOMER_CODE + " " + "integer" + ",");
            query.append(COL_COMPANY_NAME +" " +"text DEFAULT NULL" + ",");
            query.append(COL_CONTACT_PERSON +" " +"text DEFAULT NULL" + ",");
            query.append(COL_SALUTATION +" " +"text DEFAULT NULL" + ",");
            query.append(COL_FIRST_NAME +" " +"text DEFAULT NULL" + ",");
            query.append(COL_MIDDLE_NAME +" " +"text DEFAULT NULL" + ",");
            query.append(COL_LAST_NAME +" " +"text DEFAULT NULL" + ",");
            query.append(COL_MOBILE_NO +" " +"text DEFAULT NULL" + ",");
            query.append(COL_EMAIL +" " +"text DEFAULT NULL" + ",");
            query.append(COL_PHONE +" " +"text DEFAULT NULL" + ",");
            query.append(COL_FAX +" " +"text DEFAULT NULL" + ",");
            query.append(COL_ADDRESS +" " +"text DEFAULT NULL" + ",");
            query.append(COL_CITY +" " +"text DEFAULT NULL" + ",");
            query.append(COL_STATE +" " +"text DEFAULT NULL" + ",");
            query.append(COL_COUNTRY +" " +"text DEFAULT NULL" + ",");
            query.append(COL_ZIPCODE +" " +"text DEFAULT NULL" + ",");
            query.append(COL_DESIGNATION +" " +"text DEFAULT NULL" + ",");
            query.append(COL_MOBILE_NO_1 +" " +"text DEFAULT NULL" + ",");
            query.append(COL_MOBILE_NO_2 +" " +"text DEFAULT NULL" + ",");
            query.append(COL_ALTERNET_NO +" " +"text DEFAULT NULL" + ",");
            query.append(COL_EMAIL_1 +" " +"text DEFAULT NULL" + ",");
            query.append(COL_EMAIL_2 +" " +"text DEFAULT NULL" + ",");
            query.append(COL_ENTRY_BY +" " +"text DEFAULT ContactEntry" + ",");
            query.append(COL_FLAG + " " + "integer DEFAULT 1");
            query.append(")");
            Log.i("@Transworld", "Query Create Table Contact Det - " + query.toString());
            return query.toString();
        }
    }

    public static class MeTableFollowUpDocs {
        public static final String TABLE_NAME = "tab_followup_docs";
        public static final String COL_SR_NO = "srno";
        public static final String COL_DOC_COUNT = "doccount";
        public static final String COL_FOLLOW_UP_DATE = "followupdate";
        public static final String COL_FOLLOW_UP_TIME = "followuptime";
        public static final String COL_DOC_TYPE_1 = "doctype1";
        public static final String COL_DOC_NAME_1 = "docname1";
        public static final String COL_DOC_PATH_1 = "docpath1";
        public static final String COL_DOC_TYPE_2 = "doctype2";
        public static final String COL_DOC_NAME_2 = "docname2";
        public static final String COL_DOC_PATH_2 = "docpath2";
        public static final String COL_DOC_TYPE_3 = "doctype3";
        public static final String COL_DOC_NAME_3 = "docname3";
        public static final String COL_DOC_PATH_3 = "docpath3";
        public static final String COL_DOC_TYPE_4 = "doctype4";
        public static final String COL_DOC_NAME_4 = "docname4";
        public static final String COL_DOC_PATH_4 = "docpath4";
        public static final String COL_DOC_TYPE_5 = "doctype5";
        public static final String COL_DOC_NAME_5 = "docname5";
        public static final String COL_DOC_PATH_5 = "docpath5";
        public static final String COL_DOC_TYPE_6 = "doctype6";
        public static final String COL_DOC_NAME_6 = "docname6";
        public static final String COL_DOC_PATH_6 = "docpath6";
        public static final String COL_FLAG = "flag";

        public static String createTable() {
            StringBuilder query = new StringBuilder();
            query.append("CREATE TABLE " + TABLE_NAME + "(");
            query.append(COL_SR_NO + " " + "integer, ");
            query.append(COL_DOC_COUNT + " " + "integer, ");
            query.append(COL_FOLLOW_UP_DATE + " " + "text DEFAULT NULL, ");
            query.append(COL_FOLLOW_UP_TIME + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_TYPE_1 + " " + "integer DEFAULT NULL" + ",");
            query.append(COL_DOC_NAME_1 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_PATH_1 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_TYPE_2 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_NAME_2 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_PATH_2 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_TYPE_3 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_NAME_3 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_PATH_3 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_TYPE_4 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_NAME_4 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_PATH_4 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_TYPE_5 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_NAME_5 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_PATH_5 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_TYPE_6 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_NAME_6 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_DOC_PATH_6 + " " + "text DEFAULT NULL" + ",");
            query.append(COL_FLAG + " " + "integer DEFAULT 1");
            query.append(")");
            Log.i("@Transworld", "Query Create Table Contact Det - " + query.toString());
            return query.toString();
        }
    }

    public static class MeTableMarketingRepMaster {
        public static final String TABLE_NAME = "tab_marketing_rep_master";
        public static final String COL_MARKETING_REP_CODE = "marketingRepCode";
        public static final String COL_MARKETING_REP_NAME = "marketingRepName";

        public static String createTable() {
            StringBuilder query = new StringBuilder();
            query.append("CREATE TABLE " + TABLE_NAME + "(");
            query.append(COL_MARKETING_REP_CODE + " " + "integer, ");
            query.append(COL_MARKETING_REP_NAME + " " + "text DEFAULT NULL");
            query.append(")");
            Log.i("@Transworld", "Query Create Table Marketing Rep Master - " + query.toString());
            return query.toString();
        }

    }

    public static class MeTableUserTracker {
        public static final String TABLE_NAME = "tab_usertracker";
        public static final String COL_INDEX_NO = "indexno";
        // public static final String COL_IMEI = "imei";
        //  public static final String COL_USERNAME = "username";
        //  public static final String COL_COMPANY_MASTER_ID = "companyMasterId";
        //  public static final String COL_MARKETING_REP_CODE = "marketingRepCode";
        public static final String COL_LATITUDE = "lat";
        public static final String COL_LONGITUDE = "long";
        public static final String COL_SOURCE = "source";
        public static final String COL_TIME_ZONE = "time";
        public static final String COL_SPEED = "speed";
        public static final String COL_ALTITUDE = "altitude";
        public static final String COL_STATUS="status";




        public static String createTable()
        {
            StringBuilder query =new StringBuilder();
            query.append("CREATE TABLE " + TABLE_NAME + "(");
            query.append(COL_INDEX_NO + " " + "integer PRIMARY KEY AUTOINCREMENT" + ",");
            //  query.append(COL_IMEI + " " + "text NOT NULL"+",");
            //  query.append(COL_USERNAME + " " + "text NOT NULL"+",");
            //  query.append(COL_COMPANY_MASTER_ID + " " + "integer"+",");
            //  query.append(COL_MARKETING_REP_CODE + " " + "integer"+",");
            query.append(COL_LATITUDE + " " + "double"+",");
            query.append(COL_LONGITUDE + " " + "double"+",");
            query.append(COL_SOURCE + " " + "text DEFAULT NotAvailable"+",");
            query.append(COL_TIME_ZONE + " " + "text" +",");
            query.append(COL_SPEED + " " + "text" +",");
            query.append(COL_ALTITUDE + " " + "text"+",");
            query.append(COL_STATUS + " " + "text");
            query.append(")");
            Log.i("@Transworld", "Query Create Table UserTracker - " + query.toString());
            return query.toString();
        }

    }

    public static class TableCheckIn {

        public static final String TABLE_NAME = "table_checkin";
        public static final String SR_NO = "s_no";
        public static final String COL_U_NAME = "user_name";
        public static final String COL_U_ID = "user_id";
        public static final String COL_CLIENT_NAME = "client_name";
        public static final String COL_COMMENT = "comment";
        public static final String COL_STATUS = "status";
        public static final String COL_DATE_TIME = "date_time";
        public static final String COL_LATITUDE = "latitude";
        public static final String COL_LONGITUDE = "longitude";
        public static final String COL_UPDATE_STATUS = "update_status";


        public static String createTable() {
            Log.e("checkin table", "table_checkin");
            try {
                StringBuilder query = new StringBuilder();
                query.append("CREATE TABLE " + TABLE_NAME + "(");
                query.append(SR_NO + " " + "integer PRIMARY KEY AUTOINCREMENT" + ",");
                // query.append(SR_NO + " " + "integer" + ",");
                query.append(COL_U_NAME + " " + "text" + ",");
                query.append(COL_U_ID + " " + "integer" + ",");
                query.append(COL_CLIENT_NAME + " " + "text" + ",");
                query.append(COL_COMMENT + " " + "text" + ",");
                query.append(COL_STATUS + " " + "text" + ",");
                query.append(COL_DATE_TIME + " " + "text" + ",");
                query.append(COL_LATITUDE + " " + "double" + ",");
                query.append(COL_LONGITUDE + " " + "double" + ",");
                query.append(COL_UPDATE_STATUS + " " + "text)");
                Log.e("query", query.toString());
                return query.toString();
            } catch (Exception e) {
                Log.e("chekin table", "exception-" + e.getMessage());
                return "exception";
            }
        }
    }


}
