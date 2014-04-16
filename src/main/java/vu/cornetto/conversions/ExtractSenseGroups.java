package vu.cornetto.conversions;

import vu.cornetto.cdb.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: kyoto
 * Date: 6/29/12
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtractSenseGroups {

    /// polysemyRelations are semantic relations between different senses of the same word

    static public String getRelatedPolysemyRelations (String word, ArrayList<CdbLuForm> luIds, CdbSynsetParser synsetParser) {
        String str = "";
        boolean related = false;
        str = "<lemma spelling=\""+word+"\">\n";
        for (int i = 0; i < luIds.size(); i++) {
            CdbLuForm luForm = luIds.get(i);
            String luId = luForm.getLuId();
            String seq_nr = luForm.getSeqNr();
            if (synsetParser.luIdSynsetMap.containsKey(luId)) {
                CdbSynset synset = synsetParser.luIdSynsetMap.get(luId);
                ArrayList<CdbInternalRelation> rels = synset.getIRelations();
                for (int j = i+1; j < luIds.size(); j++) {
                    CdbLuForm oLuForm = luIds.get(j);
                    String oLuId = oLuForm.getLuId();
                    String oSeq_nr = oLuForm.getSeqNr();
                    if (synsetParser.luIdSynsetMap.containsKey(oLuId)) {
                        CdbSynset oSynset = synsetParser.luIdSynsetMap.get(oLuId);
                        for (int k = 0; k < rels.size(); k++) {
                            CdbInternalRelation cdbInternalRelation = rels.get(k);
                            if (cdbInternalRelation.getTarget().equals(oSynset.getC_sy_id())) {
                                related = true;
                                /// we reverse the has_hyponym relations, we als need to reverse the order of the senses
                                if (cdbInternalRelation.getRelation_name().equalsIgnoreCase("HAS_HYPONYM")) {
                                    str += "\t<relation type=\""+"HAS_HYPERONYM"+"\">\n";
                                    str += "\t\t<lu sense=\""+oSeq_nr+"\""+" id=\""+oLuId+"\""+" direction=\""+"source\"/>\n";
                                    str += "\t\t<lu sense=\""+seq_nr+"\""+" id=\""+luId+"\""+" direction=\""+"target\"/>\n";
                                }
                                else {
                                    str += "\t<relation type=\""+cdbInternalRelation.getRelation_name()+"\">\n";
                                    str += "\t\t<lu sense=\""+seq_nr+"\""+" id=\""+luId+"\""+" direction=\""+"source\"/>\n";
                                    str += "\t\t<lu sense=\""+oSeq_nr+"\""+" id=\""+oLuId+"\""+" direction=\""+"target\"/>\n";
                                }
                                str += "\t</relation>\n";
                            }
                        }
                    }
                }
            }
        }
        str += "</lemma>\n";
        if (related) {
         //   System.out.println("str = " + str);
        }
        else {
            str = "";
        }
        return str;
    }


    /// cohyponym are shared hyerponym semantic relations between different senses of the same word
    /*

	    	<relation type="co-hyponyms">
		   <lu sense="1" id="r_n-10301"/>
		   <lu sense="2" id="r_n-10302"/>
	    	</relation>
     */
    static public String getCoHyponymRelations (String word, ArrayList<CdbLuForm> luIds, CdbSynsetParser synsetParser) {
        String str = "";
        boolean related = false;
        str = "<lemma spelling=\""+word+"\">\n";
        for (int i = 0; i < luIds.size(); i++) {
            CdbLuForm luForm = luIds.get(i);
            String luId = luForm.getLuId();
            String seq_nr = luForm.getSeqNr();
            if (synsetParser.luIdSynsetMap.containsKey(luId)) {
                CdbSynset synset = synsetParser.luIdSynsetMap.get(luId);
                ArrayList<String> hypers = new ArrayList<String>();
                ArrayList<CdbInternalRelation> rels = synset.getIRelations();
                for (int j = 0; j < rels.size(); j++) {
                    CdbInternalRelation cdbInternalRelation = rels.get(j);
                    if (cdbInternalRelation.getRelation_name().equalsIgnoreCase("HAS_HYPERONYM")) {
                       hypers.add(cdbInternalRelation.getTarget());
                    }
                }
                if (hypers.size()>0) {
                    for (int j = i+1; j < luIds.size(); j++) {
                        CdbLuForm oLuForm = luIds.get(j);
                        String oLuId = oLuForm.getLuId();
                        String oSeq_nr = oLuForm.getSeqNr();
                        if (synsetParser.luIdSynsetMap.containsKey(oLuId)) {
                            CdbSynset oSynset = synsetParser.luIdSynsetMap.get(oLuId);
                            ArrayList<CdbInternalRelation> oRels = oSynset.getIRelations();
                            for (int k = 0; k < oRels.size(); k++) {
                                CdbInternalRelation oCdbInternalRelation = oRels.get(k);
                                if (hypers.contains(oCdbInternalRelation.getTarget())) {
                                    //// NOTE THAT WE DO NOT CHECK THE RELATION. IT IS MOSTLY HAS_HYPERONYM AS WELL AND IF NOT THE SENSES ARE ALSO CLOSELY RELATED
                                    related = true;
                                    str += "\t<relation type=\""+"co-hyponyms"+"\">\n";
                                    str += "<!-- "+oCdbInternalRelation.getPreviewtext()+" -->\n";
                                    str += "\t\t<lu sense=\""+seq_nr+"\""+" id=\""+luId+"\""+"/>\n";
                                    str += "\t\t<lu sense=\""+oSeq_nr+"\""+" id=\""+oLuId+"\""+"/>\n";
                                    str += "\t</relation>\n";
                                }
                            }
                        }
                    }
                }
            }
        }
        str += "</lemma>\n";
        if (related) {
          //  System.out.println("str = " + str);
        }
        else {
            str = "";
        }
        return str;
    }


/*
    		<relation type="co-synonyms" freq="2">
		   <lu sense="1" id="r_n-37823"/>
		   <lu sense="2" id="r_n-37824"/>
	    	</relation>
*/

    static public String getCoSynonymRelations (String word, ArrayList<CdbLuForm> luIds, CdbSynsetParser synsetParser, CdbLuMapFastDomParser luMapFastDomParser) {
        String str = "";
        boolean related = false;
        str = "<lemma spelling=\""+word+"\">\n";
        for (int i = 0; i < luIds.size(); i++) {
            CdbLuForm luForm = luIds.get(i);
            String luId = luForm.getLuId();
            String seq_nr = luForm.getSeqNr();
            if (synsetParser.luIdSynsetMap.containsKey(luId)) {
                CdbSynset synset = synsetParser.luIdSynsetMap.get(luId);
                ArrayList<String> synForms = new ArrayList<String>();
                ArrayList<Syn> synonyms = synset.getSynonyms();
                for (int j = 0; j < synonyms.size(); j++) {
                    Syn syn = synonyms.get(j);
                    if (luMapFastDomParser.luIdMapCdbLuFormList.containsKey(syn.getC_lu_id())) {
                        ArrayList<CdbLuForm> cdbLuForms = luMapFastDomParser.luIdMapCdbLuFormList.get(syn.getC_lu_id());
                        for (int k = 0; k < cdbLuForms.size(); k++) {
                            CdbLuForm cdbLuForm = cdbLuForms.get(k);
                            synForms.add(cdbLuForm.getFormSpelling());
                        }
                    }
                }
                if (synForms.size()>0) {
                    for (int j = i+1; j < luIds.size(); j++) {
                        CdbLuForm oLuForm = luIds.get(j);
                        String oLuId = oLuForm.getLuId();
                        String oSeq_nr = oLuForm.getSeqNr();
                        if (synsetParser.luIdSynsetMap.containsKey(oLuId)) {
                            CdbSynset oSynset = synsetParser.luIdSynsetMap.get(oLuId);
                            int nCoSynonyms = 0;
                            ArrayList<Syn> oSynonyms = oSynset.getSynonyms();
                            for (int k = 0; k < oSynonyms.size(); k++) {
                                Syn oSyn = oSynonyms.get(k);
                                if (luMapFastDomParser.luIdMapCdbLuFormList.containsKey(oSyn.getC_lu_id())) {
                                    ArrayList<CdbLuForm> cdbLuForms = luMapFastDomParser.luIdMapCdbLuFormList.get(oSyn.getC_lu_id());
                                    for (int l = 0; l < cdbLuForms.size(); l++) {
                                        CdbLuForm cdbLuForm = cdbLuForms.get(l);
                                        if (synForms.contains(cdbLuForm.getFormSpelling())) {
                                            nCoSynonyms++;
                                        }
                                    }
                                }
                            }
                            if (nCoSynonyms>1) {
                                //// NOTE THAT WE DO NOT CHECK THE RELATION. IT IS MOSTLY HAS_HYPERONYM AS WELL AND IF NOT THE SENSES ARE ALSO CLOSELY RELATED
                                related = true;
                                str += "\t<relation type=\""+"co-synonyms"+"\" freq=\""+nCoSynonyms+"\">\n";
                                str += "\t\t<lu sense=\""+seq_nr+"\""+" id=\""+luId+"\""+"/>\n";
                                    str += "<!-- ";
                                    for (int k = 0; k < synonyms.size(); k++) {
                                        Syn syn = synonyms.get(k);
                                        str += " "+syn.getPreviewtext();
                                    }
                                    str += " -->\n";
                                str += "\t\t<lu sense=\""+oSeq_nr+"\""+" id=\""+oLuId+"\""+"/>\n";
                                    str += "<!-- ";
                                    for (int k = 0; k < oSynonyms.size(); k++) {
                                        Syn syn = oSynonyms.get(k);
                                        str += " "+syn.getPreviewtext();
                                    }
                                    str += " -->\n";
                                str += "\t</relation>\n";
                            }
                        }
                    }
                }
            }
        }
        str += "</lemma>\n";
        if (related) {
         //   System.out.println("str = " + str);
        }
        else {
            str = "";
        }
        return str;
    }


    static public void main (String[] args) {
        try {
            String pathToCdbSynFile = args[0];
            String pathToCdbLuFile = args[1];
            FileOutputStream fosRel = new FileOutputStream(pathToCdbLuFile+"sense-groups.rel.xml");
            FileOutputStream fosHyp = new FileOutputStream(pathToCdbLuFile+"sense-groups.hyp.xml");
            FileOutputStream fosSyn = new FileOutputStream(pathToCdbLuFile+"sense-groups.syn.xml");
            String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
            fosRel.write(str.getBytes());
            fosHyp.write(str.getBytes());
            fosSyn.write(str.getBytes());
            str = "<file name=\"sense-groups.rel.xml\">\n";
            fosRel.write(str.getBytes());
            str = "<file name=\"sense-groups.hyp.xml\">\n";
            fosHyp.write(str.getBytes());
            str = "<file name=\"sense-groups.syn.xml\">\n";
            fosSyn.write(str.getBytes());
            CdbLuMapFastDomParser luMapFastDomParser = new CdbLuMapFastDomParser();
            luMapFastDomParser.readLuFile(pathToCdbLuFile);
            CdbSynsetParser synsetParser = new CdbSynsetParser();
            synsetParser.parseFile(pathToCdbSynFile);
            Set keySet = luMapFastDomParser.lemmaMapCdbLuFormListMap.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
              //  System.out.println("key = " + key);
                ArrayList<CdbLuForm> luIds = luMapFastDomParser.lemmaMapCdbLuFormListMap.get(key);
              //  System.out.println("luIds = " + luIds);
                str = getRelatedPolysemyRelations(key, luIds, synsetParser);
                if (!str.isEmpty()) {
                    fosRel.write(str.getBytes());
                }
                str = getCoHyponymRelations(key, luIds, synsetParser);
                if (!str.isEmpty()) {
                    fosHyp.write(str.getBytes());
                }
                str = getCoSynonymRelations(key, luIds, synsetParser, luMapFastDomParser);
                if (!str.isEmpty()) {
                    fosSyn.write(str.getBytes());
                }
            }
            str = "</file>\n";
            fosRel.write(str.getBytes());
            fosHyp.write(str.getBytes());
            fosSyn.write(str.getBytes());
            fosRel.close();
            fosHyp.close();
            fosSyn.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}

