package com.transworldtechnology.crm.domain;

/**
 * Created by root on 16/4/16.
 */
public class MeUserTracker {
    private Integer companyMasterId;
    private Integer userId;
    private String userName;
    private Double loc_lat;
    private Double loc_long;
    private String loc_source;
    private Long timeZone;
    private String imei;
    private Float speed;
    private Double altitude;

    public Integer getCompanyMasterId() {
        return companyMasterId;
    }

    public void setCompanyMasterId(Integer companyMasterId) {
        this.companyMasterId = companyMasterId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getLoc_lat() {
        return loc_lat;
    }

    public void setLoc_lat(Double loc_lat) {
        this.loc_lat = loc_lat;
    }

    public Double getLoc_long() {
        return loc_long;
    }

    public void setLoc_long(Double loc_long) {
        this.loc_long = loc_long;
    }

    public String getLoc_source() {
        return loc_source;
    }

    public void setLoc_source(String loc_source) {
        this.loc_source = loc_source;
    }

    public Long getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(Long timeZone) {
        this.timeZone = timeZone;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return "MeUserTracker{" +
                "companyMasterId=" + companyMasterId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", loc_lat=" + loc_lat +
                ", loc_long=" + loc_long +
                ", loc_source='" + loc_source + '\'' +
                ", timeZone=" + timeZone +
                ", imei='" + imei + '\'' +
                ", speed=" + speed +
                ", altitude=" + altitude +
                '}';
    }
}