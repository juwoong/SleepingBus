package kr.tamiflus.sleepingbus.structs;

import java.util.GregorianCalendar;

/**
 * Created by 김정욱 on 2016-08-12.
 */
public class BusRoute {
    private String regionName;
    private String routeId;
    private String routeName;
    private String routeTypeCd;
    private String routeTypeName;
    private ArrivingBus bus1;
    private ArrivingBus bus2;

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteTypeCd() {
        return routeTypeCd;
    }

    public void setRouteTypeCd(String routeTypeCd) {
        this.routeTypeCd = routeTypeCd;
    }

    public String getRouteTypeName() {
        return routeTypeName;
    }

    public void setRouteTypeName(String routeTypeName) {
        this.routeTypeName = routeTypeName;
    }

    @Override
    public String toString() {
        return "BusRoute{" +
                "regionName='" + regionName + '\'' +
                ", routeId='" + routeId + '\'' +
                ", routeName='" + routeName + '\'' +
                ", routeTypeCd='" + routeTypeCd + '\'' +
                ", routeTypeName='" + routeTypeName + '\'' +
                '}';
    }

    public ArrivingBus getBus1() {
        return bus1;
    }

    public void setBus1(ArrivingBus bus1) {
        this.bus1 = bus1;
    }

    public ArrivingBus getBus2() {
        return bus2;
    }

    public void setBus2(ArrivingBus bus2) {
        this.bus2 = bus2;
    }
}
