package vu.cornetto.pwn;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 25-apr-2006
 * Time: 13:47:08
 * To change this template use Options | File Templates.
 */
public class Domains {

    static final String SYSTEM_ENCODING = System.getProperty("file.encoding");

    static public HashMap rbnDomains;
    static public HashMap semnetDomains;


    static void makeMatrix (String outputFile, String rbnFilePath, String semNetFilePath) {
        rbnDomains = new HashMap();
        semnetDomains = new HashMap();
        readSemNetDomains(semNetFilePath);
        readrbnDomains(semNetFilePath);
        generateMatrix(outputFile);
    }

    static void readSemNetDomains (String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, SYSTEM_ENCODING);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            String domainLabel = "";
            String words = "";
            int nLines = 0;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                //System.out.println(nLines);
                if (inputLine.trim().length()>10) {
                    nLines++;
                    if (inputLine.startsWith("Name = ")) {
                       domainLabel = inputLine.substring(7);
                    }
                    else if (nLines==4) {
                       words = inputLine.trim();
                       if ((domainLabel.length()>0) && (words.length()>0)) {
                           semnetDomains.put(domainLabel, stringToWordList(words));
                           domainLabel = "";
                           words = "";
                       }
                       nLines = 0;
                    }
                }
            }
            System.out.println("Read:"+semnetDomains.size()+" domains");
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void readrbnDomains (String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, SYSTEM_ENCODING);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            String domainLabel = "";
            String words = "";
            int nLines = 0;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                if (inputLine.trim().length()>10) {
                    nLines++;
                    if (inputLine.startsWith("Name = ")) {
                       domainLabel = inputLine.substring(7);
                    }
                    else if (nLines==4) {
                       words = inputLine.trim();
                       if ((domainLabel.length()>0) && (words.length()>0)) {
                           rbnDomains.put(domainLabel, stringToWordList(words));
                           domainLabel = "";
                           words = "";
                       }
                       nLines = 0;
                    }
                }
            }
            System.out.println("Read:"+rbnDomains.size()+" domains");
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void generateMatrix (String outputFilePath) {
        try {
         String str = "";
         FileOutputStream fout = new FileOutputStream (outputFilePath);
         Set keySet = semnetDomains.keySet();
         Iterator keys = keySet.iterator();
         while (keys.hasNext()) {
             String key = (String) keys.next();
             ArrayList words = (ArrayList) semnetDomains.get(key);
             str = key+"\t"+words.size();
             str += overlapString (words)+"\n";
             fout.write(str.getBytes(SYSTEM_ENCODING));
         }
         fout.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    static String overlapString (ArrayList compWords) {
         String str = "";
         int overLap = 0;
         double percentTage = 0;
         Set keySet = rbnDomains.keySet();
         Iterator keys = keySet.iterator();
         while (keys.hasNext()) {
             String key = (String) keys.next();
             overLap = 0;
             percentTage = 0;
             ArrayList words = (ArrayList) rbnDomains.get(key);
             str += "\t"+key+"\t"+words.size();
             for (int i=0;i<words.size();i++) {
                 if (compWords.contains(words.get(i))) {
                     overLap++;
                 }
             }
             percentTage = (double) overLap/(double)words.size();
             str += "\t"+overLap+"\t"+percentTage;
         }
         return str;
    }

    static ArrayList stringToWordList (String words) {
        ArrayList wordList = new ArrayList ();
        String word = "";
        int idx_s = 0;
        int idx_e = words.indexOf("\t");
        while (idx_e>-1) {
            word = words.substring(idx_s, idx_e).trim();
            if (word.length()>0) {
                wordList.add(word);
            }
            idx_s = idx_e+1;
            idx_e = words.indexOf("\t", idx_s);
        }
        word = words.substring(idx_s).trim();
        if (word.length()>0) {
            wordList.add(word);
        }
        return wordList;
    }


      public static void main(String[] args) {
          String rbnPath = args[0];
          String semNetPath = args[1];
          makeMatrix ("matrix.out", rbnPath, semNetPath);
      }
}
