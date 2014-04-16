package vu.cornetto.dwn;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 4-sep-2006
 * Time: 21:04:00
 * To change this template use Options | File Templates.
 */
public class DwnId {
    private String form;
    private String dwnSynsetId;
    private String vlisLuId;
    private String seqNr;
    private String pos;


    public DwnId() {
        this.form = "";
        this.dwnSynsetId = "";
        this.vlisLuId = "";
        this.seqNr = "";
        this.pos = "";
    }

    public DwnId(String form, String seqNr, String pos, String dwnSynsetId, String vlisLuId) {
        this.form = form;
//        this.dwnSynsetId = dwnSynsetId;

/*
        if (dwnSynsetId.startsWith("n_")) {
            if (pos.equals("NOUN")) {
                this.dwnSynsetId = "n_n-"+this.dwnSynsetId.substring(2);
                this.vlisLuId = vlisLuId.substring(0, 2)+"n-"+vlisLuId.substring(2);
            }
            else if (pos.equals("VERB")) {
                this.dwnSynsetId = "n_v-"+this.dwnSynsetId.substring(2);
                this.vlisLuId = vlisLuId.substring(0, 2)+"v-"+vlisLuId.substring(2);
            }
            else if (pos.equals("ADJECTIVE")) {
                this.dwnSynsetId = "n_a-"+this.dwnSynsetId.substring(2);
                this.vlisLuId = vlisLuId.substring(0, 2)+"a-"+vlisLuId.substring(2);
            }
            else {
                this.dwnSynsetId = "n_r-"+this.dwnSynsetId.substring(2);
                this.vlisLuId = vlisLuId.substring(0, 2)+"r-"+vlisLuId.substring(2);
            }
        }
*/
        this.dwnSynsetId = dwnSynsetId;
        this.vlisLuId = vlisLuId;
        if (dwnSynsetId.indexOf("-")==-1) {
            if (pos.equals("NOUN")) {
                this.dwnSynsetId = dwnSynsetId.substring(0, 2)+"n-"+dwnSynsetId.substring(2);
            }
            else if (pos.equals("VERB")) {
                this.dwnSynsetId = dwnSynsetId.substring(0, 2)+"v-"+dwnSynsetId.substring(2);
            }
            else if (pos.equals("ADJECTIVE")) {
                this.dwnSynsetId = dwnSynsetId.substring(0, 2)+"a-"+dwnSynsetId.substring(2);
            }
            else {
                this.dwnSynsetId = dwnSynsetId.substring(0, 2)+"r-"+dwnSynsetId.substring(2);
            }
        }
        if (vlisLuId.indexOf("-")==-1) {
            if (pos.equals("NOUN")) {
                this.vlisLuId = vlisLuId.substring(0, 2)+"n-"+vlisLuId.substring(2);
            }
            else if (pos.equals("VERB")) {
                this.vlisLuId = vlisLuId.substring(0, 2)+"v-"+vlisLuId.substring(2);
            }
            else if (pos.equals("ADJECTIVE")) {
                this.vlisLuId = vlisLuId.substring(0, 2)+"a-"+vlisLuId.substring(2);
            }
            else {
                this.vlisLuId = vlisLuId.substring(0, 2)+"r-"+vlisLuId.substring(2);
            }
        }
        this.seqNr = seqNr;
        this.pos = pos;
    }

    public String toString () {
        String str = "<dwnId form=\""+form+"\" seq_nr=\""+seqNr+"\" pos=\""+pos+"\" vlisId=\""+vlisLuId+"\" synsetId=\""+dwnSynsetId+"\"/>\n";
        return str;
    }

    public String toString (String centralEntry, String centralSense, String centralSynset) {
        String str = "<dwnId d_form=\""+form+"\" d_seq_nr=\""+seqNr+"\" d_pos=\""+pos+"\" vlisId=\""+vlisLuId+"\" synsetId=\""+dwnSynsetId+"\""+" centralEntry=\""+centralEntry+"\""+" centralSense=\""+centralSense+"\""+" centralSynset=\""+centralSynset+"\""+"/>\n";
        return str;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getDwnSynsetId() {
        return dwnSynsetId;
    }

    public void setDwnSynsetId(String dwnSynsetId) {
        this.dwnSynsetId = dwnSynsetId;
    }

    public String getVlisLuId() {
        return vlisLuId;
    }

    public void setVlisLuId(String vlisLuId) {
        this.vlisLuId = vlisLuId;
    }

    public String getSeqNr() {
        return seqNr;
    }

    public void setSeqNr(String seqNr) {
        this.seqNr = seqNr;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
