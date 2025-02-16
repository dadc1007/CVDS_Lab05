package edu.eci.UniReserva.UniReserva_Backend.model.enums;

import java.time.LocalTime;

public enum TimeBlock {
    BLOCK_1(1, LocalTime.of(7, 0), LocalTime.of(8, 30)),
    BLOCK_2(2, LocalTime.of(8, 30), LocalTime.of(10, 0)),
    BLOCK_3(3, LocalTime.of(10, 0), LocalTime.of(11, 30)),
    BLOCK_4(4, LocalTime.of(11, 30), LocalTime.of(13, 0)),
    BLOCK_5(5, LocalTime.of(13, 0), LocalTime.of(14, 30)),
    BLOCK_6(6, LocalTime.of(14, 30), LocalTime.of(16, 0)),
    BLOCK_7(7, LocalTime.of(16, 0), LocalTime.of(17, 30)),
    BLOCK_8(8, LocalTime.of(17, 30), LocalTime.of(19, 0)),
    ;

    private final int blockNumber;
    private final LocalTime startTime;
    private final LocalTime endTime;

    TimeBlock(int blockNumber, LocalTime startTime, LocalTime endTime) {
        this.blockNumber = blockNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
