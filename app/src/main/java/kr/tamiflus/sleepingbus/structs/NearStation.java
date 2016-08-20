package kr.tamiflus.sleepingbus.structs;

/**
 * Created by tamiflus on 16. 8. 17..
 */
public class NearStation extends HomeObject {
    public BusStation st = new BusStation();
    public int distance;

    public NearStation() {
        super(1);
    }

    public NearStation(BusStation st) {
        super(1);
        this.st = st;
        this.distance = Integer.parseInt(st.getDist());
    }
}
