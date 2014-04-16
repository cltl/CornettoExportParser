package vu.cornetto.pwn;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 11-jul-2006
 * Time: 18:34:09
 * To change this template use Options | File Templates.
 */
public class WordNetDomainsParser {

    static final String SYSTEM_ENCODING = System.getProperty("file.encoding");
    public HashMap WORDNETDOMAINS;


    public WordNetDomainsParser () {
        WORDNETDOMAINS = new HashMap();
    }

    public void readWnDomainFile (String filePath) {
//01208714-n	zoology
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, SYSTEM_ENCODING);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            String key = "";
            String value = "";
            ArrayList domains = new ArrayList();
            int idx = 0;
            int nLine = 0;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                nLine++;
                //System.out.println(nLine);
                if ((inputLine.trim().length()>0)){
                    idx = inputLine.indexOf("\t");
                    if (idx>-1) {
                        key = inputLine.substring(0,idx).trim();
                        value = inputLine.substring(idx+1).trim();
                        if ((key.length()>0) && (value.length()>0)) {
                            if (WORDNETDOMAINS.containsKey(key)) {
                                domains = (ArrayList) WORDNETDOMAINS.get(key);
                                domains.add(value);
                                WORDNETDOMAINS.put(key, domains);
                            }
                            else {
                                domains = new ArrayList();
                                domains.add(value);
                                WORDNETDOMAINS.put(key, domains);
                            }
                        }
                        else {
                            System.out.println("No key or value");
                        }
                    }
                    else {
                        System.out.println("No TAB");
                    }
                }
                else {
                    System.out.println("Blank line");
                }
            }
            System.out.println("Read DOMAIN data for:"+WORDNETDOMAINS.size()+" synsets");
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
