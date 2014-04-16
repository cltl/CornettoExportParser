package vu.cornetto.dwn;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 19-apr-2006
 * Time: 18:29:43
 * To change this template use Options | File Templates.
 */
public class EquivalenceRelation extends Relation{

    ////<ELR>ENG15-01371393-v<TYPE>eq_near_synonym</TYPE></ELR>
    private String TYPE;
    private String TARGET;
    private String SOURCE;
    private String STATUS;
    private double PROB;

    public EquivalenceRelation() {
        TYPE = "";
        TARGET = "";
        SOURCE = "";
        STATUS = "";
        PROB = 0;
    }

    public EquivalenceRelation(String TYPE, String TARGET) {
        this.TYPE = TYPE;
        this.TARGET = TARGET;
        SOURCE = "";
        STATUS = "";
        PROB = 0;
    }

    public String getSOURCE() {
        return SOURCE;
    }

    public void setSOURCE(String SOURCE) {
        this.SOURCE = SOURCE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public double getPROB() {
        return PROB;
    }

    public void setPROB(double PROB) {
        this.PROB = PROB;
    }
}
