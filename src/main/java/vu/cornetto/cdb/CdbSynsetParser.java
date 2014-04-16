package vu.cornetto.cdb;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import vu.cornetto.util.FileProcessor;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 11-jul-2007
 * Time: 18:01:13
 * To change this template use File | Settings | File Templates.
 */
public class CdbSynsetParser  extends DefaultHandler {


    long nounSS;
    long verbSS;
    long adjSS;
    long advSS;
    long otherSS;
    long nounLU;
    long verbLU;
    long adjLU;
    long advLU;
    long otherLU;

    public boolean suppressVdl = false;
    public boolean bigLog = false;
    long nTags;
    long nAttrributes;
    long nSynonyms;
    long nDefinitions;
    long nDiffs;
    long nVlisDomains;
    long nWnDomains;
    long nSumos;
    long nInternalRelations;
    long nEquivalenceRelations;
    long nBaseConcepts;
    long nAuthors;
    HashMap tagCount;
    String value = "";
    CdbSynset cdbSynset;
    CdbEquivalenceRelation equi;
    CdbInternalRelation inter;
    Authorship author;
    public HashMap<String, CdbSynset> synsetIdSynsetMap;
    public HashMap<String, CdbSynset> luIdSynsetMap;
    public HashMap<String, ArrayList<CdbSynset>> luIdMapSynsetList;
    public HashMap<String, ArrayList<String>> lemmaMapSynsetIdList;

    HashMap interTypes;
    HashMap equiTypes;
    HashMap domTypes;
    HashMap sumTypes;
    int nSynsets;
    boolean INTER;
    boolean EQUI;
    boolean VLISDOMAIN;
    boolean WNDOMAIN;
    final String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<cdb>";

    String [] inters = {"HAS_HYPERONYM",
                        "HAS_HYPONYM",
                        "HAS_XPOS_HYPERONYM",
                        "HAS_XPOS_HYPONYM",
                        "XPOS_NEAR_SYNONYM",
                        "HAS_HOLONYM",
                        "HAS_HOLO_LOCATION",
                        "HAS_HOLO_MADEOF",
                        "HAS_HOLO_MEMBER",
                        "HAS_HOLO_PART",
                        "HAS_HOLO_PORTION",
                        "HAS_MERONYM",
                        "HAS_MERO_LOCATION",
                        "HAS_MERO_MADEOF",
                        "HAS_MERO_MEMBER",
                        "HAS_MERO_PART",
                        "HAS_MERO_PORTION",
                        "HAS_SUBEVENT",
                        "IS_SUBEVENT_OF",
                        "ROLE",
                        "ROLE_AGENT",
                        "ROLE_DIRECTION",
                        "ROLE_INSTRUMENT",
                        "ROLE_LOCATION",
                        "ROLE_PATIENT",
                        "ROLE_RESULT",
                        "ROLE_SOURCE_DIRECTION",
                        "ROLE_TARGET_DIRECTION",
                        "INVOLVED",
                        "INVOLVED_AGENT",
                        "INVOLVED_DIRECTION",
                        "INVOLVED_INSTRUMENT",
                        "INVOLVED_LOCATION",
                        "INVOLVED_PATIENT",
                        "INVOLVED_RESULT",
                        "INVOLVED_SOURCE_DIRECTION",
                        "INVOLVED_TARGET_DIRECTION",
                        "CO_ROLE",
                        "CO_AGENT_INSTRUMENT",
                        "CO_AGENT_PATIENT",
                        "CO_AGENT_RESULT",
                        "CO_INSTRUMENT_AGENT",
                        "CO_INSTRUMENT_PATIENT",
                        "CO_INSTRUMENT_RESULT",
                        "CO_PATIENT_AGENT",
                        "CO_PATIENT_INSTRUMENT",
                        "CO_RESULT_AGENT",
                        "CO_RESULT_INSTRUMENT",
                        "STATE_OF",
                        "BE_IN_STATE",
                        "CAUSES",
                        "IS_CAUSED_BY",
                        "MANNER_OF",
                        "IN_MANNER",
                        "XPOS_NEAR_ANTONYM",
                        "NEAR_ANTONYM",
                        "NEAR_SYNONYM",
                        "FUZZYNYM",
                        "XPOS_FUZZYNYM",
                        "UNSPECIFIED"};
    int [] interCounts= new int [inters.length];
    String [] equis = {"EQ_SYNONYM",
                        "EQ_NEAR_SYNONYM",
                        "EQ_HAS_HYPERONYM",
                        "EQ_HAS_HYPERNYM",
                        "EQ_HAS_HYPONYM",
                        "EQ_HAS_HOLONYM",
                        "EQ_HAS_MERONYM",
                        "EQ_ROLE",
                        "EQ_INVOLVED",
                        "EQ_CO_ROLE",
                        "EQ_IS_CAUSED_BY",
                        "EQ_CAUSES",
                        "EQ_BE_IN_STATE",
                        "EQ_IS_STATE_OF",
                        "EQ_HAS_SUBEVENT",
                        "EQ_IS_SUBEVENT_OF",
                        "EQ_UNSPECIFIED"};
    int [] equiCounts= new int [equis.length];

    
    public CdbSynsetParser() {
        synsetIdSynsetMap = new HashMap<String, CdbSynset>();
        luIdSynsetMap = new HashMap<String, CdbSynset>();
        luIdMapSynsetList = new HashMap<String, ArrayList<CdbSynset>>();
        lemmaMapSynsetIdList = new HashMap<String, ArrayList<String>>();
        tagCount = new HashMap();
        bigLog = false;
    }

    public CdbSynset getCdbSynset () {
        return this.cdbSynset;
    }

    void printTagCounts () {
        Set keySet = tagCount.keySet();
        Iterator keys = keySet.iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            Long l = (Long) tagCount.get(key);
            System.out.println(key+"\t"+l);
        }
    }

    void initCounts () {
        nounSS=0;
        verbSS=0;
        adjSS=0;
        advSS=0;
        otherSS=0;
        nounLU=0;
        verbLU=0;
        adjLU=0;
        advLU=0;
        otherLU=0;

        nTags = 0;
        nAttrributes = 0;
        nSynonyms = 0;
        nDefinitions = 0;
        nDiffs = 0;
        nVlisDomains = 0;
        nWnDomains = 0;
        nSumos = 0;
        nInternalRelations = 0;
        nEquivalenceRelations = 0;
        nBaseConcepts = 0;
        nAuthors = 0;
        interTypes = new HashMap();
        equiTypes = new HashMap();
        domTypes = new HashMap();
        sumTypes = new HashMap();
    }

    void printCounts () {
        System.out.println("nSynsets = " + nSynsets);
        System.out.println("nTags = " + nTags);
        System.out.println("nAttrributes = " + nAttrributes);
        System.out.println("nSynonyms = " + nSynonyms);
        System.out.println("nInternalRelations = " + nInternalRelations);
        System.out.println("nEquivalenceRelations = " + nEquivalenceRelations);
        System.out.println("nAuthors = " + nAuthors);
        System.out.println("nDiffs = " + nDiffs);
        System.out.println("nDefinitions = " + nDefinitions);
        System.out.println("nVlisDomains = " + nVlisDomains);
        System.out.println("nWnDomains = " + nWnDomains);
        System.out.println("nSumos = " + nSumos);
        if (bigLog) {
            System.out.println("nounSS = " + nounSS);
            System.out.println("verbSS = " + verbSS);
            System.out.println("adjSS = " + adjSS);
            System.out.println("advSS = " + advSS);
            System.out.println("otherSS = " + otherSS);
            System.out.println("nounLU = " + nounLU);
            System.out.println("verbLU = " + verbLU);
            System.out.println("adjLU = " + adjLU);
            System.out.println("advLU = " + advLU);
            System.out.println("otherLU = " + otherLU);
            Set keySet = interTypes.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Integer count = (Integer) interTypes.get(key);
                if (key.length()==0) {
                    key = "UNSPECIFIED";
                }
                int idx = vu.cornetto.util.Other.idxStringArray(key, inters);
                if (idx==-1) {
                    System.out.println("UNKNOWN key = " + key);
                }
                else {
                    interCounts[idx]= count.intValue();
                }
                //System.out.println("key= " + key+"\t"+count.intValue());
            }
            for (int i = 0; i < inters.length; i++) {
                String s = inters[i];
                int count = interCounts[i];
                System.out.println(s+"\t"+count);
            }
            keySet = equiTypes.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Integer count = (Integer) equiTypes.get(key);
                if (key.length()==0) {
                    key = "EQ_UNSPECIFIED";
                }
                int idx = vu.cornetto.util.Other.idxStringArray(key, equis);
                if (idx==-1) {
                    System.out.println("UNKNOWN key = " + key);
                }
                else {
                    equiCounts[idx]= count.intValue();
                }
                //System.out.println("key= " + key+"\t"+count.intValue());
            }
            for (int i = 0; i < equis.length; i++) {
                String s = equis[i];
                int count = equiCounts[i];
                System.out.println(s+"\t"+count);
            }
            keySet = domTypes.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Integer count = (Integer) domTypes.get(key);
                System.out.println("key= " + key+"\t"+count.intValue());
            }
            keySet = sumTypes.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Integer count = (Integer) sumTypes.get(key);
                System.out.println("key= " + key+"\t"+count.intValue());
            }
        }
    }
    void printCounts2 () {
        System.out.println("\nnTags\t" + nTags);
        System.out.println("nAttrributes\t" + nAttrributes);
        System.out.println("nSynsets\t" + nSynsets);
        System.out.println("nSynonyms\t" + nSynonyms);
        System.out.println("nInternalRelations\t" + nInternalRelations);
        System.out.println("nEquivalenceRelations\t" + nEquivalenceRelations);
        System.out.println("nDefinitions\t" + nDefinitions);
        System.out.println("nWnDomains\t" + nWnDomains);
        System.out.println("nSumos\t" + nSumos);
        System.out.println("nAuthors\t" + nAuthors);
        System.out.println("nDiffs\t" + nDiffs);
        System.out.println("nVlisDomains\t" + nVlisDomains);
        if (bigLog) {
            System.out.println("\tALL\tNouns\tVerbs\tAdjectives\tAdverbs\tOthers\n");
            System.out.println("Synset\t" +(nounSS+verbSS+adjSS+advSS+otherSS)+"\t"+ nounSS+"\t"+verbSS+"\t"+adjSS+"\t"+advSS+"\t"+otherSS);
            System.out.println("Synonyms in synsets\t" + (nounLU + verbLU+ adjLU+ advLU+ otherLU)+"\t"+ nounLU +"\t"+ verbLU+"\t"+ adjLU+"\t"+ advLU+"\t"+ otherLU);
            System.out.println("\nINTER RELATIONS");
            Set keySet = interTypes.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Integer count = (Integer) interTypes.get(key);
                if (key.length()==0) {
                    key = "UNSPECIFIED";
                }
                int idx = vu.cornetto.util.Other.idxStringArray(key, inters);
                if (idx==-1) {
                    System.out.println("UNKNOWN key = " + key);
                }
                else {
                    interCounts[idx]= count.intValue();
                }
                //System.out.println("key= " + key+"\t"+count.intValue());
            }
            for (int i = 0; i < inters.length; i++) {
                String s = inters[i];
                int count = interCounts[i];
                System.out.println(s+"\t"+count);
            }
            System.out.println("\nEQUI RELATIONS");
            keySet = equiTypes.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Integer count = (Integer) equiTypes.get(key);
                if (key.length()==0) {
                    key = "EQ_UNSPECIFIED";
                }
                int idx = vu.cornetto.util.Other.idxStringArray(key, equis);
                if (idx==-1) {
                    System.out.println("UNKNOWN key = " + key);
                }
                else {
                    equiCounts[idx]= count.intValue();
                }
                //System.out.println("key= " + key+"\t"+count.intValue());
            }
            for (int i = 0; i < equis.length; i++) {
                String s = equis[i];
                int count = equiCounts[i];
                System.out.println(s+"\t"+count);
            }
            System.out.println("\nDOM RELATIONS");
            keySet = domTypes.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Integer count = (Integer) domTypes.get(key);
                System.out.println(key+"\t"+count.intValue());
            }
            System.out.println("\nSUMO RELATIONS");
            keySet = sumTypes.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Integer count = (Integer) sumTypes.get(key);
                System.out.println( key+"\t"+count.intValue());
            }
        }
    }

    public void parseFile(String filePath) {
        initCounts();
        String myerror = "";
        cdbSynset = new CdbSynset();
        author = new Authorship ();
        equi = new CdbEquivalenceRelation();
        inter = new CdbInternalRelation();
        //data = new HashMap();
        nSynsets = 0;
        INTER = false;
        EQUI = false;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            InputSource inp = new InputSource (new FileReader (filePath));
            parser.parse(inp, this);
            System.out.println("imported synset data = " + synsetIdSynsetMap.size());
            System.out.println("lemmaMapSynsetIdList = " + lemmaMapSynsetIdList.size());
           // printTagCounts();
           // printCounts2();
        } catch (SAXParseException err) {
            myerror = "\n** Parsing error" + ", line " + err.getLineNumber()
                    + ", uri " + err.getSystemId();
            myerror += "\n" + err.getMessage();
            System.out.println("myerror = " + myerror);
            System.out.println("nSynsets = " + nSynsets);
            System.out.println("cdbSynset = " + cdbSynset.getC_sy_id());
        } catch (SAXException e) {
            Exception x = e;
            if (e.getException() != null)
                x = e.getException();
            myerror += "\nSAXException --" + x.getMessage();
            System.out.println("myerror = " + myerror);
            System.out.println("nSynsets = " + nSynsets);
            System.out.println("cdbSynset = " + cdbSynset.getC_sy_id());
        } catch (Exception eee) {
            myerror += "\nException --" + eee.getMessage();
            System.out.println("myerror = " + myerror);
            System.out.println("nSynsets = " + nSynsets);
            System.out.println("cdbSynset = " + cdbSynset.getC_sy_id());
        }
        System.out.println("myerror = " + myerror);
    }//--c

    public void parseFile(File file) {
        initCounts();
        String myerror = "";
        cdbSynset = new CdbSynset();
        author = new Authorship ();
        equi = new CdbEquivalenceRelation();
        inter = new CdbInternalRelation();
        //data = new HashMap();
        nSynsets = 0;
        INTER = false;
        EQUI = false;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            InputSource inp = new InputSource (new FileReader (file));
            parser.parse(inp, this);
            System.out.println("imported synset data = " + synsetIdSynsetMap.size());
            printTagCounts();
            printCounts2();
        } catch (SAXParseException err) {
            myerror = "\n** Parsing error" + ", line " + err.getLineNumber()
                    + ", uri " + err.getSystemId();
            myerror += "\n" + err.getMessage();
            System.out.println("myerror = " + myerror);
            System.out.println("nSynsets = " + nSynsets);
            System.out.println("cdbSynset = " + cdbSynset.getC_sy_id());
        } catch (SAXException e) {
            Exception x = e;
            if (e.getException() != null)
                x = e.getException();
            myerror += "\nSAXException --" + x.getMessage();
            System.out.println("myerror = " + myerror);
            System.out.println("nSynsets = " + nSynsets);
            System.out.println("cdbSynset = " + cdbSynset.getC_sy_id());
        } catch (Exception eee) {
            myerror += "\nException --" + eee.getMessage();
            System.out.println("myerror = " + myerror);
            System.out.println("nSynsets = " + nSynsets);
            System.out.println("cdbSynset = " + cdbSynset.getC_sy_id());
        }
        System.out.println("myerror = " + myerror);
    }//--c




/*
<cdb_synset c_sy_id="d_n-9840" d_synset_id="d_n-9840">
	<synonyms>
		<synonym c_lu_id="d_n-298575" c_cid_id="" status=""/>
		<synonym c_lu_id="d_n-291088" c_cid_id="114144" status=""/>
		<synonym c_lu_id="d_n-539814" c_cid_id="128551" status=""/>
	</synonyms>
	<wn_internal_relations>
			<relation relation_name="HAS_HYPERONYM" target="d_n-14736" coordinative="false" disjunctive="false" reversed="false" factive="" negative="false">
				<author name="Paul" source_id="d_n-9840" date="19961206" score="0.0" status=""/>
			</relation>
	</wn_internal_relations>
	<wn_equivalence_relations>
		<relation relation_name="EQ_NEAR_SYNONYM" version="pwn_1_5" target="ENG15-02697740-n" target20="ENG20-04172735-n">
			<author name="Paul" source_id="HEURISTICS_BI" date="19970908" score="7500.0" status=""/>
		</relation>
	</wn_equivalence_relations>
	<sumo_relations>
		<ont_relation relation_name="+" arg1="" arg2="Device" negative="false" name="dwn10_pwn15_pwn20_mapping" status="false"/>
	</sumo_relations>
	<wn_domains>
		<dom_relation  term="factotum" name="dwn10_pwn15_pwn16_mapping" status="false"/>
	</wn_domains>
</cdb_synset>
 */

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException {
       //System.out.println("qName = " + qName);
        if (tagCount.containsKey(qName)) {
            Long n = (Long) tagCount.get(qName);
            long l = n.longValue();
            l++;
            tagCount.put(qName, new Long(l));
        }
        else {
            tagCount.put(qName, new Long(1));
        }
        this.nTags++;
        this.nAttrributes += attributes.getLength();
        if (qName.equalsIgnoreCase("cdb_synset")) {
            cdbSynset = new CdbSynset();
            parserCdbSynset(attributes);
        }
        else if (qName.equalsIgnoreCase("synonym")) {
            this.parserSynonyms(attributes);
        }
        else if (qName.equalsIgnoreCase("wn_equivalence_relations")) {
           EQUI = true;
        }
        else if (qName.equalsIgnoreCase("wn_internal_relations")) {
           INTER = true;
        }
        else if (qName.equalsIgnoreCase("vlis_domains")) {
             VLISDOMAIN = true;
        }
        else if (qName.equalsIgnoreCase("wn_domains")) {
             WNDOMAIN = true;
        }
        else if (qName.equalsIgnoreCase("relation")) {
            if (EQUI) {
                parserEquivalent(attributes);
            }
            else if (INTER) {
                parserInter(attributes);
            }
        }
        else if (qName.equalsIgnoreCase("author")) {
            parserAuthor(attributes);
        }
        else if (qName.equalsIgnoreCase("ont_relation")) {
            this.parserSumo(attributes);
        }
        else if (qName.equalsIgnoreCase("dom_relation")) {
            if (WNDOMAIN) {
                this.parserWnDomain(attributes);
            }
            else if (VLISDOMAIN) {
                if (!suppressVdl) {
                    this.parserVlisDomain(attributes);
                }
            }
        }

        value = "";
    }//--startElement
    /*
          <wn_domains>
        <dom_relation status="true" name="dwn10_pwn15_pwn16_mapping" term="play"/>
      </wn_domains>
      <differentiae><![CDATA[]]></differentiae>
      <base_concept>false</base_concept>
      <vlis_domains>
  		<dom_relation term="SPORT"/>
  	</vlis_domains>

      <wn_equivalence_relations>
        <relation relation_name="EQ_SYNONYM" target20-previewtext="goal:3" version="pwn_1_5" target20="ENG20-03316986-n" pos="" target="ENG15-02565248-n">
          <author score="5952.0" status="YES" name="Laura" date="19980904" source_id=""/>
        </relation>
      </wn_equivalence_relations>
      <definition><![CDATA[ruimte waardoor of waarin bij sommige balspelen de bal gegooid, geschopt enz. moet worden]]></definition>
      <sumo_relations>
        <ont_relation status="true" name="dwn10_pwn15_pwn20_mapping" negative="false" relation_name="=" arg1="" arg2="GameGoal"/>
      </sumo_relations>
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equals("cdb_synset")) {
         //   System.out.println("cdbSynset = " + cdbSynset.getC_sy_id());
            if (synsetIdSynsetMap.containsKey(cdbSynset.getC_sy_id())) {
                System.out.println("duplicate cdbSynset = " + cdbSynset.getC_sy_id());
            }
            synsetIdSynsetMap.put(cdbSynset.getC_sy_id(), cdbSynset);
            for (int i = 0; i < cdbSynset.getSynonyms().size(); i++) {
                Syn syn = (Syn) cdbSynset.getSynonyms().get(i);
                if (!luIdSynsetMap.containsKey(syn.getC_lu_id())) {
                    luIdSynsetMap.put(syn.getC_lu_id(), cdbSynset);
                }
                else {
                    //// Already included as duplicate, only add the new synset
                    if (luIdMapSynsetList.containsKey(syn.getC_lu_id())) {
                        ArrayList<CdbSynset> synsets = luIdMapSynsetList.get(syn.getC_lu_id());
                        synsets.add(cdbSynset);
                        luIdMapSynsetList.put(syn.getC_lu_id(), synsets);
                    }
                    else {
                        /// we need to store the one stored and the new one
                        CdbSynset oSynset = luIdSynsetMap.get(syn.getC_lu_id());
                        ArrayList<CdbSynset> synsets = new ArrayList<CdbSynset>();
                        synsets.add(cdbSynset);
                        synsets.add(oSynset);
                        luIdMapSynsetList.put(syn.getC_lu_id(), synsets);
                    }
                }
                if (!syn.getForm().isEmpty()) {
                    if (lemmaMapSynsetIdList.containsKey(syn.getForm())) {
                        ArrayList<String> synsets = lemmaMapSynsetIdList.get(syn.getForm());
                        synsets.add(cdbSynset.getC_sy_id());
                        lemmaMapSynsetIdList.put(syn.getForm(), synsets);
                    }
                    else {
                        ArrayList<String> synsets = new ArrayList<String>();
                        synsets.add(cdbSynset.getC_sy_id());
                        lemmaMapSynsetIdList.put(syn.getForm(), synsets);
                    }
                }

            }
            //dataList.add(cdbSynset);
            nSynsets++;
        }

        else if (qName.equals("definition")) {
            if (!suppressVdl) {
                cdbSynset.setDefinition(value);
                if (value.length()>0) {
                    this.nDefinitions++;
                }
            }
        }
        else if (qName.equals("differentiae")) {
            if (!suppressVdl) {
                cdbSynset.setDifferentiae(value);
                this.nDiffs++;
            }
        }
        else if (qName.equals("base_concept")) {
            cdbSynset.setBaseConcept(value);
            this.nBaseConcepts++;
        }
        else if (qName.equals("relation")) {
            /// We processed a relation which can either be a equi or an internal relation
            //// We check if we also processed an author as part of the relation structure
            vu.cornetto.cdb.AlsStatus status = new vu.cornetto.cdb.AlsStatus();
            if ((author.getName().length()>0)) {
                //System.out.println("Good author = " + author.toString("\t"));
                status.setType(author.getId());
                status.setWho(author.getName());
                status.setDate(author.getDate());
                status.setOkay(author.getStatus());
                status.setScore((author.getScore()));
            }
            else {
                //System.out.println("Bad author = " + author.toString("\t"));
            }
            if (EQUI) {
                equi.setAuthor(author);;
                cdbSynset.addERelation(equi);
                author = new Authorship();
                equi = new CdbEquivalenceRelation();
            }
            else if (INTER) {
                if (inter.getRelation_name().length()>0) {
                    inter.setAuthor(author);
                    cdbSynset.addIRelation(inter);
                }
                author = new Authorship();
                inter = new CdbInternalRelation();
            }
        }
        else if (qName.equalsIgnoreCase("wn_equivalence_relations")) {
            EQUI = false;
        }
        else if (qName.equalsIgnoreCase("wn_internal_relations")) {
            INTER = false;
        }
        else if (qName.equalsIgnoreCase("vlis_domains")) {
             VLISDOMAIN = false;
        }
        else if (qName.equalsIgnoreCase("wn_domains")) {
             WNDOMAIN = false;
        }
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {
        value += new String(ch, start, length);
       // System.out.println("tagValue:"+value);
    }


    void parserCdbSynset (Attributes attributes) {
        for (int i=0; i<attributes.getLength();i++) {
            String type = attributes.getQName(i);
            String value = attributes.getValue(type);
            if (type.equals("c_sy_id")) {
                cdbSynset.setC_sy_id(value);
            }
            if (type.equals("d_synset_id")) {
                cdbSynset.setD_synset_id(value);
            }
            if (type.equals("posSpecific")) {
                cdbSynset.setPosSpecific(value);
                if (value.toLowerCase().startsWith("noun")) {
                    nounSS++;
                }
                else if (value.toLowerCase().startsWith("verb")) {
                    verbSS++;
                }
                else if (value.toLowerCase().startsWith("adj")) {
                    adjSS++;
                }
                else if (value.toLowerCase().startsWith("adv")) {
                    advSS++;
                }
                else {
                    otherSS++;
                }

            }
            if (type.equals("comment")) {
                cdbSynset.setComment(value);
            }
        }
    }

    void parserSynonyms (Attributes attributes) {
        Syn syn = new Syn();
        for (int i=0; i<attributes.getLength();i++) {
            String type = attributes.getQName(i);
            String value = attributes.getValue(type);
            if (type.equals("c_lu_id")) {
                syn.setC_lu_id(value);
            }
            if (type.equals("c_cid_id")) {
                syn.setC_cid_id(value);;
            }
            if (type.equals("c_lu_id-previewtext")) {
                syn.setPreviewtext(value);
            }
            if (type.equals("status")) {
                syn.setStatus(value);
            }
        }
        if (cdbSynset.getPosSpecific().toLowerCase().startsWith("noun")) {
            nounLU++;
        }
        else if (cdbSynset.getPosSpecific().toLowerCase().startsWith("verb")) {
            verbLU++;
        }
        else if (cdbSynset.getPosSpecific().toLowerCase().startsWith("adj")) {
            adjLU++;
        }
        else if (cdbSynset.getPosSpecific().toLowerCase().startsWith("adv")) {
            advLU++;
        }
        else {
            otherLU++;
        }
        cdbSynset.addSynonym(syn);
        this.nSynonyms++;
    }

    void parserSumo (Attributes attributes) {
        /*
        	<sumo_relations>
		<ont_relation relation_name="@" arg1="" arg2="TimeInterval" negative="false" name="dwn10_pwn15_pwn20_mapping" status="false"/>
	</sumo_relations>
         */
        vu.cornetto.sumo.SumoRelation sumo = new vu.cornetto.sumo.SumoRelation();
        for (int i=0; i<attributes.getLength();i++) {
            String type = attributes.getQName(i);
            String value = attributes.getValue(type);
            if (type.equals("relation_name")) {
                sumo.setRelation(value);
            }
            else if (type.equals("arg1")) {
                sumo.setArg1(value);
            }
            else if (type.equals("arg2")) {
                sumo.setArg2(value);
            }
            else if (type.equals("name")) {
                sumo.setName(value);
            }
            else if (type.equals("status")) {
                sumo.setStatus(value);
            }
            else if (type.equals("negative")) {
                sumo.setNegative(value);
            }
        }
        if (bigLog) {
            String sumKey = sumo.getRelation()+":"+sumo.getArg1()+":"+sumo.getArg2();
            if (sumTypes.containsKey(sumKey)) {
                Integer count = (Integer) sumTypes.get(sumKey);
                count = new Integer (count.intValue()+1);
                sumTypes.put(value.trim(), count);

            }
            else {
                Integer count = new Integer(1);
                sumTypes.put(sumKey, count);
            }
        }
        cdbSynset.addSRelation(sumo);
        this.nSumos++;
    }

    void parserWnDomain (Attributes attributes) {
        /*
	<wn_domains>
		<dom_relation  term="geology" name="dwn10_pwn15_pwn16_mapping" status="false"/>
	</wn_domains>
         */
        CdbDomainRelation dom = new CdbDomainRelation();
        for (int i=0; i<attributes.getLength();i++) {
            String type = attributes.getQName(i);
            String value = attributes.getValue(type);
            if (type.equals("term")) {
                dom.setaDomain(value);
                if (bigLog) {
                    String [] domArray = value.split(" ");
                    for (int a=0; a<domArray.length;a++) {
                        String aDom = domArray[a];
                        if (domTypes.containsKey(aDom.trim())) {
                            Integer count = (Integer) domTypes.get(aDom.trim());
                            count = new Integer (count.intValue()+1);
                            domTypes.put(aDom.trim(), count);

                        }
                        else {
                            Integer count = new Integer(1);
                            domTypes.put(aDom.trim(), count);
                        }
                    }
                }
            }
            else if (type.equals("status")) {
                dom.setStatus(value);
            }
            else if (type.equals("name")) {
                dom.setName(value);
            }
        }
        //System.out.println("dom.getTerm() = " + dom.getTerm());
        cdbSynset.addDRelation(dom);
        this.nWnDomains++;
    }

    void parserVlisDomain (Attributes attributes) {
        /*
        <vlis_domains>
            <dom_relation term="SCHEEPV"/>
        </vlis_domains>
         */
        CdbDomainRelation dom = new CdbDomainRelation();
        for (int i=0; i<attributes.getLength();i++) {
            String type = attributes.getQName(i);
            String value = attributes.getValue(type);
            if (type.equals("term")) {
                dom.setaDomain(value);
            }
        }
        cdbSynset.addVdRelations(dom);
        this.nVlisDomains++;
    }

    void parserInter (Attributes attributes) {
/*	<wn_internal_relations>
			<relation
			relation_name="HAS_HYPERONYM"
			target="d_n-42534"
			coordinative="false"
			disjunctive="false"
			reversed="false"
			factive="" negative="false">
				<author name="Paul" source_id="d_n-9728" date="19961206" score="0.0" status=""/>
			</relation>*/
        inter = new CdbInternalRelation();
        for (int i=0; i<attributes.getLength();i++) {
            String type = attributes.getQName(i);
            String value = attributes.getValue(type);
            if (type.equals("relation_name")) {
/*
                if (value.trim().equalsIgnoreCase("has_hyponym")) {
                    break;                    
                }
*/
                inter.setRelation_name(value.trim());
                if (bigLog) {
                    if (interTypes.containsKey(value.trim())) {
                        Integer count = (Integer) interTypes.get(value.trim());
                        count = new Integer (count.intValue()+1);
                        interTypes.put(value.trim(), count);

                    }
                    else {
                        Integer count = new Integer(1);
                        interTypes.put(value.trim(), count);
                    }
                }
            }
            if (type.equals("target")) {
                inter.setTarget(value.trim());
            }
            if (type.equals("coordinative")) {
                inter.setCoordinative(value.trim());
           }
            if (type.equals("disjunctive")) {
                inter.setDisjunctive(value.trim());
            }
            if (type.equals("reversed")) {
                inter.setReversed(value.trim());
            }
            if (type.equals("negative")) {
                inter.setNegative(value.trim());
            }
            if (type.equals("target-previewtext")) {
                inter.setPreviewtext(value.trim());
            }
        }
        if (inter.getRelation_name().length()>0) {
            this.nInternalRelations++;
        }
    }


    void parserAuthor(Attributes attributes) {
        for (int i=0; i<attributes.getLength();i++) {
            String type = attributes.getQName(i);
            String value = attributes.getValue(type);
            if (type.equals("name")) {
                author.setName(value);
            }
            if (type.equals("source_id")) {
                author.setId(value);
            }
            if (type.equals("date")) {
                author.setDate(value);
            }
            if (type.equals("score")) {
                if (value.length()>0) {
                    //System.out.println("Value for score = " + value);
                    try {
                        author.setScore(new Double (value).doubleValue());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
                else {
                    //System.out.println("No value for score= " + value);
                }                
            }
            if (type.equals("status")) {
                author.setStatus(value);
            }
        }
        if (author.getName().length()>0) {
            this.nAuthors++;
        }
    }
    void parserEquivalent (Attributes attributes) {
        /*
	<wn_equivalence_relations>
		<relation relation_name="EQ_NEAR_SYNONYM"
		version="pwn_1_5"
		target="ENG15-09075372-n"
		target20="ENG20-14270540-n">
			<author name="Paul" source_id="HEURISTICS_BI" date="19970908" score="5000.0" status=""/>
		</relation>
	</wn_equivalence_relations>
         */
        for (int i=0; i<attributes.getLength();i++) {
            String type = attributes.getQName(i);
            String value = attributes.getValue(type);
            if (type.equals("relation_name")) {
                equi.setRelationName(value.trim());
                if (equiTypes.containsKey(value.trim())) {
                    Integer count = (Integer) equiTypes.get(value.trim());
                    count = new Integer (count.intValue()+1);
                    equiTypes.put(value.trim(), count);

                }
                else {
                    Integer count = new Integer(1);
                    equiTypes.put(value.trim(), count);
                }
            }
            else if (type.equals("target")) {
                equi.setTarget15(value.trim());
            }
            else if (type.equals("target15")) {
                equi.setTarget15(value.trim());
            }
            else if (type.equals("target20")) {
                equi.setTarget20(value.trim());
            }
            else if (type.equals("target30")) {
                equi.setTarget30(value.trim());
            }
            else if (type.equals("target20-previewtext")) {
                equi.setTarget20Previewtext(value.trim());
            }
            else if (type.equals("target20-target20Previewtext")) {
                equi.setTarget20Previewtext(value.trim());
            }
            else if (type.equals("target16")) {
                equi.setTarget16(value.trim());
            }
            else if (type.equals("version")) {
                equi.setVersion(value.trim());
            }
        }
        this.nEquivalenceRelations++;
    }
        ///* This function reads a synset file and stores CdbSynset objects in a hashmap
        ///
        public static void main(String[] args) throws IOException {
            // ENCODING = UTF8;
            try {
                boolean suppress = false;
                String cdbSynsetFile = args[0];
                if (args.length==2) {
                    suppress = args[1].equalsIgnoreCase("--suppress");
                }
                FileOutputStream importOutFile = new FileOutputStream(cdbSynsetFile+".imp.xml");
                OutputStreamWriter synsetOut = new OutputStreamWriter(importOutFile, "UTF-8");
                String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "\n" +
                        "<cdb_synsets>\n";
                FileProcessor.storeResult(synsetOut, str);
                CdbSynsetParser cdbSynsetParser = new CdbSynsetParser();
                if (suppress) {
                    cdbSynsetParser.suppressVdl = true;
                }
                //cdbSynsetParser.parseFileAsBytes(cdbSynsetFile);
                cdbSynsetParser.parseFile(cdbSynsetFile);
                //cdbSynsetParser.parseFile(cdbSynsetFile, "UTF-8");
/*
                for (int c=0; c<cdbSynsetParser.dataList.size();c++) {
                    CdbSynset synset = (CdbSynset) cdbSynsetParser.dataList.get(c);
                    str = synset.toString();
                    FileProcessor.storeResult(synsetOut, str);
                }
*/
                Set keySet = cdbSynsetParser.synsetIdSynsetMap.keySet();
                Iterator keys = keySet.iterator();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    CdbSynset synset = (CdbSynset) cdbSynsetParser.synsetIdSynsetMap.get(key);
                    //str = synset.toString();
                    str = synset.toStringNoRedundant();
                    FileProcessor.storeResult(synsetOut, str);
                }
                str = "</cdb_synsets>\n";
                FileProcessor.storeResult(synsetOut, str);
                synsetOut.flush();
                synsetOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
}
