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
 * Created with IntelliJ IDEA.
 * User: kyoto
 * Date: 4/25/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckNotFoundLUsInSynsets {

    static public void main (String [] args) {
        try {
            String pathToCdbSynsetFile = args[0];
            CdbSynsetParser parser = new CdbSynsetParser();
            FileOutputStream fos = new FileOutputStream(pathToCdbSynsetFile+".lu-errors");
            parser.parseFile(pathToCdbSynsetFile);
            Set keySet = parser.synsetIdSynsetMap.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
                ArrayList<String> lus = new ArrayList<String>();
                for (int i = 0; i < synset.getSynonyms().size(); i++) {
                    Syn syn=  (Syn) synset.getSynonyms().get(i);
                    if (syn.getPreviewtext().equals("not_found:0")) {
                        String str = "Not found LU:"+synset.getC_sy_id()+":"+synset.toFlatSynonymString()+"\n";
                        fos.write(str.getBytes());
                    }
                    else if (lus.contains(syn.getPreviewtext())) {
                        String str = "Double LU:"+synset.getC_sy_id()+":"+synset.toFlatSynonymString()+"\n";
                        fos.write(str.getBytes());
                    }
                    else {
                        lus.add(syn.getPreviewtext());
                    }
                }
                for (int i = 0; i < synset.getIRelations().size(); i++) {
                    CdbInternalRelation rel =  (CdbInternalRelation) synset.getIRelations().get(i);
                    if (rel.getPreviewtext().equals("not_found:0")) {
                        String str = "Not found target LU:"+synset.getC_sy_id()+":"+synset.toFlatSynonymString()+":"+rel.getRelation_name()+":"+rel.getTarget()+"\n";
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
