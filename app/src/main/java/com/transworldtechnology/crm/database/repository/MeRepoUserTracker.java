package com.transworldtechnology.crm.database.repository;

import com.transworldtechnology.crm.domain.MeUserTracker;

import java.util.List;

/**
 * Created by transworldtechnology on 12/4/16.
 */
public interface MeRepoUserTracker
{
    void saveUserCrediantials(Double lat, Double lng, String source, String timeZone, Float speed, Double altitude) throws Exception;

    List<MeUserTracker> sendLocations() throws Exception;

    void deleteTableData() throws Exception;

    Integer getcompanyMasterId() throws Exception;

    Integer getMrepCode() throws Exception;

    String getUserName() throws Exception;

    Double getLatitude() throws Exception;

    Double getLongitude() throws Exception;

    String getSource() throws Exception;

    String getTimeZone() throws Exception;

    String getImei() throws Exception;

    Float getSpeed() throws Exception;

    Double getAltitude() throws Exception;


}
