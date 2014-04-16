package vu.cornetto.check;

import vu.cornetto.cdb.CdbInternalRelation;
import vu.cornetto.cdb.CdbSynset;
import vu.cornetto.cdb.CdbSynsetParser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: kyoto
 * Date: 4/25/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckSelfReferences {

    static public void main (String [] args) {
        try {
            String pathToCdbSynsetFile = args[0];
            CdbSynsetParser parser = new CdbSynsetParser();
            FileOutputStream fos = new FileOutputStream(pathToCdbSynsetFile+".circular");
            parser.parseFile(pathToCdbSynsetFile);
            Set keySet = parser.synsetIdSynsetMap.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
                for (int i = 0; i < synset.getIRelations().size(); i++) {
                    CdbInternalRelation rel =  (CdbInternalRelation) synset.getIRelations().get(i);
                    if (rel.getTarget().equals(synset.getC_sy_id())) {
                        String str = synset.getC_sy_id()+":"+synset.toFlatSynonymString()+":"+rel.getRelation_name()+":"+rel.getTarget()+"\n";
                        str += "";
                        fos.write(str.getBytes());
                    }
                }
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
