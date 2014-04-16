package vu.cornetto.cdb;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 3-jul-2008
 * Time: 10:55:15
 * To change this template use File | Settings | File Templates.
 */
public class CdbLuForm {

    String seqNr;
    String formLength;
    String formCat;
    String formSpelling;
    String luId;

    public CdbLuForm() {
        this.luId = "";
        this.seqNr="";
        this.formLength = "";
        this.formCat = "";
        this.formSpelling = "";
    }

    public String getLuId() {
        return luId;
    }

    public void setLuId(String luId) {
        this.luId = luId;
    }

    public String getSeqNr() {
        return seqNr;
    }

    public void setSeqNr(String seqNr) {
        this.seqNr = seqNr;
    }

    public String getFormLength() {
        return formLength;
    }

    public void setFormLength(String formLength) {
        this.formLength = formLength;
    }

    public String getFormCat() {
        return formCat;
    }

    public void setFormCat(String formCat) {
        this.formCat = formCat;
    }

    public String getFormSpelling() {
        return formSpelling;
    }

    public void setFormSpelling(String formSpelling) {
        this.formSpelling = formSpelling;
    }
}
