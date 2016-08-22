package kr.tamiflus.sleepingbus.structs;

/**
 * Created by 김정욱 on 2016-08-12.
 *
 * 도착하고 있는 버스 정보를 저장
 */
public class ArrivingBus extends Bus {
    private String plateNo; // 차량 번호판. ex : 경기73바1577
    private int timeToWait; // 예상 대기 시간. 예를 들어 7분 후 도착이면 7 값을 가진다
    private String numOfStationsToWait;    // 몇 정거장 전. 버스가 2정거장 전에 있다면 2 값을 가진다

    public ArrivingBus() { }

    public ArrivingBus(String routeId, String routeName, String routeTypeCd, String plateNo, int timeToWait) {
        super(routeId, routeName, routeTypeCd);
        this.plateNo = plateNo;
        this.timeToWait = timeToWait;
    }

    public ArrivingBus(String routeId, String routeName, String routeTypeCd) {
        super(routeId, routeName, routeTypeCd);
    }

    public ArrivingBus(String districtCd, String regionName, String routeId, String routeName, String routeTypeCd, String routeTypeName) {
        super(districtCd, regionName, routeId, routeName, routeTypeCd, routeTypeName);
    }

    public ArrivingBus(String districtCd, String regionName, String routeId, String routeName, String routeTypeCd, String routeTypeName, String plateNo, int timeToWait) {
        super(districtCd, regionName, routeId, routeName, routeTypeCd, routeTypeName);
        this.plateNo = plateNo;
        this.timeToWait = timeToWait;
    }

    public ArrivingBus(Bus bus) {
        super(bus.getDistrictCd(), bus.getRegionName(), bus.getRouteId(), bus.getRouteName(), bus.getRouteTypeCd(), bus.getRouteTypeName());
    }

    public ArrivingBus(Bus bus, String plateNo, int timeToWait) {
        this(bus);
        this.plateNo = plateNo;
        this.timeToWait = timeToWait;
    }

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

    public String getNumOfStationsToWait() {
        return numOfStationsToWait;
    }

    public void setNumOfStationsToWait(String numOfStationsToWait) {
        this.numOfStationsToWait = numOfStationsToWait;
    }

    @Override
    public String toString() {
        return "ArrivingBus{" +
                "plateNo='" + plateNo + '\'' +
                ", timeToWait=" + timeToWait +
                ", numOfStationsToWait='" + numOfStationsToWait + '\'' +
                ", routeId='" + routeId + '\'' +
                ", routeName='" + routeName + '\'' +
                '}';
    }
}
