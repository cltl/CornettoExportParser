package vu.cornetto.cdb;

import vu.cornetto.dwn.DwnDomainRelation;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 19-apr-2006
 * Time: 18:37:44
 * To change this template use Options | File Templates.
 */
public class CdbDomainRelation {
    //		<dom_relation  term="building_industry" name="dwn10_pwn16_mapping" status="false"/>

    private String term;
    private String pwn16Synset_id;
    private String pwn15Synset_id;
    private String pwn20Synset_id;
    private double score;
    private String status;
    private String name;


    public CdbDomainRelation() {
        this.term = "";
        this.pwn16Synset_id = "";
        this.pwn15Synset_id = "";
        this.pwn20Synset_id = "";
        this.score = 0;
        this.status = "";
        this.name = "";
    }

    public CdbDomainRelation(DwnDomainRelation dom) {
        this.term = dom.getTerm();
        this.pwn16Synset_id = dom.getPwn16Synset_id();
        this.pwn15Synset_id = dom.getPwn15Synset_id();
        this.pwn20Synset_id = dom.getPwn20Synset_id();
        this.score = dom.getScore();
        this.status = dom.getStatus();
        this.name = dom.getName();
    }


    public CdbDomainRelation(String term) {
        this.term = term;
        this.pwn16Synset_id = "";
        this.pwn15Synset_id = "";
        this.pwn20Synset_id = "";
        this.score = 0;
        this.status = "";
        this.name = "";
    }

    public String toString (String tab) {
        String str =tab+"<dom_relation " +
                                        " term=\""+ term +"\""+
                                        " name=\""+name+"\""+
                                        " status=\""+status+"\""+
                                        "/>\n";
        return str;
    }

    public String toVlisDomainString (String tab) {
        String str =tab+"<dom_relation " +
                                        " term=\""+ term +"\""+
                                        "/>\n";
        return str;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTerm() {
        return term;
    }

    public void setaDomain(String aDomain) {
        this.term = aDomain;
    }

    public String getPwn16Synset_id() {
        return pwn16Synset_id;
    }

    public void setPwn16Synset_id(String pwn16Synset_id) {
        this.pwn16Synset_id = pwn16Synset_id;
    }

    public String getPwn15Synset_id() {
        return pwn15Synset_id;
    }

    public void setPwn15Synset_id(String pwn15Synset_id) {
        this.pwn15Synset_id = pwn15Synset_id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getPwn20Synset_id() {
        return pwn20Synset_id;
    }

    public void setPwn20Synset_id(String pwn20Synset_id) {
        this.pwn20Synset_id = pwn20Synset_id;
    }
}