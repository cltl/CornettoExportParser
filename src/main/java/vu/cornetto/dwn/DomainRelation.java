package vu.cornetto.dwn;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 19-apr-2006
 * Time: 18:37:44
 * To change this template use Options | File Templates.
 */
public class DomainRelation {
    private String aDomain;
    private String pwn16Synset_id;
    private String pwn15Synset_id;
    private double score;


    public DomainRelation() {
        this.aDomain = "";
        this.pwn16Synset_id = "";
        this.pwn15Synset_id = "";
        this.score = 0;
    }

    public DomainRelation(String aDomain, String pwn16Synset_id, String pwn15Synset_id, double score) {
        this.aDomain = aDomain;
        this.pwn16Synset_id = pwn16Synset_id;
        this.pwn15Synset_id = pwn15Synset_id;
        this.score = score;
    }

    public DomainRelation(String aDomain) {
        this.aDomain = aDomain;
        this.pwn16Synset_id = "";
        this.pwn15Synset_id = "";
        this.score = 0;
    }

    public String toString (String tab) {
        String str =tab+"<dom_relation " +
                                        " term=\""+aDomain+"\""+
                                        " name=\""+"dwn10_pwn15_pwn16_mapping"+"\""+
                                        " status=\""+false+"\""+
                                        "/>\n";
        return str;
    }

    public String to16String (String tab) {
        String str =tab+"<dom_relation " +
                                        " term=\""+aDomain+"\""+
                                        " name=\""+"dwn10_pwn16_mapping"+"\""+
                                        " status=\""+false+"\""+
                                        "/>\n";
        return str;
    }

    public String toString_org (String tab) {
        String str =tab+"<dom_relation " +
                                        " term=\""+aDomain+"\""+
                                        " target16=\""+pwn16Synset_id+"\""+
                                        " target15=\""+pwn15Synset_id+"\""+
                                        " score=\""+score+"\""+
                                        "/>\n";
        return str;
    }

    public String toDwnString (String tab) {
        String str =tab+"<dom_relation "+
                                        " type=\"wnDomain\""+
                                        " term=\""+aDomain+"\"" +
                                        ">\n";
        return str;
    }

    public String getaDomain() {
        return aDomain;
    }

    public void setaDomain(String aDomain) {
        this.aDomain = aDomain;
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
