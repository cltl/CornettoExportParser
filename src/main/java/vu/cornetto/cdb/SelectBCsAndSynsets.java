package vu.cornetto.cdb;

import vu.cornetto.pwn.BaseConceptReader;

import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.util.HashMap;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 12-feb-2009
 * Time: 12:41:55
 * To change this template use File | Settings | File Templates.
 */
public class SelectBCsAndSynsets {
    static HashMap map = new HashMap();

    static public void main (String [] args) {
        String cdbFile = args[0];
        String baseFile = args[1];
        String ext = args[2];
        String select = args[3];
        map = new HashMap();
        if (select.equalsIgnoreCase("synset")) {
            selectFromSynsetId(cdbFile, baseFile, ext);
        }
        if (select.equalsIgnoreCase("equi3")) {
            selectFromEquivalenceWn3(cdbFile, baseFile, ext);
        }
    }

    static public void  selectFromSynsetId (String cdbFile, String baseFile, String ext) {
        CdbSynsetParser parser = new CdbSynsetParser();
        ArrayList synsets = BaseConceptReader.readFile(baseFile);
        int n = 0;
        try {
            FileOutputStream fos = new FileOutputStream (cdbFile+ext);
            String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<cdbsyn.dbxml>\n";
            fos.write(str.getBytes());
            parser.parseFile(cdbFile);
            Set keySet = parser.synsetIdSynsetMap.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
                if (synsets.contains(synset.getC_sy_id())) {
                    str = synset.toString();
                    fos.write(str.getBytes());
                }
            }
            str = "</cdbsyn.dbxml>\n";
            fos.write(str.getBytes());
            fos.close();
            System.out.println("n = " + n);
        } catch (IOException e) {
            e.printStackTrace();  //To select body of catch statement use File | Settings | File Templates.
        }
    }

    static public void  selectFromEquivalenceWn3 (String cdbFile, String baseFile, String ext) {
        CdbSynsetParser parser = new CdbSynsetParser();
        ArrayList bcs = BaseConceptReader.readFile(baseFile);
        boolean select = false;
        int n = 0;
        try {
            FileOutputStream fos = new FileOutputStream (cdbFile+ext);
            String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<cdbsyn.dbxml>\n";
            fos.write(str.getBytes());
            parser.parseFile(cdbFile);
            Set keySet = parser.synsetIdSynsetMap.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                select = false;
                String key = (String) keys.next();
                CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
                ArrayList erels = (ArrayList) synset.getERelations();
                for (int i = 0; i < erels.size(); i++) {
                    CdbEquivalenceRelation erel = (CdbEquivalenceRelation) erels.get(i);
                    String target = erel.getTarget30();
                    String target20 = target+"#"+erel.getRelationName()+"#"+erel.getTarget20()+"#"+erel.getTarget20Previewtext();
                    if (bcs.contains(target)) {
                        if (map.containsKey(target20)) {
                            ArrayList syns = (ArrayList) map.get(target20);
                            syns.add(synset);
                            map.put(target20, syns);
                        }
                        else {
                            ArrayList syns = new ArrayList();
                            syns.add(synset);
                            map.put(target20, syns);
                        }
                        n++;
                        //break;
                    }
                }
            }
            keySet = map.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                ArrayList syns = (ArrayList) map.get(key);
                str = key+"#";
                for (int i = 0; i < syns.size(); i++) {
                    CdbSynset cdbSynset = (CdbSynset) syns.get(i);
                    str += cdbSynset.getC_sy_id()+cdbSynset.toFlatSynonymString()+";";
                }
                str += "\n";
                fos.write(str.getBytes());
            }
            fos.close();
            System.out.println("n = " + n);
        } catch (IOException e) {
            e.printStackTrace();  //To select body of catch statement use File | Settings | File Templates.
        }
    }

}