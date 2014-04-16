package vu.cornetto.cdb;


import vu.cornetto.sumo.SumoRelation;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 11-jul-2007
 * Time: 18:00:59
 * To change this template use File | Settings | File Templates.
 */
public class CdbSynset {

/*    
    <cdb_synset c_sy_id="d_n-9728" d_synset_id="d_n-9728">
	<synonyms>
	//c_lu_id-previewtext="balie:2"
		<synonym c_lu_id="d_n-45981" c_cid_id="" status=""/>
		<synonym c_lu_id="d_n-521093" c_cid_id="109535" status=""/>
	</synonyms>
	<wn_internal_relations>
			<relation relation_name="HAS_HYPERONYM" target="d_n-42534" coordinative="false" disjunctive="false" reversed="false" factive="" negative="false">
				<author name="Paul" source_id="d_n-9728" date="19961206" score="0.0" status=""/>
			</relation>
	</wn_internal_relations>
	<wn_equivalence_relations>
		<relation relation_name="EQ_NEAR_SYNONYM" version="pwn_1_5" target="ENG15-09075372-n" target20="ENG20-14270540-n">
			<author name="Paul" source_id="HEURISTICS_BI" date="19970908" score="5000.0" status=""/>
		</relation>
	</wn_equivalence_relations>
	<sumo_relations>
		<ont_relation relation_name="@" arg1="" arg2="TimeInterval" negative="false" name="dwn10_pwn15_pwn20_mapping" status="false"/>
	</sumo_relations>
	<wn_domains>
		<dom_relation  term="geology" name="dwn10_pwn15_pwn16_mapping" status="false"/>
	</wn_domains>
</cdb_synset>
*/
    String c_sy_id;
    String d_synset_id;
    String posSpecific;
    String comment;
    ArrayList <Syn> synonyms;
    ArrayList <CdbInternalRelation> iRelations;
    ArrayList <CdbEquivalenceRelation> eRelations;
    ArrayList <vu.cornetto.sumo.SumoRelation> sRelations;
    ArrayList <CdbDomainRelation> dRelations;
    ArrayList <CdbDomainRelation> vdRelations;
    String definition;
    String differentiae;
    String baseConcept;

    public CdbSynset() {
             c_sy_id = "";
             d_synset_id = "";
             comment = "";
             posSpecific = "";
             definition ="";
             differentiae ="";
             baseConcept ="";
             synonyms = new ArrayList<Syn>();
             iRelations= new ArrayList<CdbInternalRelation>();
             eRelations= new ArrayList<CdbEquivalenceRelation>();
             sRelations= new ArrayList<SumoRelation>();
             dRelations= new ArrayList<CdbDomainRelation>();
             vdRelations= new ArrayList<CdbDomainRelation>();
    }

    public String getPos () {
        if (this.posSpecific.startsWith("NOUN")) {
            return "NOUN";
        }
        else if (this.posSpecific.startsWith("VERB")) {
            return "VERB";
        }
        else if (this.posSpecific.startsWith("ADJECTIVE")) {
            return "ADJECTIVE";
        }
        else if (this.posSpecific.startsWith("ADJ")) {
            return "ADJECTIVE";
        }
        else if (this.getPosSpecific().isEmpty()) {
            for (int i = 0; i < synonyms.size(); i++) {
                Syn syn = synonyms.get(i);
                if (syn.getC_lu_id().startsWith("r_n")) {
                    return "NOUN";
                }
                else if (syn.getC_lu_id().startsWith("d_n")) {
                    return "NOUN";
                }
                else if (syn.getC_lu_id().startsWith("n_n")) {
                    return "NOUN";
                }
                if (syn.getC_lu_id().startsWith("r_v")) {
                    return "VERB";
                }
                else if (syn.getC_lu_id().startsWith("d_v")) {
                    return "VERB";
                }
                if (syn.getC_lu_id().startsWith("r_a")) {
                    return "ADJECTIVE";
                }
                else if (syn.getC_lu_id().startsWith("d_a")) {
                    return "ADJECTIVE";
                }
            }
        }
        return "OTHER";
    }
    
    public String getC_sy_id() {
        return c_sy_id;
    }

    public void setC_sy_id(String c_sy_id) {
        this.c_sy_id = c_sy_id;
    }

    public String getD_synset_id() {
        return d_synset_id;
    }

    public void setD_synset_id(String d_synset_id) {
        this.d_synset_id = d_synset_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void addComment(String comment) {
        if (this.comment.indexOf(comment)==-1) {
            this.comment += ";"+comment;
        }
    }

    public String getPosSpecific() {
        return posSpecific;
    }

    public void setPosSpecific(String posSpecific) {
        this.posSpecific = posSpecific;
    }

    public void addPosSpecific(String posSpecific) {
        if (this.posSpecific.indexOf(posSpecific)==-1) {
            this.posSpecific += ";"+posSpecific;
        }
    }

    public ArrayList getSynonyms() {
        return synonyms;
    }

    public ArrayList getSynonymsAsSubSense(int i) {
        ArrayList<Syn> subSyn = new ArrayList<Syn>();
        for (int j = 0; j < synonyms.size(); j++) {
            Syn syn = synonyms.get(j);
            Syn newSyn = new Syn();
            newSyn.setC_lu_id(syn.getC_lu_id());
            newSyn.setPreviewtext(syn.getPreviewtext());
            newSyn.setSubsense(new Integer (i).toString());
            //System.out.println("newSyn.getPreviewtext() = " + newSyn.getPreviewtext());
            subSyn.add(newSyn);
        }
        return subSyn;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDifferentiae() {
        return differentiae;
    }

    public void setDifferentiae(String differentiae) {
        this.differentiae = differentiae;
    }

    public String getBaseConcept() {
        return baseConcept;
    }

    public void setBaseConcept(String baseConcept) {
        this.baseConcept = baseConcept;
    }

    public void setSynonyms(ArrayList<Syn> synonyms) {
        this.synonyms = synonyms;
    }

    public void setSynonyms(String status, ArrayList<Syn> synonyms) {
        for (int i = 0; i < synonyms.size(); i++) {
            Syn syn = synonyms.get(i);
            syn.setStatus(status);
        }
        this.synonyms = synonyms;
    }

    public void addSynonyms(String status, ArrayList<Syn> synonyms) {
        for (int i = 0; i < synonyms.size(); i++) {
            Syn syn =  synonyms.get(i);
            syn.setStatus(status);
            this.addSynonym(syn);

        }
    }

    public void addSynonyms(ArrayList<Syn> synonyms) {
        for (int i = 0; i < synonyms.size(); i++) {
            Syn syn =  synonyms.get(i);
            this.addSynonym(syn);

        }
    }

    public void addSynonym(Syn syn) {
        for (int i = 0; i < synonyms.size(); i++) {
            Syn syn1 = synonyms.get(i);
            if (syn1.getC_lu_id().equals(syn.getC_lu_id())) {
                return;
            }
        }
        this.synonyms.add(syn);
    }

    public ArrayList getIRelations() {
        return iRelations;
    }

    public void setVdRelations(ArrayList vdRelations) {
        this.vdRelations = vdRelations;
    }

    public void addVdRelations(CdbDomainRelation domRelationCdb) {
        this.vdRelations.add(domRelationCdb);
    }

    public ArrayList<CdbDomainRelation> getVdRelations() {
        return vdRelations;
    }

    public void setIRelations(ArrayList iRelations) {
        this.iRelations = iRelations;
    }

    public void addIRelation(CdbInternalRelation rel) {
        this.iRelations.add(rel);
    }
    public ArrayList getERelations() {
        return eRelations;
    }

    public void setERelations(ArrayList<CdbEquivalenceRelation> eRelations) {
        this.eRelations = eRelations;
    }

    public void addERelation(CdbEquivalenceRelation equi) {
        this.eRelations.add(equi);
    }

    public ArrayList getSRelations() {
        return sRelations;
    }

    public void setSRelations(ArrayList sRelations) {
        this.sRelations = sRelations;
    }

    public void addSRelation(vu.cornetto.sumo.SumoRelation sumo) {
        this.sRelations.add(sumo);
    }

    public ArrayList<CdbDomainRelation> getDRelations() {
        return dRelations;
    }

    public void setDRelations(ArrayList dRelations) {
        this.dRelations = dRelations;
    }

    public void addDRelation(CdbDomainRelation dom) {
        this.dRelations.add(dom);
    }

    public String toTargetPreviewStringSkipNotFound () {
        String str = "";
        if (synonyms.size()>0) {
            for (int i=0;i<synonyms.size();i++) {
                 Syn rel = (Syn) synonyms.get(i);
                 if (!rel.getPreviewtext().equals("not_found:0")) {
                    str += rel.getPreviewtext();
                     if (i<synonyms.size()-1) {
                         str += ", ";
                     }
                 }
            }
        }
        return str;
    }

    public String toSynsetStringSkipNotFound () {
        String str = "";
        if (synonyms.size()>0) {
            str += "\t<synonyms>\n";
            for (int i=0;i<synonyms.size();i++) {
                 Syn rel = (Syn) synonyms.get(i);
                 if (!rel.getPreviewtext().equals("not_found:0")) {
                    str += "\t\t"+rel.toString();
                 }
            }
            str += "\t</synonyms>\n";
        }
        else {
            str += "\t<synonyms/>\n";
        }
        return str;
    }

    public String toSynsetString () {
        String str = "";
        if (synonyms.size()>0) {
            str += "\t<synonyms>\n";
            for (int i=0;i<synonyms.size();i++) {
                 Syn rel = (Syn) synonyms.get(i);
                 str += "\t\t"+rel.toString();
            }
            str += "\t</synonyms>\n";
        }
        else {
            str += "\t<synonyms/>\n";
        }
        return str;
    }

    public String toSynsetStringWithoutCid () {
        String str = "";
        if (synonyms.size()>0) {
            str += "\t<synonyms>\n";
            for (int i=0;i<synonyms.size();i++) {
                 Syn rel = (Syn) synonyms.get(i);
                 str += "\t\t"+rel.toStringWithoutCid();
            }
            str += "\t</synonyms>\n";
        }
        else {
            str += "\t<synonyms/>\n";
        }
        return str;
    }

    public String toIdentifierSynsetString () {
        String str = "";
        if (synonyms.size()>0) {
            str += "\t<synonyms>\n";
            for (int i=0;i<synonyms.size();i++) {
                 Syn rel = (Syn) synonyms.get(i);
                 str += "\t\t"+rel.toBareString();
            }
            str += "\t</synonyms>\n";
        }
        else {
            str += "\t<synonyms/>\n";
        }
        return str;
    }

    public String toFlatSynonymString () {
        String str = "";
        if (synonyms.size()>0) {
            for (int i=0;i<synonyms.size();i++) {
                 Syn rel = (Syn) synonyms.get(i);
                 if (i>0) {
                     str +=";";
                 }
                 str += rel.getPreviewtext();
            }
        }
        return str;
    }

    public String toFlatSynonymLUString () {
        String str = "";
        if (synonyms.size()>0) {
            for (int i=0;i<synonyms.size();i++) {
                 Syn rel = (Syn) synonyms.get(i);
                 if (i>0) {
                     str +=";";
                 }
                 str += rel.getPreviewtext()+":"+rel.getC_lu_id();
            }
        }
        return str;
    }

    public String toIntRelationsString () {
        String str = "";
        /// WN_INTERNAL_RELATIONS
        if (iRelations.size()>0) {
            str += "\t<wn_internal_relations>\n";
            for (int r=0;r<this.iRelations.size();r++) {
                CdbInternalRelation rel = (CdbInternalRelation) this.iRelations.get(r);
                if (!rel.getRelation_name().equalsIgnoreCase("HAS_HYPONYM")) {
                     str += rel.toString();
                }
                //str += rel.toString();
            }
            str += "\t</wn_internal_relations>\n";
        }
        else {
            str += "\t<wn_internal_relations/>\n";
        }
        return str;
    }

    public String toIntRelationsStringWithoutPreviewText () {
        String str = "";
        /// WN_INTERNAL_RELATIONS
        if (iRelations.size()>0) {
            str += "\t<wn_internal_relations>\n";
            for (int r=0;r<this.iRelations.size();r++) {
                CdbInternalRelation rel = (CdbInternalRelation) this.iRelations.get(r);
                if (!rel.getRelation_name().equalsIgnoreCase("HAS_HYPONYM")) {
                     str += rel.toBareString();
                }
                //str += rel.toString();
            }
            str += "\t</wn_internal_relations>\n";
        }
        else {
            str += "\t<wn_internal_relations/>\n";
        }
        return str;
    }
    
    public String toIntRelationsStringWithoutAuthor () {
        String str = "";
        /// WN_INTERNAL_RELATIONS
        if (iRelations.size()>0) {
            str += "\t<wn_internal_relations>\n";
            for (int r=0;r<this.iRelations.size();r++) {
                CdbInternalRelation rel = (CdbInternalRelation) this.iRelations.get(r);
                if (!rel.getRelation_name().equalsIgnoreCase("HAS_HYPONYM")) {
                     str += rel.toSimpleString();
                }
                //str += rel.toString();
            }
            str += "\t</wn_internal_relations>\n";
        }
        else {
            str += "\t<wn_internal_relations/>\n";
        }
        return str;
    }

    public String toEquiRelationsString () {
        String str = "";
        //// WN_EQUIVALENCE_RELATIONS
        if (eRelations.size()>0) {
/*
		<relation relation_name="EQ_NEAR_SYNONYM" version="pwn_1_5" target="ENG15-05808988-n" target20="ENG20-08945151-n">
			<author name="Paul" source_id="HEURISTICS_BI" date="19981209" score="32767.0" status="YES"/>
		</relation>
 */
            str += "\t<wn_equivalence_relations>\n";
            for (int i=0;i<eRelations.size();i++) {
                CdbEquivalenceRelation rel = (CdbEquivalenceRelation) this.eRelations.get(i);
                str += rel.toString();
            }
            str += "\t</wn_equivalence_relations>\n";
        }
        else {
            str += "\t<wn_equivalence_relations/>\n";
        }
        return str;
    }

    public String toDomainRelationsString () {
        String str = "";
        if (dRelations.size()>0) {
            ArrayList domList = new ArrayList();
            str += "\t<wn_domains>\n";
            for (int i=0;i<dRelations.size();i++) {
                CdbDomainRelation dom = (CdbDomainRelation) dRelations.get(i);
                //str += dom.toString("\t\t");
                if (!domList.contains(dom.getTerm())) {
                    domList.add(dom.getTerm());
                    str += dom.toString("\t\t");
                }
            }
            str += "\t</wn_domains>\n";
        }
        else {
            str += "\t<wn_domains/>\n";
        }
        return str;
    }

    public boolean hasDomainRelation (String aTerm) {
        if (dRelations.size()>0) {
            for (int i=0;i<dRelations.size();i++) {
                CdbDomainRelation dom = (CdbDomainRelation) dRelations.get(i);
                if (dom.getTerm().equals(aTerm)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String toVlisDomainRelationsString () {
        String str = "";
        if (vdRelations.size()>0) {
            ArrayList domList = new ArrayList();
            str += "\t<vlis_domains>\n";
            for (int i=0;i<vdRelations.size();i++) {
                CdbDomainRelation dom = (CdbDomainRelation) vdRelations.get(i);
                //str += dom.toVlisDomainString("\t\t");
                if (!domList.contains(dom.getTerm())) {
                    domList.add(dom.getTerm());
                    str += dom.toVlisDomainString("\t\t");
                }
            }
            str += "\t</vlis_domains>\n";
        }
        else {
            str += "\t<vlis_domains/>\n";
        }
        return str;
    }

    public String toSumoRelationsString () {
        String str = "";
        if (sRelations.size()>0) {
            str += "\t<sumo_relations>\n";
            ArrayList sumList = new ArrayList();
            for (int r=0;r<this.sRelations.size();r++) {
               vu.cornetto.sumo.SumoRelation rel = (vu.cornetto.sumo.SumoRelation) this.sRelations.get(r);
               str += rel.toSourceString("\t\t");
/*
                if (!sumList.contains(rel.getArg2())) {
                    sumList.add(rel.getArg2());
                   str += rel.toSourceString("\t\t");
                   //System.out.println("rel = " + rel.toString());
                }
                else {
                    System.out.println("rel.getArg2() = " + rel.getArg2());
                }
*/
            }
            str += "\t</sumo_relations>\n";
        }
        else {
            str += "\t<sumo_relations/>\n";
        }
        return str;
    }



    public String toString () {
        String str = "<cdb_synset";
        str += " c_sy_id=\""+this.c_sy_id+"\"";
        str += " d_synset_id=\""+this.d_synset_id+"\"";
        str += " posSpecific=\""+this.posSpecific+"\"";
        str += " comment=\""+this.comment+"\"";
        str += ">\n";
        str += toSynsetString();
        if (this.baseConcept.length()>0) {
            str += "\t<base_concept>"+baseConcept+"</base_concept>\n";
        }
        else {
           // str += "\t<base_concept/>\n";
        }
        if (definition.length()>0) {
            str += "\t<definition>"+ vu.cornetto.util.Other.addCDATAstring(definition);
            str += "</definition>\n";
        }
        else {
         //   str +="\t<definition/>\n";
        }

        if (this.differentiae.length()>0) {
            str += "\t<differentiae>"+ vu.cornetto.util.Other.addCDATAstring(differentiae);
            str += "</differentiae>\n";
        }
        else {
          //  str +="\t<differentiae/>\n";
        }
        str += toIntRelationsString();
        str += toEquiRelationsString();
        str += toDomainRelationsString();
        str += toVlisDomainRelationsString();
        str += toSumoRelationsString();
        str += "</cdb_synset>\n";
        return str;
    }

    public String toStringPwn () {
        String str = "<cdb_synset";
        str += " c_sy_id=\""+this.c_sy_id+"\"";
        str += " posSpecific=\""+this.posSpecific+"\"";
        str += " comment=\""+this.comment+"\"";
        str += ">\n";
        str += toSynsetStringWithoutCid();
        if (this.baseConcept.length()>0) {
            str += "\t<base_concept>"+baseConcept+"</base_concept>\n";
        }
        else {
           // str += "\t<base_concept/>\n";
        }
        if (definition.length()>0) {
            str += "\t<definition>"+ vu.cornetto.util.Other.addCDATAstring(definition);
            str += "</definition>\n";
        }
        else {
         //   str +="\t<definition/>\n";
        }

        str += toIntRelationsStringWithoutAuthor();
        //str += toDomainRelationsString();
        //str += toSumoRelationsString();
        str += "</cdb_synset>\n";
        return str;
    }

    public String toStringSkipNotFound () {
        String str = "<cdb_synset";
        str += " c_sy_id=\""+this.c_sy_id+"\"";
        str += " d_synset_id=\""+this.d_synset_id+"\"";
        str += " posSpecific=\""+this.posSpecific+"\"";
        str += " comment=\""+this.comment+"\"";
        str += ">\n";
        str += this.toSynsetStringSkipNotFound();
        if (this.baseConcept.length()>0) {
            str += "\t<base_concept>"+baseConcept+"</base_concept>\n";
        }
        else {
           // str += "\t<base_concept/>\n";
        }
        if (definition.length()>0) {
            str += "\t<definition>"+ vu.cornetto.util.Other.addCDATAstring(definition);
            str += "</definition>\n";
        }
        else {
            str +="\t<definition/>\n";
        }

        str += toIntRelationsString();
        str += toEquiRelationsString();
        str += toDomainRelationsString();
        str += toSumoRelationsString();
        str += "</cdb_synset>\n";
        return str;
    }

    public String toStringNoRedundant () {
        String str = "<cdb_synset";
        str += " c_sy_id=\""+this.c_sy_id+"\"";
        str += " d_synset_id=\""+this.d_synset_id+"\"";
        str += " posSpecific=\""+this.posSpecific+"\"";
        str += " comment=\""+this.comment+"\"";
        str += ">\n";
        str += this.toIdentifierSynsetString();
        if (this.baseConcept.length()>0) {
            str += "\t<base_concept>"+baseConcept+"</base_concept>\n";
        }
        else {
           // str += "\t<base_concept/>\n";
        }

        str += toIntRelationsStringWithoutPreviewText();
        str += toEquiRelationsString();
        str += toDomainRelationsString();
        str += toSumoRelationsString();
        str += "</cdb_synset>\n";
        return str;
    }

    public boolean hasEquivalence (String pwnId) {
        for (int i=0;i<eRelations.size();i++) {
            CdbEquivalenceRelation rel = (CdbEquivalenceRelation) this.eRelations.get(i);
            if (rel.getTarget20().equals(pwnId)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyEquivalence () {
        if (eRelations.size()>0) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean hasData () {
        if (this.iRelations.size()>0) {
            return true;
        }
        if (this.eRelations.size()>0) {
            return true;
        }
        if (this.dRelations.size()>0) {
            return true;
        }
        if (this.sRelations.size()>0) {
            return true;
        }
        return false;
    }
    /*
    /*
    <cdb_synset c_sy_id="d_n-9728" d_synset_id="d_n-9728">
	<synonyms>
		<synonym c_lu_id="d_n-45981" c_cid_id="" status=""/>
		<synonym c_lu_id="d_n-521093" c_cid_id="109535" status=""/>
	</synonyms>
	<wn_internal_relations>
			<relation relation_name="HAS_HYPERONYM" target="d_n-42534" coordinative="false" disjunctive="false" reversed="false" factive="" negative="false">
				<author name="Paul" source_id="d_n-9728" date="19961206" score="0.0" status=""/>
			</relation>
	</wn_internal_relations>
	<wn_equivalence_relations>
		<relation relation_name="EQ_NEAR_SYNONYM" version="pwn_1_5" target="ENG15-09075372-n" target20="ENG20-14270540-n">
			<author name="Paul" source_id="HEURISTICS_BI" date="19970908" score="5000.0" status=""/>
		</relation>
	</wn_equivalence_relations>
	<sumo_relations>
		<ont_relation relation_name="@" arg1="" arg2="TimeInterval" negative="false" name="dwn10_pwn15_pwn20_mapping" status="false"/>
	</sumo_relations>
	<wn_domains>
		<dom_relation  term="geology" name="dwn10_pwn15_pwn16_mapping" status="false"/>
	</wn_domains>
</cdb_synset>
*/

    public boolean manualInternal() {
        if (iRelations.size()>0) {
            for (int r=0;r<this.iRelations.size();r++) {
                CdbInternalRelation rel = (CdbInternalRelation) this.iRelations.get(r);
                if (!rel.getAuthor().getName().equals("Paul")) {
                    System.out.println("MANUAL INTERNAL:"+rel.getAuthor().getName());

                   return true;
                }
                if (rel.getAuthor().getStatus().length()>0) {
                    System.out.println("MANUAL INTERNAL:"+rel.getAuthor().getStatus());
                   return true;
                }
            }
        }
        return false;
    }

    public boolean manualEquivalence() {
       //// WN_EQUIVALENCE_RELATIONS
        if (eRelations.size()>0) {
/*
		<relation relation_name="EQ_NEAR_SYNONYM" version="pwn_1_5" target="ENG15-05808988-n" target20="ENG20-08945151-n">
			<author name="Paul" source_id="HEURISTICS_BI" date="19981209" score="32767.0" status="YES"/>
		</relation>
      <relation relation_name="EQ_NEAR_SYNONYM" target20-previewtext="narrow-minded:2, narrow:3" version="pwn_1_5" target20="ENG20-00285913-a" pos="" target="ENG15-00223992-a">
        <author score="1862.0" status="" name="Paul" date="19970908" source_id=""/>
      </relation>
      <relation relation_name="EQ_NEAR_SYNONYM" target20-previewtext="nefarious:1, villainous:1" version="pwn_1_6" target20="ENG20-02426953-a" pos="" target="ENG16-02394260-a">
        <author score="42.0" status="" name="Irion Technologies" date="20070622" source_id=""/>
      </relation>

 */
            for (int i=0;i<eRelations.size();i++) {
                CdbEquivalenceRelation rel = (CdbEquivalenceRelation) this.eRelations.get(i);
                if ((!rel.getAuthor().getName().equals("Irion Technologies")) &&
                    (!rel.getAuthor().getName().equals("Paul"))) {
                     //System.out.println("MANUAL EQUIVALENCE:"+rel.getAuthor().getName());
                     return true;
                }
                else if (rel.getAuthor().getStatus().length()>0) {
                    //System.out.println("MANUAL EQUIVALENCE:"+rel.getAuthor().getStatus());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean manualDomain() {
        if (dRelations.size()>0) {
            for (int i=0;i<dRelations.size();i++) {
                CdbDomainRelation dom = (CdbDomainRelation) dRelations.get(i);
                if ((!dom.getName().equals("dwn10_pwn15_pwn16_mapping")) &&
                    (!dom.getName().equals("dwn10_pwn15_pwn20_mapping")) &&
                    (!dom.getName().equals("dwn10_pwn16_pwn20_mapping")) &&
                    (!dom.getName().equals("dwn10_pwn16_mapping"))) {
                    //System.out.println("MANUAL DOMAIN:"+dom.getName());
                    return true;
                }
                else if (dom.getStatus().equals("true")) {
                    //System.out.println("MANUAL DOMAIN:"+dom.getStatus());
                    return true;
                }
            }
        }
       // System.out.println("AUTOMATIC DOMAIN");
        return false;
    }

    public boolean manualOntology() {
        if (sRelations.size()>0) {
            ArrayList sumList = new ArrayList();
            for (int r=0;r<this.sRelations.size();r++) {
               vu.cornetto.sumo.SumoRelation rel = (vu.cornetto.sumo.SumoRelation) this.sRelations.get(r);
               if ((!rel.getName().equals("dwn10_pwn15_pwn16_mapping")) &&
                    (!rel.getName().equals("dwn10_pwn15_pwn20_mapping")) &&
                    (!rel.getName().equals("dwn10_pwn16_pwn20_mapping")) &&
                    (!rel.getName().equals("dwn10_pwn16_mapping"))) {
                    //System.out.println("MANUAL ONT:"+rel.getName());
                    return true;
               }
               else if (rel.getStatus().equals("true")) {
                   //System.out.println("MANUAL ONT:"+rel.getStatus());
                   return true;
               }
            }
        }
        //System.out.println("AUTOMATIC ONTOLOGY");
        return false;
    }
    

    public boolean fixEquivalence() {
        boolean fix = false;
       //// WN_EQUIVALENCE_RELATIONS
        if (eRelations.size()>3) {
/*
		<relation relation_name="EQ_NEAR_SYNONYM" version="pwn_1_5" target="ENG15-05808988-n" target20="ENG20-08945151-n">
			<author name="Paul" source_id="HEURISTICS_BI" date="19981209" score="32767.0" status="YES"/>
		</relation>
      <relation relation_name="EQ_NEAR_SYNONYM" target20-previewtext="narrow-minded:2, narrow:3" version="pwn_1_5" target20="ENG20-00285913-a" pos="" target="ENG15-00223992-a">
        <author score="1862.0" status="" name="Paul" date="19970908" source_id=""/>
      </relation>
      <relation relation_name="EQ_NEAR_SYNONYM" target20-previewtext="nefarious:1, villainous:1" version="pwn_1_6" target20="ENG20-02426953-a" pos="" target="ENG16-02394260-a">
        <author score="42.0" status="" name="Irion Technologies" date="20070622" source_id=""/>
      </relation>

 */
            double topScore = 0.0;
            for (int i=0;i<eRelations.size();i++) {
                CdbEquivalenceRelation rel = (CdbEquivalenceRelation) this.eRelations.get(i);
                //System.out.println("score ="+rel.getAuthor().getScore());
                if (rel.getAuthor().getScore()>topScore) {
                    topScore = rel.getAuthor().getScore();
                }
            }
            System.out.println("topScore = " + topScore);
            ArrayList newRelations = new ArrayList();
            for (int i=0;i<eRelations.size();i++) {
                CdbEquivalenceRelation rel = (CdbEquivalenceRelation) this.eRelations.get(i);
                if (((rel.getAuthor().getScore()/topScore)*100)>=80) {
                    newRelations.add(rel);
                }
                else {
                    fix = true;
                    System.out.println("rejecting = " +rel.getAuthor().getScore() + ": "+ rel.getTarget20Previewtext());
                }
            }
            eRelations = newRelations;
        }
        else {
            //System.out.println("LESS THAN 4 EQUIVALENCE RELATION!");
        }
        return fix;
    }

}
