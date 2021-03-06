package vu.cornetto.dwn;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 19-apr-2006
 * Time: 18:29:36
 * To change this template use Options | File Templates.
 */
public class InternalRelation extends Relation{
            ////<ILR><TYPE>hypernym</TYPE>DUT-00003904-v</ILR><ILR><TYPE>hypernym</TYPE>ENG15-01472320-v</ILR>

    private String TYPE;
    private String TARGET;
    private String SOURCE;
    private String STATUS;
    private double PROB;

    public InternalRelation() {
        this.TYPE = "";
        this.TARGET = "";
        SOURCE = "";
        STATUS = "";
        PROB = 0;
    }

    public InternalRelation(String TYPE, String TARGET) {
        this.TYPE = TYPE;
        this.TARGET = TARGET;
        SOURCE = "";
        STATUS = "";
        PROB = 0;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getTARGET() {
        return TARGET;
    }

    public void setTARGET(String TARGET) {
        this.TARGET = TARGET;
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
