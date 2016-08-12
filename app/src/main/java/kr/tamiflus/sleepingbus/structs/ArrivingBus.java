package kr.tamiflus.sleepingbus.structs;

/**
 * Created by 김정욱 on 2016-08-12.
 *
 * 도착하고 있는 버스 정보를 저장
 */
public class ArrivingBus {
    private String plateNo; // 차량 번호판. ex : 경기73바1577
    private int timeToWait; // 예상 대기 시간. 예를 들어 7분 후 도착이면 7 값을 가짐

    public ArrivingBus() { }

    public ArrivingBus(String plateNo, int timeToWait) {
        this.plateNo = plateNo;
        this.timeToWait = timeToWait;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public int getTimeToWait() {
        return timeToWait;
    }

    public void setTimeToWait(int timeToWait) {
        this.timeToWait = timeToWait;
    }
}
