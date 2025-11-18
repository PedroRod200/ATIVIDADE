package com.pedrorod.atividade.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExitResponse {
    private String plate;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private long hours;
    private BigDecimal ratePerHour;
    private BigDecimal total;

    // getters/setters
    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    public long getHours() { return hours; }
    public void setHours(long hours) { this.hours = hours; }
    public BigDecimal getRatePerHour() { return ratePerHour; }
    public void setRatePerHour(BigDecimal ratePerHour) { this.ratePerHour = ratePerHour; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}