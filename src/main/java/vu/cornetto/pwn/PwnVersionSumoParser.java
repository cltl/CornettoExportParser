package vu.cornetto.pwn;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;

import vu.cornetto.sumo.PwnSumoParser;
import vu.cornetto.sumo.SumoRelation;
import vu.cornetto.util.FileProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 13-jun-2006
 * Time: 7:47:29
 * To change this template use Options | File Templates.
 */
public class PwnVersionSumoParser {

    static final String SYSTEM_ENCODING = System.getProperty("file.encoding");

    public HashMap pwn15pwn20;
    public HashMap pwn16pwn20;
    public HashMap pwn20sumo;

    public PwnVersionSumoParser () {
        pwn15pwn20 = new HashMap();
        pwn16pwn20 = new HashMap();
        pwn20sumo = new HashMap();
    }

    /*
00002403 00001740 1
00002728 00003009 0.365 00003226 0.635
00003504 00004609 1
00003627 00004740 1
00003711 00004824 1
00004473 00005598 1
00004865 00006026 1
    */


    public void readSumoPwn (String filePath, String pos) {
        PwnSumoParser sumoParser = new PwnSumoParser();
        System.out.println("SUMO filePath = " + filePath);
        sumoParser.readSumoPwnFile(filePath, pos);
        pwn20sumo = sumoParser.SUMOPWN;
    }

    public void readPwn15Pwn20File (String filePath, String pos) {
        try {
            FileOutputStream fos = new FileOutputStream (filePath+".sum");
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, SYSTEM_ENCODING);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            String key = "";
            String value = "";
            String target = "";
            String scoreString = "";
            ArrayList targets = new ArrayList();
            int idx = 0;
            int nLine = 0;
            FileProcessor.storeResult(fos, "<XML>\n");
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                nLine++;
                //System.out.println(nLine);
                if (inputLine.trim().length()>0) {
                    key =  inputLine.substring(0,8);
                    int idx_e = inputLine.indexOf(" ");
                    int idx_s = 8;
                    while (idx_e >-1) {
                        value = inputLine.substring(idx_s, idx_e).trim();
                        if ((value.length()==8) && value.indexOf(".")==-1) {
                            target = value;
                        }
                        else {
                            scoreString = value;
                            if (vu.cornetto.util.Other.checkStringNumber(scoreString)) {
                                if (pwn20sumo.containsKey(target)) {
                                    ArrayList rels = (ArrayList) pwn20sumo.get(target);
                                    for (int i=0;i<rels.size();i++) {
                                        SumoRelation rel = (SumoRelation) rels.get(i);
                                        rel.setPwn15Synset_id("ENG15-"+key+"-"+pos);
                                        rel.setPwn16Synset_id("ENG16-"+key+"-"+pos);
                                        rel.setScore((new Double (scoreString)).doubleValue());
                                        FileProcessor.storeResult(fos, rel.toDataFile("\t"));
                                    }
                                }
                            }
                        }
                        idx_s = idx_e+1;
                        idx_e = inputLine.indexOf(" ", idx_s);
                    }
                }
            }
            FileProcessor.storeResult(fos, "</XML>\n");
            System.out.println("Read data for:"+pwn15pwn20.size()+" mappings");
            in.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



       public static void main(String[] args) throws IOException
       {
         String map = args[0];
         String pwnSumo = args[1];
         String pos = args[2];
         PwnVersionSumoParser parser = new PwnVersionSumoParser();
         parser.readSumoPwn(pwnSumo, pos);
         parser.readPwn15Pwn20File(map, pos);
       }

}
