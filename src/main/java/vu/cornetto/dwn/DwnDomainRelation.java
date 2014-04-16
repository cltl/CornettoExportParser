package vu.cornetto.dwn;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 19-apr-2006
 * Time: 18:37:44
 * To change this template use Options | File Templates.
 */
public class DwnDomainRelation {
    private String term;
    private String pwn16Synset_id;
    private String pwn15Synset_id;
    private String pwn20Synset_id;
    private double score;
    private String status;
    private String name;


    public DwnDomainRelation() {
        this.term = "";
        this.pwn16Synset_id = "";
        this.pwn15Synset_id = "";
        this.pwn20Synset_id = "";
        this.score = 0;
        this.status = "";
        this.name = "";
    }

/*
    public DwnDomainRelation(String term, String pwn16Synset_id, String pwn15Synset_id, double score, String status, String name) {
        this.term = term;
        this.pwn16Synset_id = pwn16Synset_id;
        this.pwn15Synset_id = pwn15Synset_id;
        this.score = score;
        this.status = status;
        this.name = name;
    }
*/

    public DwnDomainRelation(String term) {
        this.term = term;
        this.pwn16Synset_id = "";
        this.pwn15Synset_id = "";
        this.pwn20Synset_id = "";
        this.score = 0;
        this.status = "";
        this.name = "";
    }

    //	<dom_relation  term="factotum" target16="pwn_1_6_00001740-n" target15="pwn_1_5_00002403-n" score="1.0"/>
    public String toStringWithTargets (String tab) {
        String str =tab+"<dom_relation " +
                                        " term=\""+ term +"\""+
                                        " name=\""+"dwn10_pwn15_pwn16_mapping"+"\""+
                                        " status=\""+false+"\""+
                                        " target16=\""+this.getPwn16Synset_id()+"\""+
                                        " target20=\""+this.getPwn20Synset_id()+"\""+
                                        " target15=\""+this.getPwn15Synset_id()+"\""+
                                        " score=\""+this.getScore()+"\""+
                                        "/>\n";
        return str;
    }
    public String toString (String tab) {
        String str =tab+"<dom_relation " +
                                        " term=\""+ term +"\""+
                                        " name=\""+"dwn10_pwn15_pwn16_mapping"+"\""+
                                        " status=\""+false+"\""+
                                        "/>\n";
        return str;
    }

    public String to16String (String tab) {
        String str =tab+"<dom_relation " +
                                        " term=\""+ term +"\""+
                                        " name=\""+"dwn10_pwn16_mapping"+"\""+
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

    public String toDwnString (String tab) {
        String str =tab+"<dom_relation "+
                                        " type=\"wnDomain\""+
                                        " term=\""+ term +"\"" +
                                        ">\n";
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

    public String getPwn20Synset_id() {
        return pwn20Synset_id;
    }

    public void setPwn20Synset_id(String pwn20Synset_id) {
        this.pwn20Synset_id = pwn20Synset_id;
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

}
