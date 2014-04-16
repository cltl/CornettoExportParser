package vu.cornetto.cdb;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 7-jun-2006
 * Time: 16:07:29
 * To change this template use Options | File Templates.
 */
public class Authorship {

    /*
    <authorship>
        <author name="LEX_ALIGNER_v_2_3" id="rbn_7586975" date="2006.04.27 AD at 02_34_09 PM CEST" score="0.89" status="AUTO" >
            <comment/>
        </author>
        <author name="LEX_ALIGNER_v_2_3" id="dwn_1_0_7657364_n" date="2006.04.27 AD at 02_34_09 PM CEST" score="0.76" status="AUTO" >
            <comment/>
        </author>

    </authorship>
    */


    public String toString (String tab) {
        String str = tab+"<author ";
//        str += "type=\""+type+"\" ";
        str += "name=\""+name+"\" ";
        str += "source_id=\""+id+"\" ";
        str += "date=\""+date+"\" ";
        str += "score=\""+score+"\" ";
        str += "status=\""+status+"\"/>\n";
 //       str += tab+"\t<comment>"+comment+"</comment>\n";
//        str += tab+"</author>\n";
        return str;
    }


    public String toRelString (String tab) {
        String str = tab+"<author ";
        str += "type=\""+"dwnRel"+"\">\n";
        str += tab+"\t<name>"+name+"</name>\n";
        str += tab+"\t<source_id>"+id+"</source_id>\n";
        str += tab+"\t<date>"+date+"</date>\n";
        str += tab+"\t<score>"+score+"</score>\n";
        str += tab+"\t<status>"+status+"</status>\n";
        str += tab+"\t<comment>"+comment+"</comment>\n";
        str += tab+"</author>\n";
        return str;
    }

    public String toSynString (String tab) {
        String str = tab+"<author ";
        str += "type=\""+"dwnSyn"+"\">\n";
        str += tab+"\t<name>"+name+"</name>\n";
        str += tab+"\t<source_id>"+id+"</source_id>\n";
        str += tab+"\t<subset>"+subset+"</subset>\n";
        str += tab+"\t<date>"+date+"</date>\n";
        str += tab+"\t<comment>"+comment+"</comment>\n";
        str += tab+"</author>\n";
        return str;
    }

    String type;
    String name;
    String id;
    String date;
    double score;
    String status;
    String comment;
    String subset;
    String sequenceNr;

    public Authorship() {
        type = "vu/cornetto/dwn";
        name = "";
        id = "";
        score = 0;
        status="";
        comment = "";
        subset = "";
        sequenceNr = "";
    }

    public String getSubset() {
        return subset;
    }

    public void setSubset(String subset) {
        this.subset = subset;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
