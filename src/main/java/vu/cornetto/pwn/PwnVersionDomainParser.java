package vu.cornetto.pwn;

import vu.cornetto.dwn.DwnDomainRelation;
import vu.cornetto.util.FileProcessor;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 11-jul-2006
 * Time: 18:43:59
 * To change this template use Options | File Templates.
 */
public class PwnVersionDomainParser {
    static final String SYSTEM_ENCODING = System.getProperty("file.encoding");

/*
    public HashMap pwn16pwn20;
    public HashMap pwn15pwn16;
*/
    public HashMap pwn16domains;

    public PwnVersionDomainParser () {
        pwn16domains = new HashMap();
/*
        pwn15pwn16 = new HashMap();
        pwn16pwn20 = new HashMap();
*/
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


    public void readWordNetDomain16(String filePath) {
        WordNetDomainsParser parser = new WordNetDomainsParser();
        System.out.println("DOMAIN filePath = " + filePath);
        parser.readWnDomainFile(filePath);
        pwn16domains = parser.WORDNETDOMAINS;
    }

    public void readPwn15Pwn16File (String filePath, String pos, String version) {
        try {
            FileOutputStream fos = new FileOutputStream (filePath+".dom");
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, SYSTEM_ENCODING);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            String key = "";
            String value = "";
            String target = "";
            String scoreString = "";
            ArrayList targets = new ArrayList();
            DwnDomainRelation dom = new DwnDomainRelation();
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
                                target += "-"+pos;
                                if (pwn16domains.containsKey(target)) {
                                    ArrayList doms = (ArrayList) pwn16domains.get(target);
                                    for (int i=0;i<doms.size();i++) {
                                        String aDom = (String) doms.get(i);
                                        dom = new DwnDomainRelation(aDom);
                                        if (version.equals("15")) {
                                            dom.setPwn15Synset_id("ENG15-"+key+"-"+pos);
                                        }
                                        else if (version.equals("20")) {
                                            dom.setPwn20Synset_id("ENG20-"+key+"-"+pos);
                                        }
                                        dom.setPwn16Synset_id("ENG16-"+target);
                                        dom.setScore((new Double (scoreString)).doubleValue());
                                        FileProcessor.storeResult(fos, dom.toStringWithTargets("\t"));
                                    }
                                }
                                else {
                                    System.out.println("Could not find target = " + target);
                                }
                            }
                        }
                        idx_s = idx_e+1;
                        idx_e = inputLine.indexOf(" ", idx_s);
                    }
                }
            }
            System.out.println("last dom.getTerm() = " + dom.getTerm());
            System.out.println("last dom.getPwn15Synset_id = " + dom.getPwn15Synset_id());
            System.out.println("last dom.getPwn16Synset_id = " + dom.getPwn16Synset_id());
            FileProcessor.storeResult(fos, "</XML>\n");
            System.out.println("Read data for:"+pwn16domains.size()+" mappings");
            in.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


   public static void main(String[] args) throws IOException {
          String map = args[0];
          String pwnDomains = args[1];
          String pos = args[2];
          String version = args[3];
          PwnVersionDomainParser parser = new PwnVersionDomainParser();
          parser.readWordNetDomain16(pwnDomains);
          parser.readPwn15Pwn16File(map, pos, version);
   }

}
