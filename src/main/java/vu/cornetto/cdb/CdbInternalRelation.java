package vu.cornetto.cdb;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 19-apr-2006
 * Time: 18:29:36
 * To change this template use Options | File Templates.
 */
public class CdbInternalRelation {

    /*
      <relation factive="false" reversed="false" negative="false" relation_name="HAS_HYPERONYM" target-previewtext="beperkt:1, begrensd:1, eindig:1" coordinative="false" disjunctive="false" target="n_a-503624">
        <author score="0.0" status="" name="Paul" date="19961206" source_id="n_a-503477"/>
      </relation>
    <relation previewgenerated="true" target-previewtext="kortjan:1" relation_name="HAS_HYPONYM" target="n_n-515897"/>

      <relation previewgenerated="true" target-previewtext="banderilla:1" relation_name="HAS_HYPONYM" target="d_n-32639"/>

      <relation previewgenerated="true" target-previewtext="speer:1, spies:2, spiets:1" relation_name="HAS_HYPONYM" target="d_n-36545"/>
      <relation previewgenerated="true" target-previewtext="lans:1" relation_name="HAS_HYPONYM" target="d_n-40386"/>
      <relation previewgenerated="true" target-previewtext="degen:1, zijdgeweer:1" relation_name="HAS_HYPONYM" target="d_n-10791"/>
      <relation previewgenerated="true" target-previewtext="dolk:1" relation_name="HAS_HYPONYM" target="d_n-27737"/>

	<relation factive="" reversed="false" target-previewtext="materie:1, stof:2, substantie:1" relation_name="HAS_HYPERONYM" negative="false" coordinative="false" disjunctive="false" target="d_n-19937">
			<author name="Laura" score="0.0" status="" date="19981009" source_id="d_n-31041"/>
	</relation>
	<relation factive="" reversed="false" target-previewtext="renpaard:1, koerspaard:1, racepaard:1" relation_name="HAS_HYPERONYM" negative="false" coordinative="false" disjunctive="false" target="d_n-24831">
		<author name="Paul" score="0.0" status="YES" date="19961206" source_id="d_n-31048"/>
	</relation>			    

	<relation factive="" reversed="false" target-previewtext="knaagdier:1" relation_name="HAS_HYPERONYM" negative="false" coordinative="false" disjunctive="false" target="d_n-28920">
		<author name="Piek" score="0.0" status="" date="19961217" source_id="d_n-31050"/>
	</relation>

			<relation factive="" reversed="false" target-previewtext="dierenziekte:1" relation_name="HAS_HYPERONYM" negative="false"
			coordinative="false" disjunctive="false" target="d_n-33314">
				<author name="Paul" score="0.0" status="YES" date="19961206" source_id="d_n-31053"/>
			</relation>
			<relation factive="" reversed="false"
			target-previewtext="paard:1, peerd:1, ros:1, viervoeter:2"
			relation_name="INVOLVED_PATIENT"
			negative="false"
			coordinative="false"
			disjunctive="false"
			target="d_n-41162">
				<author name="Piek" score="0.0" status="YES" date="19970422" source_id="d_n-31053"/>
			</relation>
*/

    private String factive;
    private String reversed;
    private String relation_name;
    private String negative;
    private String coordinative;
    private String disjunctive;
    private String target;
    private String previewtext;
    private Authorship author;

    public CdbInternalRelation() {
        factive = "";
        reversed= "";
        relation_name= "";
        negative= "";
        coordinative= "";
        disjunctive= "";
        target= "";
        author = new Authorship();
        previewtext = "";
    }

    public CdbInternalRelation(CdbInternalRelation oRel) {
        factive = oRel.getFactive();
        reversed= oRel.getReversed();
        relation_name= oRel.getRelation_name();
        negative= oRel.getNegative();
        coordinative= oRel.getCoordinative();
        disjunctive= oRel.getDisjunctive();
        target= oRel.getTarget();
        author = oRel.getAuthor();
        previewtext = "";
    }


    public Authorship getAuthor() {
        return author;
    }

    public void setAuthor(Authorship author) {
        this.author = author;
    }

    public String getPreviewtext() {
        return previewtext;
    }

    public void setPreviewtext(String previewtext) {
        this.previewtext = previewtext;
    }

    public String getFactive() {
        return factive;
    }

    public void setFactive(String factive) {
        this.factive = factive;
    }

    public String getReversed() {
        return reversed;
    }

    public void setReversed(String reversed) {
        this.reversed = reversed;
    }

    public String getRelation_name() {
        return relation_name;
    }

    public void setRelation_name(String relation_name) {
        this.relation_name = relation_name;
    }

    public String getNegative() {
        return negative;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    public String getCoordinative() {
        return coordinative;
    }

    public void setCoordinative(String coordinative) {
        this.coordinative = coordinative;
    }

    public String getDisjunctive() {
        return disjunctive;
    }

    public void setDisjunctive(String disjunctive) {
        this.disjunctive = disjunctive;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String toString() {
        /*
     <relation factive="false" reversed="false" negative="false"
     relation_name="HAS_HYPERONYM" target-previewtext="beperkt:1, begrensd:1, eindig:1"
     coordinative="false" disjunctive="false"
     target="n_a-503624">
        <author score="0.0" status="" name="Paul" date="19961206" source_id="n_a-503477"/>
      </relation>
         */
           String str = "\t\t\t<relation relation_name=\""+this.getRelation_name()
                    +"\" target-previewtext=\""+this.getPreviewtext()
                    +"\" target=\""+this.getTarget()+ "\"";
            str +=" coordinative=\""+this.getCoordinative()+ "\""
                +" disjunctive=\""+this.getDisjunctive()+ "\""
                +" reversed=\""+this.getReversed()+ "\""
                +" factive=\""+this.getFactive()+ "\""
                +" negative=\""+this.getNegative()+ "\""
                +">\n";
                str += this.getAuthor().toString("\t\t\t\t");
            str += "\t\t\t</relation>\n";
            return str;

    }
    public String toBareString() {
        /*
     <relation factive="false" reversed="false" negative="false"
     relation_name="HAS_HYPERONYM" target-previewtext="beperkt:1, begrensd:1, eindig:1"
     coordinative="false" disjunctive="false"
     target="n_a-503624">
        <author score="0.0" status="" name="Paul" date="19961206" source_id="n_a-503477"/>
      </relation>
         */
           String str = "\t\t\t<relation relation_name=\""+this.getRelation_name()
                    +"\" target=\""+this.getTarget()+ "\"";
            str +=" coordinative=\""+this.getCoordinative()+ "\""
                +" disjunctive=\""+this.getDisjunctive()+ "\""
                +" reversed=\""+this.getReversed()+ "\""
                +" factive=\""+this.getFactive()+ "\""
                +" negative=\""+this.getNegative()+ "\""
                +">\n";
                str += this.getAuthor().toString("\t\t\t\t");
            str += "\t\t\t</relation>\n";
            return str;

    }
    public String toSimpleString() {
        /*
     <relation
     relation_name="HAS_HYPERONYM"
     target="n_a-503624"/>
         */
           String str = "\t\t\t<relation relation_name=\""+this.getRelation_name()
                    +"\" target=\""+this.getTarget()+ "\"/>\n";
            return str;

    }
}