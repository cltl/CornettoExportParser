package vu.cornetto.conversions;

import vu.cornetto.cdb.CdbInternalRelation;
import vu.cornetto.cdb.CdbSynset;
import vu.cornetto.cdb.CdbSynsetParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 22-aug-2008
 * Time: 7:50:42
 * To change this template use File | Settings | File Templates.
 */
public class SynsetsToWsdGraphImport {

    public static void main (String args[]) {
        String cdbSynFilePath = args[0];
        String nameKey = args[1];
        String outputFile = nameKey+"_graph.txt";
        String outputFilePath = new File(cdbSynFilePath).getParent()+"/"+outputFile;
        CdbSynsetParser parser = new CdbSynsetParser();
        parser.parseFile(cdbSynFilePath);
        //parser.parseFileAsBytes(cdbSynFilePath);
        try {
            String str = "";
            FileOutputStream out = new FileOutputStream (outputFilePath);
/*
            String fixed = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<cids>\n";
            out.write(fixed.getBytes());
*/
            Set keySet = parser.synsetIdSynsetMap.keySet();
            Iterator keys = keySet.iterator();
            int counter = 0;
            while (keys.hasNext()) {
                counter++;
                String key = (String) keys.next();
                CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
                String c_sy_id = "u:"+synset.getC_sy_id();
                if (synset.getPos().toLowerCase().equals("noun")) {
                     c_sy_id += "-n";
                }
                else if (synset.getPos().toLowerCase().equals("verb")) {
                     c_sy_id += "-v";
                 }
                else if (synset.getPos().toLowerCase().equals("adjective")) {
                     c_sy_id += "-a";
                 }
                else if (synset.getPos().toLowerCase().equals("adj")) {
                     c_sy_id += "-a";
                 }
                else if (synset.getPos().toLowerCase().equals("adverb")) {
                     c_sy_id += "-b";
                 }
                else if (synset.getPos().toLowerCase().equals("adv")) {
                     c_sy_id += "-b";
                 }
                else if (synset.getPos().length()==0) {
                     c_sy_id += "-n";
                 }
                ArrayList relations = synset.getIRelations();
                for (int i=0; i<relations.size();i++) {
                      CdbInternalRelation rel = (CdbInternalRelation) relations.get(i);
                      if (rel.getTarget().length()>0) {
                          String targetId = " v:"+rel.getTarget();
                          if (targetId.indexOf("_n-")!=-1) {
                              targetId += "-n";
                          }
                          else if (targetId.indexOf("_v-")!=-1) {
                              targetId += "-v";
                          }
                          else if (targetId.indexOf("_a-")!=-1) {
                              targetId += "-a";
                          }
                          else if (targetId.indexOf("_b-")!=-1) {
                              targetId += "-b";
                          }
                          else if (targetId.indexOf("_r-")!=-1) {
                              targetId += "-b";
                          }
                          else if (targetId.startsWith(" v:c_")) {
                              targetId += "-n";
                          }
                          str = c_sy_id + targetId+"\n";
                          out.write(str.getBytes());
                      }
                }
            }
            //fixed = "</cids>\n";
            //out.write(fixed.getBytes("UTF-8"));
            //out.write(fixed.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}