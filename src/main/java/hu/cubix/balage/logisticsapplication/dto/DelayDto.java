package hu.cubix.balage.logisticsapplication.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DelayDto {
    @NotNull
    @Positive
    private long mileStoneId;
    @NotNull
    @Positive
    private int delay;

    public DelayDto() {
    }

    public DelayDto(long mileStoneId, int delay) {
        this.mileStoneId = mileStoneId;
        this.delay = delay;
    }

    public long getMileStoneId() {
        return mileStoneId;
    }

    public void setMileStoneId(long mileStoneId) {
        this.mileStoneId = mileStoneId;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}