package vu.cornetto.pwn;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 20-apr-2006
 * Time: 8:00:20
 * To change this template use Options | File Templates.
 */
public class PwnVersionMapping {

    private String target;
    private double score;

    public PwnVersionMapping(double score, String target) {
        this.score = score;
        this.target = target;
    }

    public double getScore() {
        return score;
    }

    public String getTarget() {
        return target;
    }
}
