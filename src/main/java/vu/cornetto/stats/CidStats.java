package vu.cornetto.stats;

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
public class CidStats {

    static final String [] statusLabels = {"manual:ADJ",
"manual:ADJECTIVE",
"manual:NOUN",
"manual:VERB",
"manual:ADVERB",
"manual:",
"B-95:ADJ",
"B-95:NOUN",
"B-95:VERB",
"BM-90:ADJ",
"BM-90:ADJECTIVE",
"BM-90:NOUN",
"BM-90:VERB",
"BM-90:REST",
"M-97:ADJ",
"M-97:ADJECTIVE",
"M-97:NOUN",
"M-97:VERB",
"RESUME-75:ADJ",
"RESUME-75:NOUN",
"RESUME-75:VERB",
"D-75:NOUN",
"D-58:VERB",
"D-55:ADJ",
":ADJ",
":ADJECTIVE",
":ADVERB",
":CONJUNCTION",
":INTERJECTION",
":NOUN",
":NUMERAL",
":PREPOSITION",
":PRONOUN",
":VERB"};
    static int [] statusInt = new int [statusLabels.length];


    static public void main (String [] args) {
        String cidPath = args[0];
        System.out.println("cidPath = " + cidPath);
        try {
            FileOutputStream fos = new FileOutputStream(cidPath+".data.log");
            FileOutputStream fosNoun = new FileOutputStream(cidPath+".noun.xls");
            FileOutputStream fosVerb = new FileOutputStream(cidPath+".verb.xls");
            FileOutputStream fosAdj = new FileOutputStream(cidPath+".adj.xls");
            FileOutputStream fosRest = new FileOutputStream(cidPath+".rest.xls");
            CdbCidArraySaxParser parser = new CdbCidArraySaxParser ();
            parser.parseFile(cidPath);
            //parser.parseFileAsBytes(cidPath);
            Set keyset = parser.formMap.keySet();
            Iterator keys = keyset.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                ArrayList cids = (ArrayList) parser.formMap.get(key);
                int topSense = 0;
                String name = "";
                CdbCid topCid = null;
                for (int i=0;i<cids.size();i++) {
                    CdbCid cid = (CdbCid) cids.get(i);
                    if ((name.length()==0) && (!cid.getName().equals("hel"))) {
                        name = cid.getName();
                    }
                    if ((cid.getC_seq_nr()>2) && (cid.getC_seq_nr()>topSense)) {
                        topCid = cid;
                        topSense = cid.getC_seq_nr();
                        if ((name.length()>0) && (cid.getName().equals("hel"))) {
                            topCid.setName(name);
                        }
                    }
                    for (int j=0;j<cids.size();j++) {
                        if (i!=j) {
                            CdbCid oCid = (CdbCid) cids.get(j);
                            if ((oCid.getC_seq_nr()==cid.getC_seq_nr()) &&
                                (!oCid.getC_lu_id().equals(cid.getC_lu_id()))) {
                                System.out.println("cid.\t" + cid.getCid());
                                System.out.println("oCid.\t" + oCid.getCid());
                                System.out.println("cid.getC_lu_id()\t" + cid.getC_lu_id());
                                System.out.println("cid.getSelected()\t" + cid.getSelected());
                                System.out.println("oCid.getSelected()\t" + oCid.getSelected());
                                System.out.println("oCid.getC_lu_id()\t" + oCid.getC_lu_id());
                                System.out.println("oCid.getC_form()\t" + oCid.getC_form());
                                System.out.println("oCid.getC_pos()\t" + oCid.getC_pos());
                                System.out.println("oCid.getC_seq_nr()\t" + oCid.getC_seq_nr());
                            }
                        }
                    }
                }
                if (topCid!=null) {
                    String str = topCid.getC_form()+"\t"+topCid.getC_pos()+"\t"+topCid.getC_seq_nr()+"\t"+topCid.getName()+"\n";
                    if (topCid.getC_pos().equalsIgnoreCase("NOUN")) {
                        fosNoun.write(str.getBytes());
                    }
                    else if (topCid.getC_pos().equalsIgnoreCase("VERB")) {
                        fosVerb.write(str.getBytes());
                    }
                    else if (topCid.getC_pos().equalsIgnoreCase("ADJ")) {
                        fosAdj.write(str.getBytes());
                    }
                    else {
                        fosRest.write(str.getBytes());
                    }
                }
            }
            System.out.println("\nSTATUS MAP");
            keyset = parser.statusMap.keySet();
            keys = keyset.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Integer count = (Integer) parser.statusMap.get(key);
                int idx = vu.cornetto.util.Other.idxStringArray(key, statusLabels);
                if (idx>-1) {
                    statusInt[idx] = count.intValue();
                }
                else {
                    System.out.println("UNKNOWN KEY AS STATUS:"+key);
                }
                //System.out.println(key+":"+count.toString());
            }
            int manual = 0;
            int b95 = 0;
            int bm90 = 0;
            int d55 = 0;
            int d58 = 0;
            int d75 = 0;
            int m97 = 0;
            int resume = 0;
            int auto = 0;

            for (int i = 0; i < statusLabels.length; i++) {
                String statusLabel = statusLabels[i];
                int count = statusInt[i];
                if (statusLabel.startsWith("manual:")) {
                    manual += count;
                }
                else if (statusLabel.startsWith("B-95:")) {
                    b95 += count;
                }
                else if (statusLabel.startsWith("BM-90:")) {
                    bm90 += count;
                }
                else if (statusLabel.startsWith("D-55:")) {
                    d55 += count;
                }
                else if (statusLabel.startsWith("D-58:")) {
                    d58 += count;
                }
                else if (statusLabel.startsWith("D-75:")) {
                    d75 += count;
                }
                else if (statusLabel.startsWith("M-97:")) {
                    m97 += count;
                }
                else if (statusLabel.startsWith("RESUME-75:")) {
                    resume += count;
                }
                else if (statusLabel.startsWith(":")) {
                    auto += count;
                }
                System.out.println(statusLabel+"\t"+count);
            }
            System.out.println("\nNo status value\t" + auto);
            System.out.println("Status value\t" + (manual+b95+bm90+d55+d58+d75+m97+resume));
            System.out.println("manual\t" + manual);
            System.out.println("B-95\t" + b95);
            System.out.println("BM-90\t= " + bm90);
            System.out.println("D-55\t" + d55);
            System.out.println("D-58\t" + d58);
            System.out.println("D-75\t" + d75);
            System.out.println("M-97\t" + m97);
            System.out.println("RESUME-75\t" + resume);

            System.out.println("\nNAME MAP");
            keyset = parser.nameMap.keySet();
            keys = keyset.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Integer count = (Integer) parser.nameMap.get(key);
                System.out.println(key+"\t"+count.toString());
            }
            keyset = parser.cidLuMap.keySet();
            keys = keyset.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                ArrayList cids = (ArrayList) parser.cidLuMap.get(key);
                for (int i=0;i<cids.size();i++) {
                    CdbCid cid = (CdbCid) cids.get(i);
                    fos.write(("CID:"+cid.getCid()+"\n").getBytes());
                    fos.write(("LU:"+cid.getC_lu_id()+"\n").getBytes());
                    fos.write(("SYNSET:"+cid.getC_sy_id()+"\n").getBytes());
                    fos.write(("FORM:"+cid.getC_form()+"\n").getBytes());
                    fos.write(("SEQNR:"+cid.getC_seq_nr()+"\n").getBytes());
                }
            }
            fos.close();
            fosNoun.close();
            fosVerb.close();
            fosAdj.close();
            fosRest.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
