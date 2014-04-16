package vu.cornetto.cdb;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 21-apr-2006
 * Time: 19:40:15
 * To change this template use Options | File Templates.
 */
public class Syn {
    String c_cid_id;
    String c_lu_id;
    String previewtext;
    String subsense;
    String status;

    public Syn(String c_cid_id, String c_lu_id, String previewtext, String status) {
        this.c_cid_id = c_cid_id;
        this.c_lu_id = c_lu_id;
        this.previewtext = previewtext;
        this.status = status;
    }

    public Syn() {
        this.c_cid_id = "";
        this.c_lu_id = "";
        this.previewtext = "";
        this.subsense = "";
        this.status = "";
    }

    public String getC_cid_id() {
        return c_cid_id;
    }

    public void setC_cid_id(String c_cid_id) {
        this.c_cid_id = c_cid_id;
    }

    public String getC_lu_id() {
        return c_lu_id;
    }

    public void setC_lu_id(String c_lu_id) {
        this.c_lu_id = c_lu_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubsense() {
        return subsense;
    }

    public void setSubsense(String subsense) {
        this.subsense = subsense;
    }

    public String toString() {
        //      <synonym c_lu_id-previewtext="balie:2" c_lu_id="r_n-5798"/>
        //<synonym c_lu_id="d_n-45981" c_cid_id="" status=""/>
        String str = "<synonym"
                                +" c_cid_id=\""+this.getC_cid_id()+"\""
                                +" c_lu_id=\""+this.getC_lu_id()+"\"";
        if (!subsense.isEmpty()) {
                               str += " subsense=\""+this.getSubsense()+"\"";
        }
        if (!this.getPreviewtext().isEmpty()) {
            str += " c_lu_id-previewtext=\""+this.getPreviewtext()+"\"";
        }
        if (!this.getStatus().isEmpty()) {
                               str +=" status=\""+this.getStatus()+"\"";
        }
        str +="/>\n";
        return str;
    }

   public String toStringWithoutCid() {
        //      <synonym c_lu_id-previewtext="balie:2" c_lu_id="r_n-5798"/>
        //<synonym c_lu_id="d_n-45981" c_cid_id="" status=""/>
        String str = "<synonym"
                                +" c_lu_id=\""+this.getC_lu_id()+"\"";
        if (!subsense.isEmpty()) {
                               str += " subsense=\""+this.getSubsense()+"\"";
        }
        if (!this.getPreviewtext().isEmpty()) {
            str += " c_lu_id-previewtext=\""+this.getPreviewtext()+"\"";
        }
        if (!this.getStatus().isEmpty()) {
                               str +=" status=\""+this.getStatus()+"\"";
        }
        str +="/>\n";
        return str;
    }

    public String toBareString() {
        //      <synonym c_lu_id-previewtext="balie:2" c_lu_id="r_n-5798"/>
        //<synonym c_lu_id="d_n-45981" c_cid_id="" status=""/>
        String str = "<synonym"
                                +" c_cid_id=\""+this.getC_cid_id()+"\""
                                +" c_lu_id=\""+this.getC_lu_id()+"\"";
        if (!subsense.isEmpty()) {
            str += " subsense=\""+this.getSubsense()+"\"";
        }
        if (!this.getStatus().isEmpty()) {
            str +=" status=\""+this.getStatus()+"\"";
        }
        str +="/>\n";
        return str;
    }

    public String getPreviewtext() {
        return previewtext;
    }

    public void setPreviewtext(String previewtext) {
        this.previewtext = previewtext;
    }

    public String getForm () {
        int idx = this.previewtext.indexOf(":");
        if (idx>-1) {
            return this.previewtext.substring(0, idx).trim();
        }
        else {
            return this.previewtext;
        }
    }

    public String getSeqNr () {
        int idx = this.previewtext.indexOf(":");
        if (idx>-1) {
            return this.previewtext.substring(idx+1).trim();
        }
        else {
            return this.previewtext;
        }
    }
}
