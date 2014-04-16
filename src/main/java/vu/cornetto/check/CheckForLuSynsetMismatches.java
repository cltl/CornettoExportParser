package vu.cornetto.check;

import vu.cornetto.cdb.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: kyoto
 * Date: 6/30/12
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckForLuSynsetMismatches {



    static public void main (String[] args) {
        try {
            //String pathToCdbSynFile = args[0];
            //String pathToCdbLuFile = args[0];
            String pathToCdbSynFile = "/Users/piek/Desktop/CDB/2014-MAR-28/cdb_syn.xml";
            String pathToCdbLuFile = "/Users/piek/Desktop/CDB/2014-MAR-28/cdb_lu.xml";
            String pathToCdbSynFileCheckFile = pathToCdbSynFile+".lu-synset-mismatch"+".txt";
            FileOutputStream fos = new FileOutputStream(pathToCdbSynFileCheckFile);
            CdbSynsetParser parser = new CdbSynsetParser();
            parser.parseFile(pathToCdbSynFile);
            CdbLuMapFastDomParser cdbLuMapFastDomParser = new CdbLuMapFastDomParser();
            cdbLuMapFastDomParser.readLuFile(pathToCdbLuFile);
            String str ="Lemma\tPos\tDiff\tNr. lu senses\tLu senses\tNr synset senses\tSynset senses\tMismatch senses\n";
            fos.write(str.getBytes());
            Set keysSet = parser.lemmaMapSynsetIdList.keySet();
            Iterator keys = keysSet.iterator();
            while (keys.hasNext()) {
                String lemma = (String) keys.next();
                //System.out.println("lemma = " + lemma);
                ArrayList<String> pos  = new ArrayList<String>();
                ArrayList<String> misMatchSenses = new ArrayList<String>();
                ArrayList<String> synsetSenses = new ArrayList<String>();
                ArrayList<String> luSenses = new ArrayList<String>();
                ArrayList<String> synsetIds = parser.lemmaMapSynsetIdList.get(lemma);
                for (int i = 0; i < synsetIds.size(); i++) {
                    String s = synsetIds.get(i);
                    CdbSynset cdbSynset = parser.synsetIdSynsetMap.get(s);
                    for (int j = 0; j < cdbSynset.getSynonyms().size(); j++) {
                        Syn syn = (Syn) cdbSynset.getSynonyms().get(j);
                        if (syn.getForm().equals(lemma)) {
                            synsetSenses.add(syn.getSeqNr()+":"+syn.getC_lu_id());
                        }
                    }
                   // System.out.println("synset = " + s);
                }
                if (cdbLuMapFastDomParser.lemmaMapCdbLuFormListMap.containsKey(lemma)) {
                   ArrayList<CdbLuForm> luForms = cdbLuMapFastDomParser.lemmaMapCdbLuFormListMap.get(lemma);
                    for (int i = 0; i < luForms.size(); i++) {
                        CdbLuForm cdbLuForm = luForms.get(i);
                        luSenses.add(cdbLuForm.getSeqNr()+":"+cdbLuForm.getLuId());
                        if (!pos.contains(cdbLuForm.getFormCat())) {
                            pos.add(cdbLuForm.getFormCat());
                        }
                        //System.out.println("cdbLuForm = " + cdbLuForm.getSeqNr());
                    }
                }

                boolean mismatch = false;
                for (int i = 0; i < synsetSenses.size(); i++) {
                    String s = synsetSenses.get(i);
                    if (!luSenses.contains(s)) {
                        mismatch = true;
                        misMatchSenses.add(s);
                    }
                }

                for (int i = 0; i < luSenses.size(); i++) {
                    String s = luSenses.get(i);
                    if (!synsetSenses.contains(s)) {
                        mismatch = true;
                        misMatchSenses.add(s);
                    }
                }

                if (mismatch) {
                    int diff = Math.abs(luSenses.size()-synsetSenses.size());
                    str = lemma+"\t";
                    for (int i = 0; i < pos.size(); i++) {
                        String s = pos.get(i);
                        str += s+",";
                    }
                    str += "\t"+diff+"\t"+luSenses.size()+"\t";
                    TreeSet sorter = new TreeSet();
                    for (int i = 0; i < luSenses.size(); i++) {
                        String s = luSenses.get(i);
                        sorter.add(s);
                    }
                    Iterator it = sorter.iterator();
                    while (it.hasNext()) {
                        String s = (String) it.next();
                        str += s+",";
                    }
                    str += "\t"+synsetSenses.size()+"\t";
                    sorter = new TreeSet();
                    for (int i = 0; i < synsetSenses.size(); i++) {
                        String s = synsetSenses.get(i);
                        sorter.add(s);
                    }
                    it = sorter.iterator();
                    while (it.hasNext()) {
                        String s = (String) it.next();
                        str += s+",";
                    }

                    str +="\t";
                    sorter = new TreeSet();
                    for (int i = 0; i < misMatchSenses.size(); i++) {
                        String s = misMatchSenses.get(i);
                        sorter.add(s);
                    }
                    it = sorter.iterator();
                    while (it.hasNext()) {
                        String s = (String) it.next();
                        str += s+",";
                    }

                    str += "\n";
                    fos.write(str.getBytes());
                }
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
