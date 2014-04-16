package vu.cornetto.conversions;

import vu.cornetto.cdb.*;

import java.util.ArrayList;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 28-okt-2008
 * Time: 18:41:42
 * To change this template use File | Settings | File Templates.
 */
public class DataForBc {

    //// READS CDB SYNSET XML AND FILE WITH SYNSET IDS TO GENERATE DEBUG OUTPUT
    static public void main (String [] args) {
        String cdbSynFilePath = args[0];
        String bcFilePath = args[1];
        String outputFilePath = bcFilePath+".data";
        CdbSynsetParser parser = new CdbSynsetParser();
        parser.parseFile(cdbSynFilePath);
        try {
            String str = "";
            FileOutputStream out = new FileOutputStream (outputFilePath);
            FileInputStream fis = new FileInputStream(bcFilePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            String key = "";
            String freq = "";
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                key = inputLine;
                int idx = inputLine.indexOf(" ");
                if (idx>-1) {
                    key= inputLine.substring(0, idx).trim();
                    freq = inputLine.substring(idx).trim();
                }
                String freqTabbed = "";
                for (int i=4;i>freq.length();i--) {
                    freqTabbed += " ";
                }
                freqTabbed += freq;
                str = freqTabbed+" "+key+" @";
                if (key.charAt(key.length()-2)=='-') {
                    key = key.substring(0, key.length()-2);
                }
                CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
                if (synset!=null) {
                    ArrayList syns = synset.getSynonyms();
                    for (int i=0; i<syns.size();i++) {
                          Syn rel = (Syn) syns.get(i);
                          str +=  rel.getPreviewtext()+":";
                    }
                    str += "@";
                    ArrayList relations = synset.getERelations();
                    for (int i=0; i<relations.size();i++) {
                          CdbEquivalenceRelation rel = (CdbEquivalenceRelation) relations.get(i);
                          str += rel.getRelationName()+":"+rel.getTarget20()+":"+rel.getTarget20Previewtext()+":"+rel.getTarget30()+":";
                    }
                }
                str += "\n";
                out.write(str.getBytes());

            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
