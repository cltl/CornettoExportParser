package vu.cornetto.cdb;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 3-jul-2008
 * Time: 11:00:24
 * To change this template use File | Settings | File Templates.
 */
public class CdbLuSemanticsVerb {
    String semReference;
    String semCountability;
    String semType;
    String semShift;
    String semSubclass;
    String semResume;


    public CdbLuSemanticsVerb() {
        this.semReference = "";
        this.semCountability = "";
        this.semType = "";
        this.semShift = "";
        this.semSubclass = "";
        this.semResume = "";
    }

    public String getSemReference() {
        return semReference;
    }

    public void setSemReference(String semReference) {
        this.semReference = semReference;
    }

    public String getSemCountability() {
        return semCountability;
    }

    public void setSemCountability(String semCountability) {
        this.semCountability = semCountability;
    }

    public String getSemType() {
        return semType;
    }

    public void setSemType(String semType) {
        this.semType = semType;
    }

    public String getSemShift() {
        return semShift;
    }

    public void setSemShift(String semShift) {
        this.semShift = semShift;
    }

    public String getSemSubclass() {
        return semSubclass;
    }

    public void setSemSubclass(String semSubclass) {
        this.semSubclass = semSubclass;
    }

    public String getSemResume() {
        return semResume;
    }

    public void setSemResume(String semResume) {
        this.semResume = semResume;
    }

}