package kr.tamiflus.sleepingbus.structs;

/**
 * Created by tamiflus on 16. 8. 18..
 */
public class NearTwoStation extends HomeObject {
    public BusStation s1, s2;
    public int d1, d2;
    public String name;

    public NearTwoStation() {
        super(2);
    }

    public NearTwoStation(BusStation s1, BusStation s2) {
        super(2);
        this.s1 = s1;
        this.s2 = s2;
    }
}
