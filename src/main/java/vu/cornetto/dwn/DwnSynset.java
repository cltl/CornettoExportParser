package vu.cornetto.dwn;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 19-apr-2006
 * Time: 18:11:20
 * To change this template use Options | File Templates.
 */
public class DwnSynset {
            ///<SYNSET><ID>DUT-00000001-v</ID>
        ////<POS>v</POS>
        ////<SYNONYM><LITERAL>doorlaten<SENSE>1</SENSE></LITERAL></SYNONYM>
        ////<ILR><TYPE>hypernym</TYPE>DUT-00003904-v</ILR><ILR><TYPE>hypernym</TYPE>ENG15-01472320-v</ILR>
        ////<ELR>ENG15-01371393-v<TYPE>eq_near_synonym</TYPE></ELR>
        ////<DEF>2ndOrderEntity;BoundedEvent;Cause;Dynamic;SituationType;Static;</DEF>
        ////</SYNSET>

    private String ID;
    private String POS;
    private String DEF;
    public ArrayList SYNS;
    public ArrayList IRLS;
    public ArrayList ERLS;
    public ArrayList DRLS;
    public ArrayList SRLS;

    public DwnSynset() {
        this.ID ="";
        this.POS = "";
        this.DEF = "";
        SYNS = new ArrayList();
        IRLS = new ArrayList();
        ERLS = new ArrayList();
        DRLS = new ArrayList();
        SRLS = new ArrayList();
    }

    public DwnSynset(String ID, String POS) {
        this.ID = ID;
        this.POS = POS;
        DEF = "";
        SYNS = new ArrayList();
        IRLS = new ArrayList();
        ERLS = new ArrayList();
        DRLS = new ArrayList();
        SRLS = new ArrayList();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPOS() {
        return POS;
    }

    public void setPOS(String POS) {
        this.POS = POS;
    }

    public String getDEF() {
        return DEF;
    }

    public void setDEF(String DEF) {
        this.DEF = DEF;
    }

    public void addToSYNS (Synonym syn) {
        if (!SYNS.contains(syn)) {
            SYNS.add(syn);
        }
    }

    public void addToIRLS (DwnInternalRelation irl) {
        if (!IRLS.contains(irl)) {
            IRLS.add(irl);
        }
    }

    public void addToERLS (DwnEquivalenceRelation erl) {
        if (!ERLS.contains(erl)) {
            ERLS.add(erl);
        }
    }

    public void addToDRLS (DwnDomainRelation dom) {
        if (!DRLS.contains(dom)) {
            DRLS.add(dom);
        }
    }

}
