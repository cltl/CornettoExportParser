package vu.cornetto.check;

import vu.cornetto.cdb.CdbInternalRelation;
import vu.cornetto.cdb.CdbSynset;
import vu.cornetto.cdb.CdbSynsetParser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: kyoto
 * Date: 6/30/12
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckForLUsInMultipleSynsets {



    static public void main (String[] args) {
        try {
            String pathToCdbSynFile = args[0];
            String pathToCdbSynFileCheckFile = args[0]+".duplicateSynsets"+".txt";
            FileOutputStream fos = new FileOutputStream(pathToCdbSynFileCheckFile);
            CdbSynsetParser parser = new CdbSynsetParser();
            parser.parseFile(pathToCdbSynFile);
            String str ="";
            if (parser.luIdMapSynsetList.size()>0) {
                str = "parser.luIdMapSynsetList.size() = " + parser.luIdMapSynsetList.size()+"\n";
                fos.write(str.getBytes());
                Set keysSet = parser.luIdMapSynsetList.keySet();
                Iterator keys = keysSet.iterator();
                while (keys.hasNext()) {
                    String luId = (String) keys.next();
                    ArrayList<CdbSynset> synsets = parser.luIdMapSynsetList.get(luId);
                    str = luId+":"+synsets.size()+"\n";
                    fos.write(str.getBytes());
                    for (int i = 0; i < synsets.size(); i++) {
                        CdbSynset cdbSynset = synsets.get(i);
                        str = "\t"+cdbSynset.getC_sy_id()+":"+cdbSynset.toFlatSynonymString()+": HYPERNYM:";
                        for (int j = 0; j < cdbSynset.getIRelations().size(); j++) {
                            CdbInternalRelation relation  = (CdbInternalRelation) cdbSynset.getIRelations().get(j);
                            if (relation.getRelation_name().equalsIgnoreCase("HAS_HYPERONYM")) {
                                str+= ""+relation.getPreviewtext();
                            }
                        }
                        str += "\n";
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
