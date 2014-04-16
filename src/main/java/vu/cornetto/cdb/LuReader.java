package vu.cornetto.cdb;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 11-dec-2008
 * Time: 9:01:51
 * To change this template use File | Settings | File Templates.
 */
public class LuReader {
    public HashMap luMap;

    public LuReader () {
        luMap = new HashMap();        
    }
    
    public void readLuData (String filePath) {
        /*
r_v-9981#weggeven#verb
r_v-9982#weggeven#verb
r_v-9983#wegglijden#verb
r_v-9984#wegglippen#verb
r_v-9985#weggooien#verb
r_v-9986#weghalen#verb        
         */
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            int mapCount = 0;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                if (inputLine.trim().length()>0) {
                    String [] fields = inputLine.split("#");
                    if (fields.length==4) {
                        String lu_id = fields[0].trim();
                        String form = fields[1].trim();
                        String seq = fields[2].trim();
                        String formPos = fields[3].trim();
                        CdbLuForm lu = new CdbLuForm();
                        lu.setFormCat(formPos);
                        lu.setFormSpelling(form);
                        lu.setSeqNr(seq);
                        luMap.put(lu_id, lu);
                    }
                }
            }
            System.out.println("read mapppings = " + mapCount);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readLuData2 (String filePath) {
        /*
r_v-9981#weggeven#verb
r_v-9982#weggeven#verb
r_v-9983#wegglijden#verb
r_v-9984#wegglippen#verb
r_v-9985#weggooien#verb
r_v-9986#weghalen#verb
         */
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            int mapCount = 0;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                if (inputLine.trim().length()>0) {
                    String [] fields = inputLine.split(";");
                    if (fields.length==3) {
                        String lu_id = fields[0].trim();
                        String form = fields[1].trim();
                        String seq = fields[2].trim();
                        String formPos = fields[3].trim();
                        CdbLuForm lu = new CdbLuForm();
                        lu.setFormCat(formPos);
                        lu.setFormSpelling(form);
                        lu.setSeqNr(seq);
                        luMap.put(lu_id, lu);
                    }
                }
            }
            System.out.println("read mapppings = " + mapCount);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
