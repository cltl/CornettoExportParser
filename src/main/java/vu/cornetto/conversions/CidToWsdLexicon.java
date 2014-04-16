package vu.cornetto.conversions;

import vu.cornetto.cdb.CdbCidArraySaxParser;
import vu.cornetto.cdb.CdbCid;

import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 25-aug-2008
 * Time: 13:03:14
 * To change this template use File | Settings | File Templates.
 */
public class CidToWsdLexicon {

    static public void main (String [] args) {
        String cidPath = args[0];
        System.out.println("cidPath = " + cidPath);
        try {
            FileOutputStream fos = new FileOutputStream(cidPath+".lex");
            CdbCidArraySaxParser parser = new CdbCidArraySaxParser ();
            parser.parseFile(cidPath);
            //parser.parseFileAsBytes(cidPath);
            Set keyset = parser.entryMap.keySet();
            Iterator keys = keyset.iterator();
            String str = "";
            String cleanStr = "";
            final String filter = "()";
            while (keys.hasNext()) {
                cleanStr = "";
                String key = (String) keys.next();
                str = key.replaceAll(" ", "_");
                for (int i=0; i<str.length();i++) {
                    char c = str.charAt(i);
                    if (filter.indexOf(c)==-1) {
                        cleanStr += c;
                    }
                }
                str = cleanStr;
                ArrayList cids = (ArrayList) parser.entryMap.get(key);
                for (int i=0;i<cids.size();i++) {
                    CdbCid cid = (CdbCid) cids.get(i);
                    if (cid.getSelected()) {
                          String targetId = cid.getC_sy_id();
                          if (targetId.length()>0) {
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
                              else if (targetId.startsWith("c_")) {
                                  targetId += "-n";
                              }
                              str += " "+targetId;
                          }
                    }
                }
                str = str.trim()+"\n";
                if (str.indexOf("-")!=-1) {
                    fos.write(str.getBytes());
                }
                else {
                    System.out.println("Not targets for str = " + str);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}