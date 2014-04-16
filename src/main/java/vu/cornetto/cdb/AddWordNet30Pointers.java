package vu.cornetto.cdb;

import vu.cornetto.pwn.PwnVersionParser;
import vu.cornetto.pwn.PwnVersionMapping;

import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 12-feb-2009
 * Time: 12:04:53
 * To change this template use File | Settings | File Templates.
 */
public class AddWordNet30Pointers {


    static public void main (String [] args) {
        String nmap = "D:\\Resources\\Wordnet mappings\\mapping-20-30\\wn20-30.noun";
        String vmap = "D:\\Resources\\Wordnet mappings\\mapping-20-30\\wn20-30.verb";
        String amap = "D:\\Resources\\Wordnet mappings\\mapping-20-30\\wn20-30.adj";
        String bmap = "D:\\Resources\\Wordnet mappings\\mapping-20-30\\wn20-30.adv";
        PwnVersionParser pwnVersionParser = new PwnVersionParser("ENG20", "ENG30");
        pwnVersionParser.readPwn_xx_Pwn_xx_File(nmap, "n");
        pwnVersionParser.readPwn_xx_Pwn_xx_File(vmap, "v");
        pwnVersionParser.readPwn_xx_Pwn_xx_File(amap, "a");
        pwnVersionParser.readPwn_xx_Pwn_xx_File(bmap, "r");
        CdbSynsetParser parser = new CdbSynsetParser();
        String cdbFile = args[0];
        try {
            FileOutputStream fos = new FileOutputStream (cdbFile+".3");
            String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<cdbsyn.dbxml>\n";
            fos.write(str.getBytes());
            parser.parseFile(cdbFile);
            Set keySet = parser.synsetIdSynsetMap.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
                ArrayList erels = (ArrayList) synset.getERelations();
                for (int i = 0; i < erels.size(); i++) {
                    CdbEquivalenceRelation erel = (CdbEquivalenceRelation) erels.get(i);
                    String target = erel.getTarget20();
                    if (pwnVersionParser.pwn_xx_pwn_xx.containsKey(target)) {
                        PwnVersionMapping mapping = (PwnVersionMapping) pwnVersionParser.pwn_xx_pwn_xx.get(target);
                        erel.setTarget30(mapping.getTarget());
                    }
                }
                str = synset.toString();
                fos.write(str.getBytes());
            }
            str = "</cdbsyn.dbxml>\n";
            fos.write(str.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
