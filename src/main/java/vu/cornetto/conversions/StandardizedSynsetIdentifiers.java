package vu.cornetto.conversions;

import vu.cornetto.cdb.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: kyoto
 * Date: 5/18/13
 * Time: 11:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class StandardizedSynsetIdentifiers {

    static String getPosFromLu (String luId, CdbLuMapFastDomParser cdbLuMapFastDomParser) {
        String pos = "";
        if (cdbLuMapFastDomParser.luIdMapCdbLuFormList.containsKey(luId)) {
            ArrayList<CdbLuForm> lus = cdbLuMapFastDomParser.luIdMapCdbLuFormList.get(luId);
            for (int j = 0; j < lus.size(); j++) {
                CdbLuForm cdbLuForm = lus.get(j);
                pos = cdbLuForm.getFormCat();
                break;
            }
        }
        else {
            System.out.println("CANNOT FIND LU");
            System.out.println("luId = " + luId);
        }
        if (pos.equalsIgnoreCase("noun")) {
            pos = "n";
        }
        else if (pos.equalsIgnoreCase("verb")) {
            pos = "v";
        }
        else if (pos.equalsIgnoreCase("adjective")) {
            pos = "a";
        }
        else if (pos.equalsIgnoreCase("adj")) {
            pos = "a";
        }
        else if (pos.equalsIgnoreCase("adverb")) {
            pos = "r";
        }
        else {
            pos = "o";
        }
        return pos;
    }
    static public void main (String[] args) {
        String pathToLuFile = args[0];
        String pathToSynsetFile = args[1];
        String prefix = args[2];
        HashMap<String, String> mappings = new HashMap<String, String>();
        CdbSynsetParser cdbSynsetParser = new CdbSynsetParser();
        CdbLuMapFastDomParser cdbLuMapFastDomParser = new CdbLuMapFastDomParser();
        cdbLuMapFastDomParser.readLuFile(pathToLuFile);
        cdbSynsetParser.parseFile(pathToSynsetFile);
        Set keySet = cdbSynsetParser.synsetIdSynsetMap.keySet();
        Iterator keys = keySet.iterator();
        while (keys.hasNext()) {
            String synsetId = (String) keys.next();
            String pos = "";
            CdbSynset cdbSynset = cdbSynsetParser.synsetIdSynsetMap.get(synsetId);
            for (int i = 0; i < cdbSynset.getSynonyms().size(); i++) {
                Syn syn = (Syn) cdbSynset.getSynonyms().get(i);
                String luId = syn.getC_lu_id();
                pos = getPosFromLu(luId, cdbLuMapFastDomParser);
                if (!pos.isEmpty()) {
                    break;
                }
            }
            if (!pos.isEmpty()) {
                mappings.put(synsetId, prefix+synsetId+"-"+pos);
                //System.out.println("nld-20-"+synsetId+"-"+pos);
            }
            else {
                System.out.println("COULD NOT FIND A POS");
                System.out.println("synsetId = " + synsetId);
                System.out.println("cdbSynset = " + cdbSynset.toString());
            }
        }
        System.out.println("mappings = " + mappings.size());
        try {
            FileOutputStream fos = new FileOutputStream(pathToSynsetFile+"newids.xml");
            String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<cdbsyn>\n";
            fos.write(str.getBytes());
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String synsetId = (String) keys.next();
                if (mappings.containsKey(synsetId)) {
                    String newID = mappings.get(synsetId);
                    CdbSynset cdbSynset = cdbSynsetParser.synsetIdSynsetMap.get(synsetId);
                    cdbSynset.setC_sy_id(newID);
                    for (int i = 0; i < cdbSynset.getIRelations().size(); i++) {
                        CdbInternalRelation rel = (CdbInternalRelation) cdbSynset.getIRelations().get(i);
                        String relTargetId= rel.getTarget();
                        if (mappings.containsKey(relTargetId)) {
                            String newTargetId = mappings.get(relTargetId);
                            rel.setTarget(newTargetId);
                        }
                        else {
                            System.out.println("CANNOT FIND TARGET ID");
                            System.out.println("synsetId = " + synsetId);
                            System.out.println("relTargetId = " + relTargetId);
                            String pos = "n";
                            if (synsetId.startsWith("d_v")) {
                                pos = "v";
                            }
                            else if (synsetId.startsWith("n_v")) {
                                pos = "v";
                            }
                            else if (synsetId.startsWith("d_r")) {
                                pos = "r";
                            }
                            else if (synsetId.startsWith("n_r")) {
                                pos = "r";
                            }
                            else if (synsetId.startsWith("d_a")) {
                                pos = "a";
                            }
                            else if (synsetId.startsWith("d_a")) {
                                pos = "a";
                            }
                            String newTargetId =  prefix+synsetId+"-"+ pos;
                            System.out.println("new target id = " + newTargetId);
                            rel.setTarget(newTargetId);
                        }
                    }
                    for (int i = 0; i < cdbSynset.getERelations().size(); i++) {
                        CdbEquivalenceRelation cdbEquivalenceRelation = (CdbEquivalenceRelation) cdbSynset.getERelations().get(i);
                        String target = cdbEquivalenceRelation.getTarget15();
                        if (target.startsWith("ENG15")) {
                            target = "eng-15"+target.substring(5);
                            cdbEquivalenceRelation.setTarget15(target);
                        }
                        else if (target.startsWith("ENG16")) {
                            target = "eng-16"+target.substring(5);
                            cdbEquivalenceRelation.setTarget15(target);
                        }
                        target = cdbEquivalenceRelation.getTarget16();
                        if (target.startsWith("ENG16")) {
                            target = "eng-16"+target.substring(5);
                            cdbEquivalenceRelation.setTarget16(target);
                        }
                        target = cdbEquivalenceRelation.getTarget20();
                        if (target.startsWith("ENG20")) {
                            target = "eng-20"+target.substring(5);
                            cdbEquivalenceRelation.setTarget20(target);
                        }
                        target = cdbEquivalenceRelation.getTarget30();
                        if (target.startsWith("ENG30")) {
                            target = "eng-30"+target.substring(5);
                            cdbEquivalenceRelation.setTarget30(target);
                        }
                    }
                    str =  cdbSynset.toStringNoRedundant();
                    fos.write(str.getBytes());
                }
                else {
                    System.out.println("LOST MAPPING");
                    System.out.println("synsetId = " + synsetId);
                }

            }
            str = "</cdbsyn>\n";
            fos.write(str.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
