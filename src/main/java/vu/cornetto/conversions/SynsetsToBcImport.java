package vu.cornetto.conversions;

import vu.cornetto.cdb.CdbInternalRelation;
import vu.cornetto.cdb.CdbSynset;
import vu.cornetto.cdb.CdbSynsetParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 28-okt-2008
 * Time: 17:04:47
 * To change this template use File | Settings | File Templates.
 */
public class SynsetsToBcImport {

    static public String getPosFromLuId(CdbSynset synset) {
        String POS = "-n";
        for (int i = 0; i < synset.getSynonyms().size(); i++) {
            vu.cornetto.cdb.Syn syn =  (vu.cornetto.cdb.Syn) synset.getSynonyms().get(i);
            String id = syn.getC_lu_id();
            if ((!id.isEmpty()) && (!id.startsWith("c_"))) {
                String clue = id.substring(2,3);
                if (clue.equals("v")) {
                    POS = "-v";
                    break;
                }
                else if (clue.equals("a")) {
                    POS = "-a";
                    break;

                }
                else if (clue.equals("r")) {
                    POS = "-b";
                    break;
                }
            }
            else if (id.startsWith("c_")) {
                if (syn.getPreviewtext().indexOf("en:")>-1) {
                    POS = "-v";
                    break;
                }
            }

        }
        return POS;
    }

    static public String getPosFromSynsetId(String id, String previewText) {
        String POS = "-n";
        if ((!id.isEmpty()) && (!id.startsWith("c_"))) {
            String clue = id.substring(2,3);
            if (clue.equals("v")) {
                POS = "-v";
            }
            else if (clue.equals("a")) {
                POS = "-a";
            }
            else if (clue.equals("r")) {
                POS = "-b";
            }
        }
        else if (id.startsWith("c_")) {
            if (previewText.indexOf("en:")>-1) {
                POS = "-v";
            }
        }
        return POS;
    }

    static public void main (String [] args) {
        HashMap domainMap = new HashMap();
        File cdbSynFilePath = new File(args[0]);
        System.out.println("cdbSynFilePath.getAbsolutePath() = " + cdbSynFilePath.getAbsolutePath());
        String outputFilePath = cdbSynFilePath.getAbsolutePath()+".bc.chain";
        String outputFilePath2 = cdbSynFilePath.getAbsolutePath()+".bc.stat";
        CdbSynsetParser parser = new CdbSynsetParser();
        parser.parseFile(cdbSynFilePath);
        try {
            String str = "";
            FileOutputStream out = new FileOutputStream (outputFilePath);
            FileOutputStream out2 = new FileOutputStream (outputFilePath2);
            Set keySet = parser.synsetIdSynsetMap.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
                String c_sy_id = synset.getC_sy_id();
                String POS = "-n";
                if (synset.getPos().toLowerCase().equals("noun")) {
                   //  c_sy_id += "-n";
                    POS = "-n";
                }
                else if (synset.getPos().toLowerCase().equals("verb")) {
                    POS = "-v";
                 }
                else if (synset.getPos().toLowerCase().equals("adjective")) {
                    POS = "-a";
                 }
                else if (synset.getPos().toLowerCase().equals("adj")) {
                    POS = "-a";
                 }
                else if (synset.getPos().toLowerCase().equals("adverb")) {
                    POS = "-b";
                 }
                else if (synset.getPos().toLowerCase().equals("adv")) {
                    POS = "-b";
                 }
                else {
                    POS = getPosFromLuId(synset);
                }
                c_sy_id += POS;
                ArrayList relations = synset.getIRelations();
                str = c_sy_id + " " + relations.size()+"\n";
                out2.write(str.getBytes());

                for (int i=0; i<relations.size();i++) {
                      CdbInternalRelation rel = (CdbInternalRelation) relations.get(i);
                      if (rel.getRelation_name().equalsIgnoreCase("has_hyperonym")) {
                          String c_sy_idTarget = rel.getTarget();
                          String previewText = rel.getPreviewtext();
                          String TARGETPOS =  getPosFromSynsetId(c_sy_idTarget, previewText);
                          c_sy_idTarget += TARGETPOS;
                          str = c_sy_id + " @ " + c_sy_idTarget+"\n";
                          out.write(str.getBytes());
                      }
                }
            }
            out.close();
            out2.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
