package vu.cornetto.cdb;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 6-jun-2006
 * Time: 18:00:37
 * To change this template use Options | File Templates.
 */
public class AlsStatus {

        String type;
        String who;
        String date;
        String okay;
        double score;

    public AlsStatus() {
        this.type = "";
        this.who = "";
        this.date = "";
        this.okay = "";
        this.score = 0;
    }

    public AlsStatus(String type, String who, String date, String okay) {
        this.type = type;
        this.who = who;
        this.date = date;
        this.okay = okay;
        this.score = 0;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOkay() {
        return okay;
    }

    public void setOkay(String okay) {
        this.okay = okay;
    }
}
