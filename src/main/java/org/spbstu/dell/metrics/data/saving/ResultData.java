package org.spbstu.dell.metrics.data.saving;

import java.sql.Timestamp;
import java.util.Calendar;

public class ResultData {
    private Integer idResultData;
    private Integer idInputData;
    private Double pue;
    private Double cpe;
    private Timestamp time;
    private Calendar calendar;

    public ResultData(Integer idResultData, Integer idInputData, Double pue, Double cpe, Timestamp time, Calendar calendar) {
        this.idResultData = idResultData;
        this.idInputData = idInputData;
        this.pue = pue;
        this.cpe = cpe;
        this.time = time;
        this.calendar = calendar;
    }

    public ResultData() {}
    public Integer getIdResultData() {
        return idResultData;
    }

    public void setIdResultData(Integer idResultData) {
        this.idResultData = idResultData;
    }

    public Integer getIdInputData() {
        return idInputData;
    }

    public void setIdInputData(Integer idInputData) {
        this.idInputData = idInputData;
    }

    public Double getPue() {
        return pue;
    }

    public void setPue(Double pue) {
        this.pue = pue;
    }

    public Double getCpe() {
        return cpe;
    }

    public void setCpe(Double cpe) {
        this.cpe = cpe;
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

