package vu.cornetto.stats;

import vu.cornetto.cdb.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kyoto
 * Date: 2/28/12
 * Time: 8:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetEditorStats {

    static int nForms = 0;
    static int nLUs = 0;
    static int nSynsets = 0;
    static int nSynonyms = 0;
    static int nRelations = 0;
    static int nExamples = 0;
    static int nResumes = 0;

    static void initCounts () {
          nForms = 0;
          nLUs = 0;
          nSynsets = 0;
          nSynonyms = 0;
          nRelations = 0;
          nExamples = 0;
          nResumes = 0;
    }

    static String toCounts () {
        String str ="";
        str += "nr of Forms\t"+nForms+"\n";
        str += "nr of Lus\t"+nLUs+"\n";
        str += "nr of Synsets\t"+nSynsets+"\n";
        str += "nr of Synonyms\t"+nSynonyms+"\n";
        str += "nr of Relations\t"+nRelations+"\n";
        str += "nr of Examples\t"+nExamples+"\n";
        str += "nr of Resumes\t"+nResumes+"\n";
        return str;
    }

    static public void main (String[] args) {
        String cidPath = args[0];
        String luPath = args[2];
        String synsetPath = args[1];
        CdbSynsetParser synsetParser = new CdbSynsetParser();
        synsetParser.parseFile(synsetPath);

        CdbLuDomParser luParser = new CdbLuDomParser();
        luParser.readLuFile(luPath);
        HashMap<String, ArrayList<String>> childMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> tops = new ArrayList<String>() ;
        try {
            FileOutputStream fos = new FileOutputStream(cidPath+".stats.xls");
            FileOutputStream fos2 = new FileOutputStream(cidPath+".stats-overview.xls");
            FileOutputStream animalnet = new FileOutputStream(cidPath+".txt");
            String formStr = "";
            String str = new File(cidPath).getName()+"\n";
            fos.write(str.getBytes());
            animalnet.write(str.getBytes());
            fos2.write(str.getBytes());
            str = "\tNr. Forms\tNr. LUs\tNr. Synsets\tNr. synonyms\tNr. Relations\tNr. Examples\tNr. Resumes\n";
            fos2.write(str.getBytes());
            CdbCidSaxParser cidArraySaxParser = new CdbCidSaxParser();
            cidArraySaxParser.parseFile(cidPath);
            Set keySet = cidArraySaxParser.editorCidMap.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                ArrayList<CdbCid> cids = cidArraySaxParser.editorCidMap.get(key);
                int nSelected = 0;
                CdbCid cdbCid = null;
                for (int i = 0; i < cids.size(); i++) {
                    cdbCid = cids.get(i);
                    if (cdbCid.getSelected()) {
                        nSelected++;
                    }
                }
                initCounts();
                ArrayList<String> forms = new ArrayList<String>();
                String previousForm = "";
                str = key+"\trecords\t"+cids.size()+"\n\tselected\t"+nSelected+"\n";
                for (int i = 0; i < cids.size(); i++) {
                    cdbCid = cids.get(i);
                    if (cdbCid.getSelected()) {
                        if (!forms.contains(cdbCid.getC_form())) {
                            forms.add(cdbCid.getC_form());
                        }
                        str += "\t"+cdbCid.getC_form()+":"+cdbCid.getC_seq_nr()+":"+cdbCid.getC_pos()+":"+cdbCid.getC_lu_id()+":"+cdbCid.getD_sy_id()+":"+cdbCid.getSelected()+"\n";
                        if (!previousForm.equals(cdbCid.getC_form()+":"+cdbCid.getC_seq_nr())) {
                            str += getLuInfo(luParser, cdbCid);
                            previousForm = cdbCid.getC_form()+":"+cdbCid.getC_seq_nr();
                        }
                        if (cdbCid.getSelected()) {
                            str += getSynsetInfo(synsetParser, cdbCid, tops, childMap);
                        }
                    }
                }
                str += "\n";
                fos.write(str.getBytes());
                formStr += key+"\t"+forms.toString()+"\n";
                str = key+"\t"+forms.size()+"\t"+nLUs+"\t"+nSynsets+"\t"+nSynonyms+"\t"+nRelations+"\t"+nExamples	+"\t"+nResumes+"\n";
                fos2.write(str.getBytes());

            }
            fos.close();
            fos2.write(formStr.getBytes());
            fos2.close();
            str = "";
            for (int i = 0; i < tops.size(); i++) {
                String top = tops.get(i);
             //   System.out.println("top = " + top);
                ArrayList<String> children = childMap.get(top);
              //  System.out.println("children = " + children);
                str += getTreeString(synsetParser, childMap, top, 0);
            }
            animalnet.write(str.getBytes());
           // keySet = synsetParser.data.keySet();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    
    static public String getSynsetInfo (CdbSynsetParser synsetParser, CdbCid cdbCid,  ArrayList<String> tops, HashMap<String, ArrayList<String>> childMap) {
        String str = "\tSYNSET-INFO\n";
        if (!cdbCid.getD_sy_id().isEmpty()) {
            if (synsetParser.synsetIdSynsetMap.containsKey(cdbCid.getD_sy_id())) {
                CdbSynset synset = (CdbSynset) synsetParser.synsetIdSynsetMap.get(cdbCid.getD_sy_id());
                str += "\t\tNR SYNONYMS\t"+synset.getSynonyms().size()+"\n";
                str += "\t\tSYNONYMS\t"+synset.toFlatSynonymString()+"\n";
                str += "\t\tNR RELATIONS\t"+ synset.getIRelations().size()+"\n";
                str += "\t\tRELATIONS\t";
                boolean hasHyper = false;
                nSynsets++;
                nSynonyms += synset.getSynonyms().size();
                nRelations+=synset.getIRelations().size();
                for (int j = 0; j < synset.getIRelations().size(); j++) {
                    CdbInternalRelation  rel = (CdbInternalRelation) synset.getIRelations().get(j);
                    str += rel.getRelation_name()+":"+rel.getPreviewtext()+"\t";
                    if (rel.getRelation_name().equalsIgnoreCase("HAS_HYPERONYM")) {
                        hasHyper = true;
                        String hyper = rel.getTarget();
                        if (childMap.containsKey(hyper)) {
                            ArrayList<String> children = childMap.get(hyper);
                            if (!children.contains(cdbCid.getD_sy_id())) {
                                children.add(cdbCid.getD_sy_id());
                                childMap.put(hyper, children);
                            }
                        }
                        else {
                            ArrayList<String> children = new ArrayList<String>();
                            children.add(cdbCid.getD_sy_id());
                            childMap.put(hyper, children);
                        }
                    }
                }
                if (!hasHyper) {
                    tops.add(cdbCid.getD_sy_id());
                    //  System.out.println("top cdbCid = " + cdbCid.getD_sy_id());
                }
            }
            else {
                str += "\t\tSYNSET NOT FOUND";
            }
            str += "\n";
        }
        return str;
    }
    
    static public String getLuInfo (CdbLuDomParser luParser, CdbCid cdbCid) {
        String str ="\tLU-INFO\n";
        if (luParser.luMap.containsKey(cdbCid.getC_lu_id())) {
            Document doc = luParser.luMap.get(cdbCid.getC_lu_id());
            Node node = luParser.getSubNode(doc, "sem-resume");
            if (node!=null) {
                String resume = node.getTextContent();
                str += "\t\tRESUME\t"+resume+"\n";
                nResumes++;
            }
            /*
               <textualform>
               <canonicalform>
            */
            nLUs++;
            int count = 0;
            String exampleString = "\t\tEXAMPLES\t";
            node = luParser.getSubNode(doc, "examples");
            if (node!=null) {
                NodeList children = node.getChildNodes();
                for (int c=0;c<children.getLength();c++) {
                    Node child = children.item(c);
                    if (child!=null) {
                        if (child.getNodeName().equalsIgnoreCase("example")) {
                            Node canonicalform = luParser.getSubNode(child, "canonicalform");
                            if (canonicalform!=null) {
                                if (!canonicalform.getTextContent().isEmpty()) {
                                    count++;
                                    exampleString += canonicalform.getTextContent()+"\t";
                                }
                            }
                            Node textualform = luParser.getSubNode(child, "textualform");
                            if (textualform!=null) {
                                if (!textualform.getTextContent().isEmpty()) {
                                    count++;
                                    exampleString +=textualform.getTextContent()+"\t";
                                }
                            }
                        }
                    }
                }
            }
            nExamples += count;
            str += "\t\tNR EXAMPLES\t"+nExamples+"\n";
            str += exampleString+"\n";
        }
        return str;
    }
    
    static public String getTreeString (CdbSynsetParser synsetParser, HashMap<String, ArrayList<String>> childMap, String synsetId, int level){
        String str = "";
        String tab = "";
        for (int i = 0; i < level; i++) {
           tab += "  ";
        }
        if (synsetParser.synsetIdSynsetMap.containsKey(synsetId)) {
            CdbSynset synset = (CdbSynset) synsetParser.synsetIdSynsetMap.get(synsetId);
            if (level>0) {
                str = tab+"HAS_HYPONYM:";
            }
            str += synset.toFlatSynonymString()+"#"+synset.getC_sy_id();
            for (int j = 0; j < synset.getIRelations().size(); j++) {
                CdbInternalRelation  rel = (CdbInternalRelation) synset.getIRelations().get(j);
                if ((!rel.getRelation_name().equalsIgnoreCase("HAS_HYPERONYM")) &&
                    (!rel.getRelation_name().equalsIgnoreCase("HAS_HYPONYM"))) {
                    str +="["+rel.getRelation_name()+":"+rel.getPreviewtext()+"]";
                }
            }
        }
        else {
            if (level>0) {
                str = tab+"HAS_HYPONYM:";
            }
            str += synsetId;
        }
        str += "\n";
        if (childMap.containsKey(synsetId)) {
            ArrayList<String> children = childMap.get(synsetId);
            level++;
            for (int i = 0; i < children.size(); i++) {
                String c = children.get(i);
                str += tab+getTreeString(synsetParser, childMap, c, level);
            }
        }
        return str;
    }
}
