package org.spbstu.dell.metrics.data.saving;

import java.sql.Timestamp;
import java.util.Calendar;

public class InputData {
    private Integer idInputData;
    private Integer iTEquipmentPower;
    private Integer totalFacilityPower;
    private Integer iTEquipmentUtilization;
    private Timestamp time;
    private Calendar calendar;

    public InputData() {}

    public InputData(Integer idInputData, Integer iTEquipmentPower, Integer totalFacilityPower,
                     Integer iTEquipmentUtilization, Timestamp time, Calendar calendar) {
        this.idInputData = idInputData;
        this.iTEquipmentPower = iTEquipmentPower;
        this.totalFacilityPower = totalFacilityPower;
        this.iTEquipmentUtilization = iTEquipmentUtilization;
        this.time = time;
        this.calendar = calendar;
    }

    public Integer getIdInputData() {
        return idInputData;
    }

    public void setIdInputData(Integer idInputData) {
        this.idInputData = idInputData;
    }

    public Integer getITEquipmentPower() {
        return iTEquipmentPower;
    }

    public void setITEquipmentPower(Integer iTEquipmentPower) {
        this.iTEquipmentPower = iTEquipmentPower;
    }

    public Integer getTotalFacilityPower() {
        return totalFacilityPower;
    }

    public void setTotalFacilityPower(Integer totalFacilityPower) {
        this.totalFacilityPower = totalFacilityPower;
    }

    public Integer getiTEquipmentUtilization() {
        return iTEquipmentUtilization;
    }

    public void setiTEquipmentUtilization(Integer iTEquipmentUtilization) {
        this.iTEquipmentUtilization = iTEquipmentUtilization;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
