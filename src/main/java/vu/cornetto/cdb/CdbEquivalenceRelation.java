package vu.cornetto.cdb;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 19-apr-2006
 * Time: 18:29:43
 * To change this template use Options | File Templates.
 */
public class CdbEquivalenceRelation {


/*
		<relation relation_name="EQ_NEAR_SYNONYM" version="pwn_1_5" target="ENG15-05808988-n" target20="ENG20-08945151-n">
			<author name="Paul" source_id="HEURISTICS_BI" date="19981209" score="32767.0" status="YES"/>
		</relation>
 */

    ////<ELR>ENG15-01371393-v<relationName>eq_near_synonym</relationName></ELR>
    private String version;
    private String relationName;
    private String target15;
    private String target20;
    private String target30;
    private String target16;
    String target20Previewtext;
    Authorship author;

    public CdbEquivalenceRelation() {
        relationName = "";
        target15 = "";
        target16 = "";
        target20 = "";
        version = "";
        target16 = "";
        target30 = "";
        target20Previewtext = "";
        author = new Authorship();
    }


    public Authorship getAuthor() {
        return author;
    }

    public void setAuthor(Authorship author) {
        this.author = author;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getTarget15() {
        return target15;
    }

    public void setTarget15(String target15) {
        this.target15 = target15;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTarget20() {
        return target20;
    }

    public void setTarget20(String target20) {
        this.target20 = target20;
    }

    public String getTarget16() {
        return target16;
    }

    public void setTarget16(String target16) {
        this.target16 = target16;
    }

    public String getTarget20Previewtext() {
        return target20Previewtext;
    }

    public void setTarget20Previewtext(String target20Previewtext) {
        this.target20Previewtext = target20Previewtext;
    }

    public String getTarget30() {
        return target30;
    }

    public void setTarget30(String target30) {
        this.target30 = target30;
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
        str += "\t\t<relation relation_name=\""+this.getRelationName()+"\""
                + " version=\""+this.getVersion()+"\"";
        //str += " pos=\"\""; // POS NEVER HAS A VALUE
        if (this.getTarget15().length()>0) {
            str += " target15=\""+this.getTarget15()+"\""; // FOR SOME REASON NOT NAMED target15
        }
        if (this.getTarget30().length()>0) {
            str += " target30=\""+this.getTarget30()+"\""; // FOR SOME REASON NOT NAMED target15
        }
        if (this.getTarget20().length()>0) {
            str += " target20-target20Previewtext=\""+this.target20Previewtext +"\"";
            str += " target20=\""+this.getTarget20()+"\"";
        }

        if (this.getTarget16().length()>0) {
            str += " target16=\""+this.getTarget16()+"\"";
        }
        str +=  ">\n";
        str += author.toString("\t\t\t");
 //       str += "\t\t\t</authorship>\n";
        str += "\t\t</relation>\n";
        return str;
    }

}