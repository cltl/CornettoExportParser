package vu.cornetto.pwn;

import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 12-feb-2009
 * Time: 12:35:28
 * To change this template use File | Settings | File Templates.
 */
public class BaseConceptReader {

    static public ArrayList readFile(String filePath) {
        ArrayList bcs = new ArrayList();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            String key = "";
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                //System.out.println(nLine);
                if (inputLine.trim().length()>10) {
                    key =  "ENG30-"+inputLine.substring(0,10).trim();
                    System.out.println("key = " + key);
                    bcs.add(key);
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bcs;
    }


}
