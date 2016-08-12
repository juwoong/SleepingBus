package kr.tamiflus.sleepingbus.structs;

/**
 * Created by 김정욱 on 2016-08-12.
 */
public class Time {
    private int hour = 0;   // hour (0~24)
    private int minute = 0; // minute (0~59)

    public Time() { }
    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    /**
     * Time1과 Time2 사이 시간차를 계산해서 분 단위로 리턴한다.
     * @return 시간차(분)
     */
    public static int calculateTimeDifference(Time t1, Time t2) {
        return Math.abs(t1.getHour()*60 + t1.getMinute() - t2.getHour() - t2.getHour());
    }
}
