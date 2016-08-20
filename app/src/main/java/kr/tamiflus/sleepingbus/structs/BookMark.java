package kr.tamiflus.sleepingbus.structs;

/**
 * Created by tamiflus on 16. 8. 20..
 */
public class BookMark extends HomeObject{
    public String name; //BookmarkName
    public BusStation startSt, endSt;
    public ArrivingBus arrivingBus;

    public BookMark() {
        super(3);
    }

    public BookMark(String name) {
        super(3);
        this.name = name;
    }

    public BookMark(String name, BusStation startSt, BusStation endSt) {
        this(name);
        this.startSt = startSt;
        this.endSt = endSt;
    }

    public BookMark(String name, BusStation startSt, BusStation endSt, ArrivingBus arrivingBus) {
        this(name, startSt, endSt);
        this.arrivingBus = arrivingBus;
    }
}
