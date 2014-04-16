package vu.cornetto.check;

import vu.cornetto.cdb.CdbInternalRelation;
import vu.cornetto.cdb.CdbSynset;
import vu.cornetto.cdb.CdbSynsetParser;
import vu.cornetto.cdb.Syn;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kyoto
 * Date: Nov 8, 2010
 * Time: 12:43:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckForMissingTargets {

    static public void main (String[] args) {
        try {
            String cdbSynFilePath = args[0];
            CdbSynsetParser parser = new CdbSynsetParser();
            parser.parseFile(cdbSynFilePath);
            Set keySet = parser.synsetIdSynsetMap.keySet();
            Iterator keys = keySet.iterator();
            FileOutputStream fos = new FileOutputStream(cdbSynFilePath+".missingtargets.txt");
            while (keys.hasNext()) {
                String key = (String) keys.next();
                CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
                ArrayList<CdbInternalRelation> relations = synset.getIRelations();
                for (int i=0; i<relations.size();i++) {
                      CdbInternalRelation rel = relations.get(i);
                      if (!rel.getRelation_name().equals("HAS_HYPONYM")) {
                          if (rel.getTarget().length()>0) {
                              CdbSynset targetsynset = (CdbSynset) parser.synsetIdSynsetMap.get(rel.getTarget());
                              if (targetsynset==null) {
                                  String str = rel.getTarget()+"#"+rel.getRelation_name()+"#";
                                  for (int j = 0; j < synset.getSynonyms().size(); j++) {
                                      Syn syn = (Syn) synset.getSynonyms().get(j);
                                      str += syn.getPreviewtext()+";";
                                  }
                                  str += "\n";
                                  fos.write(str.getBytes());
                              }
                          }
                      }
                }
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
