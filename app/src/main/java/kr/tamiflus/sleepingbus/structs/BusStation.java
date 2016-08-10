package kr.tamiflus.sleepingbus.structs;

/**
 * Created by juwoong on 16. 7. 14..
 */
public class BusStation {
    private String name;
    private String id;
    private String region;
    private String code;
    private String x, y;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("{%s, %s, %s}", name, x, y);
    }
}
