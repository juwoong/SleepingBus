package kr.tamiflus.sleepingbus.structs;

/**
 * Created by tamiflus on 16. 8. 20..
 */
public class Bus {
    protected String districtCd;
    protected String regionName;
    protected String routeId;
    protected String routeName;
    protected String routeTypeCd, routeTypeName;

    public Bus() {

    }

    public Bus(String routeId, String routeName, String routeTypeCd) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.routeTypeCd = routeTypeCd;
    }

    public Bus(String districtCd, String regionName, String routeId, String routeName, String routeTypeCd, String routeTypeName) {
        this(routeId, routeName, routeTypeCd);
        this.districtCd = districtCd;
        this.regionName = regionName;
        this.routeTypeName = routeTypeName;
    }

    public String getDistrictCd() {
        return districtCd;
    }

    public void setDistrictCd(String districtCd) {
        this.districtCd = districtCd;
    }

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
}
