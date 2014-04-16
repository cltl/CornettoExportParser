package vu.cornetto.conversions;

import vu.cornetto.cdb.CdbEquivalenceRelation;
import vu.cornetto.cdb.CdbInternalRelation;
import vu.cornetto.cdb.CdbSynset;
import vu.cornetto.cdb.CdbSynsetParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: kyoto
 * Date: 9/25/13
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectPredicateMatrix {
    static HashMap<String, ArrayList<ArrayList<String>>> wordNetPredicateMap = new HashMap<String,ArrayList<ArrayList<String>>>();
    static HashMap<String, ArrayList<String>> wordNetLemmaSenseMap = new HashMap<String,ArrayList<String>>();

    static public void main (String[] args) {
        //String pathToPredicateMatrixFile = args[0];
        //String pathToCdbSynsetFile = args[1];
        String pathToPredicateMatrixFile = "/Tools/ontotagger-v1.0/resources/predicate-matrix/PredicateMatrix.v1.txt";
        String pathToCdbSynsetFile = "/Users/kyoto/Desktop/CDB/2013-SEP-17/cdb_syn.xml";
        processMatrixFileWithWordnetSynset(pathToPredicateMatrixFile);
        CdbSynsetParser cdbSynsetParser = new CdbSynsetParser();
        cdbSynsetParser.parseFile(pathToCdbSynsetFile);
        Set keySet  = cdbSynsetParser.synsetIdSynsetMap.keySet();
        Iterator keys = keySet.iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            CdbSynset cdbSynset = cdbSynsetParser.synsetIdSynsetMap.get(key);
            if (key.equals("d_n-24199")) {
                System.out.println("cdbSynset.getERelations().size() = " + cdbSynset.getERelations().size());
            }
            boolean match = adaptMapping(null, cdbSynset, false);
            if (!match) {
                ArrayList<String> parentChain = new ArrayList<String>();
                getParentChain(cdbSynset, cdbSynsetParser, parentChain);
                if (key.equals("d_n-24199")) {
                    System.out.println("parentChain.toString() = " + parentChain.toString());
                }
                for (int i = 0; i < parentChain.size(); i++) {
                    String s = parentChain.get(i);
                    //System.out.println("s = " + s);
                    CdbSynset parentSynset = cdbSynsetParser.synsetIdSynsetMap.get(s);
                    if (parentSynset!=null) {
                        match = adaptMapping(cdbSynset, parentSynset, true);
                        if (match) {
                            if (key.equals("d_n-24199")) {
                                System.out.println("parent match = " + match);
                            }
                            break;
                        }
                    }
                }
            }
            if (key.equals("d_n-24199")) {
                System.out.println("match = " + match);
            }

            if (!match) {
                if (cdbSynset.getPos().equalsIgnoreCase("verb")) {
/*
                   ArrayList<String> voidMapping = new ArrayList<String>();
                   voidMapping.add("cdb-nomatch:"+cdbSynset.getC_sy_id());
                   wordNetPredicateMap.put(cdbSynset.getC_sy_id(), voidMapping);
*/
                }
                else {
                  //  System.out.println("cdbSynset.getPos() = " + cdbSynset.getPos());
                }
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(pathToPredicateMatrixFile+".nl");
            keySet = wordNetPredicateMap.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                ArrayList<ArrayList<String>> mappings = wordNetPredicateMap.get(key);
                for (int m = 0; m < mappings.size(); m++) {
                    ArrayList<String> mapping =  mappings.get(m);
                    boolean match = false;
                    for (int i = 0; i < mapping.size(); i++) {
                        String s1 = mapping.get(i);
                        if (s1.startsWith("cdb")) {
                            /// for every cdb match we output all non-cdb mappings
                            s1+=" ";
                            fos.write(s1.getBytes());
                            for (int j = 0; j < mapping.size(); j++) {
                                String s2 = mapping.get(j)+" ";
                                if (!s2.startsWith("cdb"))  {
                                    fos.write(s2.getBytes());
                                }
                            }
                            fos.write("\n".getBytes());
                        }
                    }
                    if (match) {
                        for (int i = 0; i < mapping.size(); i++) {
                            String s = mapping.get(i)+" ";
                            fos.write(s.getBytes());
                        }
                        fos.write("\n".getBytes());
                    }
                }

            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    static public void getParentChain (CdbSynset cdbSynset, CdbSynsetParser cdbSynsetParser, ArrayList<String> parentChain) {
        for (int j = 0; j < cdbSynset.getIRelations().size(); j++) {
            CdbInternalRelation cdbInternalRelation = (CdbInternalRelation) cdbSynset.getIRelations().get(j);
            if ((cdbInternalRelation.getRelation_name().equalsIgnoreCase("HAS_HYPERONYM")) ||
                (cdbInternalRelation.getRelation_name().equalsIgnoreCase("XPOS_HAS_HYPERONYM")) ||
                (cdbInternalRelation.getRelation_name().equalsIgnoreCase("XPOS_NEAR_SYNONYM"))
               ) {
                String parentSynsetId = cdbInternalRelation.getTarget();
                if (parentChain.contains(parentSynsetId)) {
                    return;
                }
                parentChain.add(parentSynsetId);
            }
        }
        for (int j = 0; j < cdbSynset.getIRelations().size(); j++) {
            CdbInternalRelation cdbInternalRelation = (CdbInternalRelation) cdbSynset.getIRelations().get(j);
            if ((cdbInternalRelation.getRelation_name().equalsIgnoreCase("HAS_HYPERONYM")) ||
                    (cdbInternalRelation.getRelation_name().equalsIgnoreCase("XPOS_HAS_HYPERONYM")) ||
                    (cdbInternalRelation.getRelation_name().equalsIgnoreCase("XPOS_NEAR_SYNONYM"))
                    ) {
                String parentSynsetId = cdbInternalRelation.getTarget();
                if (cdbSynsetParser.synsetIdSynsetMap.containsKey(parentSynsetId)) {
                    CdbSynset parentSyncet = cdbSynsetParser.synsetIdSynsetMap.get(parentSynsetId);
                    getParentChain(parentSyncet, cdbSynsetParser, parentChain);
                }
            }
        }
    }


    public static boolean adaptMapping (CdbSynset childSynset, CdbSynset cdbSynset, boolean parent) {
        boolean DEBUG = false;
        boolean match = false;
        if (cdbSynset.getC_sy_id().equals("d_v-2505")) {
            DEBUG = true;
           // 01636397-v
        }
        for (int i = 0; i < cdbSynset.getERelations().size(); i++) {
            CdbEquivalenceRelation equivalenceRelation = (CdbEquivalenceRelation) cdbSynset.getERelations().get(i);
            String target = equivalenceRelation.getTarget30();
            if (DEBUG) System.out.println("target = " + target);
            // mcr:ili-30-00619869-v  -> eng-30-00619869-v
            //target30="ENG30-04047401-n"
            if (target.length()>3) {
                target = "eng-"+ target.substring(3);
            }
            else {
                // System.out.println("target = " + target);
            }
            if (wordNetPredicateMap.containsKey(target)) {
                ArrayList<ArrayList<String>> mappings = wordNetPredicateMap.get(target);
                for (int j = 0; j < mappings.size(); j++) {
                    ArrayList<String> mapping =  mappings.get(j);
                    String s =  "cdb";
                    if (cdbSynset.getPos().toLowerCase().startsWith("n")) {
                        s+="-xpos";
                        if (parent) s+="-parent";
                        s +=":"+cdbSynset.getC_sy_id();
                        s+="-n";
                    }
                    if (cdbSynset.getPos().toLowerCase().startsWith("v")) {
                        if (parent) s+="-parent";
                        s +=":"+cdbSynset.getC_sy_id();
                        s+="-v";
                    }
                    if (cdbSynset.getPos().toLowerCase().startsWith("a")) {
                        s+="-xpos";
                        if (parent) s+="-parent";
                        s +=":"+cdbSynset.getC_sy_id();
                        s+="-a";
                    }
                    if (!mapping.contains(s)){
                        mapping.add(s);
                    }
                    if (childSynset!=null) {
                        //// we also add the child to the same mapping
                        s =  "cdb";
                        if (childSynset.getPos().toLowerCase().startsWith("n")) {
                            s+="-xpos";
                            if (parent) s+="-child";
                            s +=":"+childSynset.getC_sy_id();
                            s+="-n";
                        }
                        if (childSynset.getPos().toLowerCase().startsWith("v")) {
                            if (parent) s+="-parent";
                            s +=":"+childSynset.getC_sy_id();
                            s+="-v";
                        }
                        if (childSynset.getPos().toLowerCase().startsWith("a")) {
                            s+="-xpos";
                            if (parent) s+="-parent";
                            s +=":"+childSynset.getC_sy_id();
                            s+="-a";
                        }
                        if (!mapping.contains(s)){
                            mapping.add(s);
                        }

                    }
                    match = true;
                    wordNetPredicateMap.put(target, mappings);

                }
            }
            else {
                if (DEBUG) System.out.println("cannot find it in matrix");
            }
        }
        return match;
    }

    public static void processMatrixFileWithWordnetSynset(String file) {
        try {
           /*
           VN_CLASS VN_CLASS_NUMBER VN_SUBCLASS VN_SUBCLASS_NUMBER VN_LEMA WN_SENSE VN_THEMROLE FN_FRAME FN_LEXENT FN_ROLE PB_ROLESET PB_ARG MCR_ILIOFFSET MCR_DOMAIN MCR_SUMO MC_LEXNAME
vn:comprehend-87.2 vn:87.2 vn:null vn:null vn:misconstrue wn:misconstrue%2:31:01 vn:Experiencer fn:NULL fn:NULL fn:NULL pb:misconstrue.01 pb:0 mcr:ili-30-00619869-v mcr:factotum mcr:Communication mcr:cognition
vn:comprehend-87.2 vn:87.2 vn:null vn:null vn:misconstrue wn:misconstrue%2:31:01 vn:Attribute fn:NULL fn:NULL fn:NULL pb:misconstrue.01 pb:1 mcr:ili-30-00619869-v mcr:factotum mcr:Communication mcr:cognition
vn:comprehend-87.2 vn:87.2 vn:null vn:null vn:misconstrue wn:misconstrue%2:31:01 vn:Stimulus fn:NULL fn:NULL fn:NULL pb:misconstrue.01 pb:NULL mcr:ili-30-00619869-v mcr:factotum mcr:Communication mcr:cognition
vn:comprehend-87.2 vn:87.2 vn:null vn:null vn:misinterpret wn:misinterpret%2:31:02 vn:Experiencer fn:NULL fn:NULL fn:NULL pb:misinterpret.01 pb:0 mcr:ili-30-00619869-v mcr:factotum mcr:Communication mcr:cognition
vn:comprehend-87.2 vn:87.2 vn:null vn:null vn:misinterpret wn:misinterpret%2:31:02 vn:Attribute fn:NULL fn:NULL fn:NULL pb:misinterpret.01 pb:2 mcr:ili-30-00619869-v mcr:factotum mcr:Communication mcr:cognition

            */
            String [] headers = null;
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine = "";
            String synset = "";
            String senseKey ="";
            String lemma = "";
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                if (inputLine.trim().length()>0) {
/*                    if (inputLine.indexOf("wn:die%2:30:00")==-1) {
                        continue;
                    }*/
                    String[] fields = inputLine.split(" ");
                    //System.out.println("fields = " + fields);
                    synset = "";
                    lemma= "";
                    if (fields.length>=15) {

                        if (!fields[12].startsWith("mcr:")) {
                            // System.out.println("Error processing inputLine = " + inputLine);
                            continue;
                        }
                        //// takes wn sense key as the key
                        synset = "eng"+fields[12].substring(7);  //mcr:ili-30-00619869-v
                        senseKey = fields[5]; /// wn:misconstrue%2:31:01
                        //System.out.println("senseKey = " + senseKey);
                        lemma = senseKey.substring(3);
                        int idx = lemma.indexOf("%");
                        if (idx!=-1) {
                            lemma = lemma.substring(0, idx);
                        }
                        if (lemma.isEmpty()) {
                            continue;
                        }
                        if (wordNetLemmaSenseMap.containsKey(lemma)) {
                            ArrayList<String> synsets = wordNetLemmaSenseMap.get(lemma);
                            if (!synsets.contains(synset)) {
                                synsets.add(synset);
                                wordNetLemmaSenseMap.put(lemma, synsets);
                            }
                        }
                        else {
                            ArrayList<String> synsets =new ArrayList<String>();
                            synsets.add(synset);
                            wordNetLemmaSenseMap.put(lemma, synsets);
                        }
                        ArrayList<String> sourceFields = new ArrayList<String>();
                        for (int i = 0; i < fields.length; i++) {
                            String field = fields[i];
                            if (field.toLowerCase().indexOf("null")==-1) {
                                sourceFields.add(field);
                            }
                        }
                        if (sourceFields.size()>0) {
                            if (wordNetPredicateMap.containsKey(synset)) {
                                ArrayList<ArrayList<String>> targets = wordNetPredicateMap.get(synset);

                                if (!hasSourceField(targets, sourceFields)) {
                                    targets.add(sourceFields);
                                    wordNetPredicateMap.put(synset, targets);

                                }

                            }
                            else {
                                ArrayList<ArrayList<String>> targets = new ArrayList<ArrayList<String>>();
                                targets.add(sourceFields);
                                wordNetPredicateMap.put(synset, targets);
                            }
                        }
                    }
                    else {/*
                        System.out.println("Error in inputLine = " + inputLine);
                        System.out.println("fields.length = " + fields.length);
                        for (int i = 0; i < fields.length; i++) {
                            String field = fields[i];
                            System.out.println("field = " + field);
                        }*/
                    }
                }
            }
/*            Set keySet = wordNetLemmaSenseMap.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String str = key+"#";
                ArrayList<String> sense = wordNetLemmaSenseMap.get(key);
                for (int i = 0; i < sense.size(); i++) {
                    String s = sense.get(i);
                    str+=s+";";
                }
                System.out.println(str);

            }*/
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    static boolean hasSourceField (ArrayList<ArrayList<String>> targets, ArrayList<String> sourceField) {
        for (int i = 0; i < targets.size(); i++) {
            ArrayList<String> strings = targets.get(i);
            if (strings.containsAll(sourceField)) return true;
        }
        return false;
    }

}
