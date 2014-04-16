package vu.cornetto.pwn;

import java.util.HashMap;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 11-jul-2006
 * Time: 18:43:59
 * To change this template use Options | File Templates.
 */
public class PwnVersionParser {
    static final String SYSTEM_ENCODING = System.getProperty("file.encoding");

    String sourceKey;
    String targetKey;
    public HashMap pwn_xx_pwn_xx;

    public PwnVersionParser (String sourceKey, String targetKey) {
        pwn_xx_pwn_xx = new HashMap();
        this.sourceKey = sourceKey;
        this.targetKey = targetKey;
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

    public void readPwn_xx_Pwn_xx_File(String filePath, String pos) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, SYSTEM_ENCODING);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            String key = "";
            String value = "";
            String target = "";
            String scoreString = "";
            int nKeys = 0;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
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
                                key = sourceKey+"-"+key+"-"+pos;
                                target = targetKey+"-"+target+"-"+pos;
                                PwnVersionMapping mapping = new PwnVersionMapping(new Double (scoreString).doubleValue(), target);
                                nKeys++;
                                if (nKeys==10) {
                                    System.out.println("sourceKey = " + sourceKey);
                                    System.out.println("targetKey = " + targetKey);
                                    System.out.println("Adding pwn key = " + key);
                                }
                                pwn_xx_pwn_xx.put(key, mapping);
                            }
                        }
                        idx_s = idx_e+1;
                        idx_e = inputLine.indexOf(" ", idx_s);
                    }
                }
            }
            System.out.println("Read data for:"+ pwn_xx_pwn_xx.size()+" mappings");
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


        public static void main(String[] args) throws IOException
        {
          String map = args[0];
          String pos = args[1];
          String sourceKey = args[2];
          String targetKey = args[3];
          PwnVersionParser parser = new PwnVersionParser(sourceKey, targetKey);
        }

}
