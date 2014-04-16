package vu.cornetto.check;

import vu.cornetto.cdb.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: kyoto
 * Date: 5/8/12
 * Time: 12:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckLuSynsetMapppings {



    static public void main (String[] args)  {
        try {
            String pathToCdbSynsetFile = args[0];
            String pathToCdbLuFile = args[1];
            //String pathToCdbSynsetFile = "";
            //String pathToCdbLuFile = "";
            CdbSynsetParser parser = new CdbSynsetParser();
            CdbLuDomParser luDomParser = new CdbLuDomParser();
            FileOutputStream fos = new FileOutputStream(pathToCdbSynsetFile+".lu-synset-mismatches");
            parser.parseFile(pathToCdbSynsetFile);
            luDomParser.readLuFile(pathToCdbLuFile);
            Set keySet = luDomParser.lemmaLuMap.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                ArrayList<String> luIds = luDomParser.lemmaLuMap.get(key);
                ArrayList<String> synsets = parser.lemmaMapSynsetIdList.get(key);
                if (synsets==null) {
                    String str = "No synsets for key: "+key+" but senses in RBN:"+ luIds.size()+"\n";
                    fos.write(str.getBytes());
                }
                if (synsets.size()!= luIds.size()) {
                    String str = "Inequal senses and synsets for key: "+key+"; Synsets:"+synsets.size()+" Senses:"+ luIds.size()+"\n";
                    fos.write(str.getBytes());
                }
            }

            keySet = parser.synsetIdSynsetMap.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                ArrayList<String> luIds = luDomParser.lemmaLuMap.get(key);
                if (luIds==null) {
                    String str = "Could not find the LUs in cdblu for the form: \""+key+"\""+"\n";
                    fos.write(str.getBytes());
                }
                else {
                    CdbSynset synset = parser.synsetIdSynsetMap.get(key);
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
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
