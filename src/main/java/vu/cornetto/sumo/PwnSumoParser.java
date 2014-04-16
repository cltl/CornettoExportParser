package vu.cornetto.sumo;


import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 19-apr-2006
 * Time: 18:10:38
 * To change this template use Options | File Templates.
 */
public class PwnSumoParser {
    static final String SYSTEM_ENCODING = System.getProperty("file.encoding");
    public HashMap SUMOPWN;


    public PwnSumoParser () {
        SUMOPWN = new HashMap();
    }

    public void readSumoPwnFile (String filePath, String pos) {
//00001740 03 n 01 entity 0 010 ~ 00002056 n 0000 ~ 00005598 n 0000 ~ 00016236 n 0000 ~ 00017572 n 0000 ~ 00022625 n 0000 ~ 04253302 n 0000 ~ 08626236 n 0000 ~ 08694995 n 0000 ~ 08699136 n 0000 ~ 08843058 n 0000 | that which is perceived or known or inferred to have its own distinct existence (living or nonliving)  &%Physical=
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, SYSTEM_ENCODING);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            String key = "";
            ArrayList sumoTerms;
            ArrayList sumoRels;
            SumoRelation sRel;
            String value = "";
            String relation = "";
            int idx = 0;
            int nLine = 0;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                nLine++;
                //System.out.println(nLine);
                if ((inputLine.trim().length()>10) &&
                    (!inputLine.startsWith(";;"))){
                    key = inputLine.substring(0,8).trim();
                    idx = inputLine.indexOf("&%");
                    if (idx>-1) {
                        sumoRels = new ArrayList();
                        value = inputLine.substring(idx+2);
                        sumoTerms = vu.cornetto.util.Other.stringToWordArrayList(value);
                        for (int i=0; i< sumoTerms.size(); i++) {
                                value = (String) sumoTerms.get(i);
                                relation = value.substring(value.length()-1).trim();
                                value = value.substring(0, value.length()-1).trim();
/*
                                System.out.println("key = " + key);
                                System.out.println("value = " + value);
                                System.out.println("relation = " + relation);
*/
                                sRel = new SumoRelation (value, relation, "ENG20-"+key+"-"+pos);
                                sumoRels.add(sRel);
                        }
                        if (sumoRels.size()>0) {
                            SUMOPWN.put(key, sumoRels);
                        }
                    }
                }
            }
            System.out.println("Read SUMO data for:"+SUMOPWN.size()+" synsets");
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void outputSumoToTable (String outputFilePath) {
        try {

        }
        catch (Exception e) {

        }
    }

}
