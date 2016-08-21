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
}
