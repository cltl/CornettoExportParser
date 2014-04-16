package vu.cornetto.dwn;


import vu.cornetto.cdb.Authorship;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 19-apr-2006
 * Time: 18:29:43
 * To change this template use Options | File Templates.
 */
public class DwnEquivalenceRelation extends Relation{


/*
		<relation relation_name="EQ_NEAR_SYNONYM" version="pwn_1_5" target="ENG15-05808988-n" target20="ENG20-08945151-n">
			<author name="Paul" source_id="HEURISTICS_BI" date="19981209" score="32767.0" status="YES"/>
		</relation>
 */

    ////<ELR>ENG15-01371393-v<TYPE>eq_near_synonym</TYPE></ELR>
    private String VERSION;
    private String TYPE;
    private String TARGET15;
    private String TARGET20;
    private String TARGET16;
    private String SOURCE;
    private String STATUS;
    private double PROB;
    String target20Previewtext;
    Authorship author;

    public DwnEquivalenceRelation() {
        TYPE = "";
        TARGET15 = "";
        TARGET16 = "";
        TARGET20 = "";
        SOURCE = "";
        STATUS = "";
        VERSION = "";
        TARGET16= "";
        PROB = 0;
        target20Previewtext = "";
        author = new Authorship();
    }

    public DwnEquivalenceRelation(String TYPE, String TARGET15) {
        this.TYPE = TYPE;
        this.TARGET15 = TARGET15;
        TARGET16 = "";
        TARGET20 = "";
        SOURCE = "";
        STATUS = "";
        PROB = 0;
        target20Previewtext = "";
        author = new Authorship();
    }

    public Authorship getAuthor() {
        return author;
    }

    public void setAuthor(Authorship author) {
        this.author = author;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getTARGET15() {
        return TARGET15;
    }

    public void setTARGET15(String TARGET15) {
        this.TARGET15 = TARGET15;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getTARGET20() {
        return TARGET20;
    }

    public void setTARGET20(String TARGET20) {
        this.TARGET20 = TARGET20;
    }

    public String getTARGET16() {
        return TARGET16;
    }

    public void setTARGET16(String TARGET16) {
        this.TARGET16 = TARGET16;
    }

    public String getTarget20Previewtext() {
        return target20Previewtext;
    }

    public void setTarget20Previewtext(String target20Previewtext) {
        this.target20Previewtext = target20Previewtext;
    }

    public String getSOURCE() {
        return SOURCE;
    }

    public void setSOURCE(String SOURCE) {
        this.SOURCE = SOURCE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public double getPROB() {
        return PROB;
    }

    public void setPROB(double PROB) {
        this.PROB = PROB;
    }

    public String toString () {
        //ENG20-00002705-a
/*
      <relation relation_name="EQ_NEAR_SYNONYM"
      target20-target20Previewtext="narrow-minded:2, narrow:3" version="pwn_1_5"
      target20="ENG20-00285913-a" pos=""
      target="ENG15-00223992-a">
        <author score="1862.0" status="" name="Paul" date="19970908" source_id=""/>
      </relation>

      <relation relation_name="EQ_NEAR_SYNONYM"
      target20-previewtext="petty:3, small-minded:1" version="pwn_1_5"
      target20="ENG20-00286672-a"
      pos=""
      target="ENG15-00224654-a">
        <author score="1862.0" status="" name="Paul" date="19970908" source_id=""/>
      </relation>

      <relation relation_name="EQ_NEAR_SYNONYM" target20-previewtext="common:1" version="pwn_1_6"
      target20="ENG20-00465271-a"
      pos=""
      target="ENG16-00460273-a">
        <author score="76.0" status="" name="Irion Technologies" date="20070622" source_id=""/>
      </relation>

		<relation relation_name="EQ_SYNONYM" target20-previewtext="calculus:1, concretion:2" version="pwn_1_5"
		target20="ENG20-08656377-n"
		target="ENG15-05640675-n">
			<author name="Laura" score="0.0" status="YES" date="19981009" source_id="MANUAL"/>
		</relation>

 */
        String str = "";
        str += "\t\t<relation relation_name=\""+this.getTYPE()+"\""
                + " version=\""+this.getVERSION()+"\"";
        //str += " pos=\"\""; // POS NEVER HAS A VALUE
        if (this.getTARGET15().length()>0) {
            str += " target15=\""+this.getTARGET15()+"\""; // FOR SOME REASON NOT NAMED target15
        }
        if (this.getTARGET20().length()>0) {
            str += " target20-target20Previewtext=\""+this.target20Previewtext +"\"";
            str += " target20=\""+this.getTARGET20()+"\"";
        }

        if (this.getTARGET16().length()>0) {
            str += " target16=\""+this.getTARGET16()+"\"";
        }
        str +=  ">\n";
        str += author.toString("\t\t\t");
 //       str += "\t\t\t</authorship>\n";
        str += "\t\t</relation>\n";
        return str;
    }

}
