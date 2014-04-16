package vu.cornetto.sumo;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 19-apr-2006
 * Time: 18:35:41
 * To change this template use Options | File Templates.
 */
public class SumoRelation {
   //<ont_relation relation_name="@" arg1="" arg2="TimeInterval"
    // negative="false"
    // name="dwn10_pwn15_pwn20_mapping"
    // status="false"/>

    private String arg1;
    private String arg2;
    private String relation;
    private String negative;
    private String name;
    private String status;
    private String pwn20Synset_id;
    private String pwn16Synset_id;
    private String pwn15Synset_id;
    private double score;


    public SumoRelation() {
        this.arg1 = "";
        this.arg2 = "";
        this.name ="";
        this.status = "";
        this.negative = "";
        this.relation = "";
        this.pwn20Synset_id = "";
        this.pwn16Synset_id="";
        this.pwn15Synset_id="";
        score = 0;
    }


    public SumoRelation(String arg2, String relation, String aPwn20Synset_id) {
        this.arg1 = "";
        this.arg2 = arg2;
        this.relation = relation;
        this.pwn20Synset_id = aPwn20Synset_id;
        this.pwn15Synset_id="";
        this.pwn16Synset_id="";
        this.status = "IMPORT";
        this.negative = "";
        this.name = aPwn20Synset_id;
        score = 0;
    }


    public String toDataFile (String tab) {
        String str =tab+"<ont_relation type=\""+ relation +"\""+
                                        " term=\""+ arg2 +"\""+
                                        " target20=\""+pwn20Synset_id+"\""+
                                        " target15=\""+pwn15Synset_id+"\""+
                                        " target16=\""+pwn16Synset_id+"\""+
                                        " score=\""+score+"\""+
                                        "/>\n";
        return str;
    }

    public String toString (String tab) {

/*
        <ontology_relation relation_name="subinstance" arg1="0" arg2="Canine">

where "subinstance" indicates that the SUMO type is too general.

or for equality, I use:

<ontology_relation relation_name="instance" arg1="0" arg2="Circle">
*/

/*
        String str =tab+"<ont_relation type=\""+relation+"\""+
                                        " term=\""+arg2+"\""+
                                        " target20=\""+pwn20Synset_id+"\""+
                                        " target15=\""+pwn15Synset_id+"\""+
                                        " score=\""+score+"\""+
                                        "/>\n";
 that each concept also has a suffix, '=', ':', '+', '[', ']' or  '@', which indicates
;; the precise relationship between the SUMO concept and the WordNet synset.
;; The symbols '=', '+', and '@' mean, respectively, that the WordNet synset
;; is equivalent in meaning to the SUMO concept, is subsumed by the SUMO
;; concept or is an instance of the SUMO concept.  ':', '[', and ']' are the
;; complements of those relations.  For example, a mapping expressed as
*/
        String rel = relation;
/*
        if (rel.equals("=")) {
            rel = "instance";
        }
        else if (rel.equals("+")) {
            rel = "subinstance";
        }
        else if (rel.equals("[")) {
            rel = "superinstance";
        }
*/
        String str =tab+"<ont_relation relation_name=\""+ relation +"\""+
                                        " arg1=\""+ arg1 +"\""+
                                        " arg2=\""+ arg2 +"\""+
                                        " negative=\""+"false"+"\""+
                                        " name=\""+"dwn10_pwn15_pwn20_mapping"+"\""+
                                        " status=\""+false+"\""+
                                        "/>\n";
        return str;
    }
    //<ont_relation name="dwn10_pwn16_pwn20_mapping" status="false" relation_name="+" negative="false" arg1="" arg2="agent"/>
    public String toSourceString (String tab) {
        String str =tab+"<ont_relation"+
                                        " name=\""+getName()+"\""+
                                        " status=\""+this.getStatus()+"\""+
                                        " relation_name=\""+ relation +"\""+
                                        " negative=\""+this.getNegative()+"\""+
                                        " arg1=\""+ arg1 + "\""+
                                        " arg2=\""+ arg2 +"\""+
                                        "/>\n";
        return str;
    }
/*
    public String to16String (String tab) {
        String str =tab+"<ont_relation relation_name=\""+relation+"\""+
                                        " arg1=\""+ arg1 + "\""+
                                        " arg2=\""+ arg2 +"\""+
                                        " negative=\""+"false"+"\""+
                                        " name=\""+"dwn10_pwn16_pwn20_mapping"+"\""+
                                        " status=\""+false+"\""+
                                        "/>\n";
        return str;
    }
*/

    public String toDwnString (String tab) {
        String str =tab+"<ont_relation type=\""+ relation +"\""+
                                        " arg1=\""+ arg1 +"\"" +
                                        " arg2\""+ arg2 +"\"" +
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

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String aTerm) {
        this.arg1 = aTerm;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String aTerm) {
        this.arg2 = aTerm;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String aRelation) {
        this.relation = aRelation;
    }

    public String getPwn20Synset_id() {
        return pwn20Synset_id;
    }

    public void setPwn20Synset_id(String pwn20Synset_id) {
        this.pwn20Synset_id = pwn20Synset_id;
    }

    public String getPwn15Synset_id() {
        return pwn15Synset_id;
    }

    public void setPwn15Synset_id(String pwn15Synset_id) {
        this.pwn15Synset_id = pwn15Synset_id;
    }

    public String getPwn16Synset_id() {
        return pwn16Synset_id;
    }

    public void setPwn16Synset_id(String pwn15Synset_id) {
        this.pwn16Synset_id = pwn15Synset_id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getNegative() {
        return negative;
    }

    public void setNegative(String aNegative) {
        this.negative = aNegative;
    }
}
