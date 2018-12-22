package com.transworldtechnology.crm.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Map;

/**
 * Created by root on 29/4/16.
 */
public class MePrefs {
    public static final String KEY_COMPANY_NAME = "companyName";
    public static final String KEY_CONTACT_PERSON = "contactPerson";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_SALES_CUST_CODE = "salesCustName";
    public static final String KEY_CUSTOMER_CODE = "custCode";
    public static final String KEY_OPPORTUNITY_CODE = "opportunityCode";
    public static final String KEY_IMAGE_PATH = "IMAGE_PATH";
    public static final String PREFS_STORE_COMPANY = "STORE_COMPANY";
    public static final String PREFS_STORE_IMAGE = "STORE_IMAGE";
    public static final String PREFS_STORE_APPOINTMENT_DETAILS = "STORE_APPOINTMENT";
    public static final String PREFS_STORE_COMPANY_TEMP = "STORE_COMPANY_TEMP" ;
    // public static final String PREFS_STORE_COMPANY_APP = "STORE_COMPANY_APP";

    public static void saveImage(Context context, String selectedImagePath) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_STORE_IMAGE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_IMAGE_PATH, selectedImagePath);
        editor.commit();
    }

    public static String getKeyImageName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_IMAGE, Context.MODE_PRIVATE);
        String imagePath = sharedPreferences.getString(KEY_IMAGE_PATH, "none");
        sharedPreferences = null;
        return imagePath;
    }

    public static void saveAppointment(Context context, Map<String, Object> map) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_STORE_APPOINTMENT_DETAILS, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        for (String key : map.keySet()) {
            editor.putString(key, (String) map.get(key));
        }
        editor.commit();
    }

    public static Map<String, Object> getKeyAppointmentDetails(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_APPOINTMENT_DETAILS, Context.MODE_PRIVATE);
        Map<String, Object> map = (Map<String, Object>) sharedPreferences.getAll();
        return map;
    }

    public static void clearSharedPrefsAppointment(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_APPOINTMENT_DETAILS, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

    public static void saveCompanyInfo(Context context, List<Map<String, Object>> listCompanies, Integer position) {
        //  position = 0;
        SharedPreferences pref = context.getSharedPreferences(PREFS_STORE_COMPANY, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
//        Log.i("CompanyName",  listCompanies.get(position).get("companyName")!=null? (String) listCompanies.get(position).get("companyName") : "");
        editor.putString(KEY_COMPANY_NAME, (String) listCompanies.get(position).get("companyName"));
        editor.putInt(KEY_SALES_CUST_CODE, (Integer) listCompanies.get(position).get("salesCustomerCode"));
        editor.putInt(KEY_CUSTOMER_CODE, (Integer) listCompanies.get(position).get("customerCode"));
        editor.putInt(KEY_OPPORTUNITY_CODE, (Integer) listCompanies.get(position).get("opportunityCode"));
        String mb[] =  listCompanies.get(position).get("companyInfo").toString().split(",");
        editor.putString(KEY_CONTACT_PERSON,mb[4]);
        editor.putString(KEY_MOBILE,mb[5]);
        editor.commit();
    }

    public static void saveCompanyInfoTemp(Context context, List<Map<String, Object>> listCompanies, Integer position) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_STORE_COMPANY_TEMP, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
//        Log.i("CompanyName",  listCompanies.get(position).get("companyName")!=null? (String) listCompanies.get(position).get("companyName") : "");
        editor.putString(KEY_COMPANY_NAME, (String) listCompanies.get(position).get("companyName"));
        editor.putInt(KEY_SALES_CUST_CODE, (Integer) listCompanies.get(position).get("salesCustomerCode"));
        editor.putInt(KEY_CUSTOMER_CODE, (Integer) listCompanies.get(position).get("customerCode"));
        editor.putInt(KEY_OPPORTUNITY_CODE, (Integer) listCompanies.get(position).get("opportunityCode"));
        editor.commit();
    }

    public static String getKeySelectedCompany(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_COMPANY, Context.MODE_PRIVATE);
        String info = sharedPreferences.getString(KEY_CONTACT_PERSON, "none") + ", " + sharedPreferences.getString(KEY_MOBILE, "none");
        sharedPreferences = null;
        return info;
    }


    public static String getKeyCompanyNameTemp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_COMPANY_TEMP, Context.MODE_PRIVATE);
        String companyName = sharedPreferences.getString(KEY_COMPANY_NAME, "none");
        sharedPreferences = null;
        return companyName;
    }

    public static Integer getKeySaleCustCodeTemp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_COMPANY_TEMP, Context.MODE_PRIVATE);
        Integer salesCustCode = sharedPreferences.getInt(KEY_SALES_CUST_CODE, -1);
        sharedPreferences = null;
        return salesCustCode;
    }

    public static Integer getKeyCustomerCodeTemp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_COMPANY_TEMP, Context.MODE_PRIVATE);
        Integer customerCode = sharedPreferences.getInt(KEY_CUSTOMER_CODE, -1);
        sharedPreferences = null;
        return customerCode;
    }

    public static Integer getKeyOpportunityCodeTemp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_COMPANY_TEMP, Context.MODE_PRIVATE);
        Integer opportunityCode = sharedPreferences.getInt(KEY_OPPORTUNITY_CODE, -1);
        sharedPreferences = null;
        return opportunityCode;
    }

    public static void saveCompanyInfoLocal(Context context, String companyInfo) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_STORE_COMPANY, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String[] str = companyInfo.split(",");
        editor.putString(KEY_COMPANY_NAME, str[0]);
        editor.putInt(KEY_SALES_CUST_CODE, Integer.parseInt(str[3].trim()));
        editor.putInt(KEY_CUSTOMER_CODE, Integer.parseInt(str[2].trim()));
        editor.putInt(KEY_OPPORTUNITY_CODE, Integer.parseInt(str[1].trim()));
        editor.commit();
    }

    public static String getKeyCompanyName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_COMPANY, Context.MODE_PRIVATE);
        String companyName = sharedPreferences.getString(KEY_COMPANY_NAME, "none");
        sharedPreferences = null;
        return companyName;
    }

    public static Integer getKeySaleCustCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_COMPANY, Context.MODE_PRIVATE);
        Integer salesCustCode = sharedPreferences.getInt(KEY_SALES_CUST_CODE, -1);
        sharedPreferences = null;
        return salesCustCode;
    }

    public static Integer getKeyCustomerCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_COMPANY, Context.MODE_PRIVATE);
        Integer customerCode = sharedPreferences.getInt(KEY_CUSTOMER_CODE, -1);
        sharedPreferences = null;
        return customerCode;
    }

    public static Integer getKeyOpportunityCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_COMPANY, Context.MODE_PRIVATE);
        Integer opportunityCode = sharedPreferences.getInt(KEY_OPPORTUNITY_CODE, -1);
        sharedPreferences = null;
        return opportunityCode;
    }

    //Removing all preferences:
    public static void clearSharedPrefs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_COMPANY, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

    public static void clearSharedPrefsTemp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_COMPANY_TEMP, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }


}
