package vu.cornetto.cdb;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 16-jun-2006
 * Time: 9:54:12
 * To change this template use Options | File Templates.
 */
public class CdbCid {

    String cid;
    String c_pos;
    String c_sy_id;
    String c_lu_id;
    int c_seq_nr;
    String c_form;
    String r_lu_id;
    int r_seq_nr;
    int d_seq_nr;
    String d_sy_id;
    String d_lu_id;
    double score;
    String date;
    String status;
    String name;
    boolean selected;


    public CdbCid() {
        this.c_pos="";
        this.cid="";
        this.c_form="";
        this.c_lu_id="";
        this.c_seq_nr=-1;
        this.c_sy_id="";
        this.d_lu_id="";
        this.d_seq_nr=-1;
        this.d_sy_id="";
        this.r_lu_id="";
        this.r_seq_nr=-1;
        this.score=-1;
        this.date="";
        this.status="";
        this.name="";
        selected = false;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getC_pos() {
        return c_pos;
    }

    public void setC_pos(String c_pos) {
        this.c_pos = c_pos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getC_sy_id() {
        return c_sy_id;
    }

    public void setC_sy_id(String c_sy_id) {
        this.c_sy_id = c_sy_id;
    }

    public String getC_lu_id() {
        return c_lu_id;
    }

    public void setC_lu_id(String c_lu_id) {
        this.c_lu_id = c_lu_id;
    }

    public int getC_seq_nr() {
        return c_seq_nr;
    }

    public void setC_seq_nr(int c_seq_nr) {
        this.c_seq_nr = c_seq_nr;
    }

    public String getC_form() {
        return c_form;
    }

    public void setC_form(String c_form) {
        this.c_form = c_form;
    }

    public String getR_lu_id() {
        return r_lu_id;
    }

    public void setR_lu_id(String r_lu_id) {
        this.r_lu_id = r_lu_id;
    }

    public int getR_seq_nr() {
        return r_seq_nr;
    }

    public void setR_seq_nr(int r_seq_nr) {
        this.r_seq_nr = r_seq_nr;
    }

    public int getD_seq_nr() {
        return d_seq_nr;
    }

    public void setD_seq_nr(int d_seq_nr) {
        this.d_seq_nr = d_seq_nr;
    }

    public String getD_sy_id() {
        return d_sy_id;
    }

    public void setD_sy_id(String d_sy_id) {
        this.d_sy_id = d_sy_id;
    }

    public String getD_lu_id() {
        return d_lu_id;
    }

    public void setD_lu_id(String d_lu_id) {
        this.d_lu_id = d_lu_id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toXmlString () {
        String str = "<cid cid_id=\""+this.cid+"\"";
        str += " c_lu_id=\""+this.c_lu_id+"\"";
        str += " c_sy_id=\""+this.c_sy_id+"\"";
        str += " pos=\""+this.c_pos+"\"";
        str += " form=\""+this.c_form+"\"";
        str += " seq_nr=\""+this.c_seq_nr+"\"";
        str += " r_lu_id=\""+this.r_lu_id+"\"";
        str += " r_seq_nr=\""+this.r_seq_nr+"\"";
        str += " d_lu_id=\""+this.d_lu_id+"\"";
        str += " d_seq_nr=\""+this.d_seq_nr+"\"";
        str += " d_sy_id=\""+this.d_sy_id+"\"";
        str += " score=\""+this.score+"\"";
        str += " date=\""+this.date+"\"";
        str += " name=\""+this.name+"\"";
        str += " status=\""+this.status+"\"";
        str += " selected=\""+this.selected+"\"";
        str += "/>\n";
        return str;
    }
}