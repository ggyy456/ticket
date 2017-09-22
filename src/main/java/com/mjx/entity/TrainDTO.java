package com.mjx.entity;

/**
 * Created by Administrator on 2017/9/15 0015.
 */
public class TrainDTO {
    private Integer trainId;
    private String trainNo;
    private String trainType;
    private String beginStation;
    private String endStation;
    private String beginTime;
    private String endTime;
    private String takeTime;

    private String firstSeat;
    private String secondSeat;
    private String businessSeat;
    private String hardSeat;
    private String softSeat;
    private String hardSleep;
    private String softSleep;
    private String noSeat;

    private Integer ticketId;
    private Integer userId;
    private String ticketType;
    private String isSell;

    //查询条件
    private String queryTime1;
    private String queryTime2;

    public String getIsSell() {
        return isSell;
    }

    public void setIsSell(String isSell) {
        this.isSell = isSell;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getQueryTime1() {
        return queryTime1;
    }

    public void setQueryTime1(String queryTime1) {
        this.queryTime1 = queryTime1;
    }

    public String getQueryTime2() {
        return queryTime2;
    }

    public void setQueryTime2(String queryTime2) {
        this.queryTime2 = queryTime2;
    }

    public Integer getTrainId() {
        return trainId;
    }

    public void setTrainId(Integer trainId) {
        this.trainId = trainId;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getBeginStation() {
        return beginStation;
    }

    public void setBeginStation(String beginStation) {
        this.beginStation = beginStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(String takeTime) {
        this.takeTime = takeTime;
    }

    public String getFirstSeat() {
        return firstSeat;
    }

    public void setFirstSeat(String firstSeat) {
        this.firstSeat = firstSeat;
    }

    public String getSecondSeat() {
        return secondSeat;
    }

    public void setSecondSeat(String secondSeat) {
        this.secondSeat = secondSeat;
    }

    public String getBusinessSeat() {
        return businessSeat;
    }

    public void setBusinessSeat(String businessSeat) {
        this.businessSeat = businessSeat;
    }

    public String getHardSeat() {
        return hardSeat;
    }

    public void setHardSeat(String hardSeat) {
        this.hardSeat = hardSeat;
    }

    public String getSoftSeat() {
        return softSeat;
    }

    public void setSoftSeat(String softSeat) {
        this.softSeat = softSeat;
    }

    public String getHardSleep() {
        return hardSleep;
    }

    public void setHardSleep(String hardSleep) {
        this.hardSleep = hardSleep;
    }

    public String getSoftSleep() {
        return softSleep;
    }

    public void setSoftSleep(String softSleep) {
        this.softSleep = softSleep;
    }

    public String getNoSeat() {
        return noSeat;
    }

    public void setNoSeat(String noSeat) {
        this.noSeat = noSeat;
    }
}
