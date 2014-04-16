package vu.cornetto.dwn;

import vu.cornetto.util.FileProcessor;

import java.io.*;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 10-jun-2006
 * Time: 16:38:58
 * To change this template use Options | File Templates.
 */
public class DwnParser {

    static String ANSI_ENCODING            = "ISO-8859-1";
    static final String SYSTEM_ENCODING = System.getProperty("file.encoding");
 //   static final String ANSI = "ISO-8859-1";
    public int newIds;
    public HashMap dwnSenses;
    public HashMap vlisIds;
    public HashMap synsetIds;

    /*
      0 @10187@ WORD_MEANING
  1 PART_OF_SPEECH "n"
  1 VARIANTS
    2 LITERAL "bas"
      3 SENSE 2
      3 DEFINITION "2ndOrderEntity;Agentive;BoundedEvent;Cause;Communication;Dynamic;Existence;Experience;Phenomenal;Physical;Purpose;SituationType;Social;Stimulating;UnboundedEvent;"
      3 STATUS 21940
      3 EXTERNAL_INFO
        4 CORPUS_ID 1
          5 FREQUENCY 7
        4 SOURCE_ID 2001
    2 LITERAL "basstem"
      3 SENSE 1
      3 STATUS 22218
      3 EXTERNAL_INFO
        4 CORPUS_ID 1
          5 FREQUENCY 39
        4 SOURCE_ID 2001
    */


    public DwnParser () {
        newIds = 5000000;
        dwnSenses = new HashMap();
        vlisIds = new HashMap();
        synsetIds = new HashMap();
    }

    public void readIdTableFromEwn (String filePath, String pos) {
        try {

            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, ANSI_ENCODING);
            BufferedReader in = new BufferedReader(isr);
            int nLines = 0;
            String inputLine = "";
            String synsetId = "";
            String sense = "";
            String vlisId = "";
            int idx_s = -1;
            int idx_e = -1;
            long nSynsets=0;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                nLines++;
                if (inputLine.trim().length()>0) {
                    if (inputLine.indexOf("WORD_MEANING")>-1) {
                       idx_s = inputLine.indexOf("@");
                       idx_e = inputLine.lastIndexOf("@");
                       synsetId = "d_"+pos+"-"+inputLine.substring(idx_s+1, idx_e).trim();
                       //synsetId = inputLine.substring(idx_s+1, idx_e).trim();
                     //   System.out.println("synsetId = " + synsetId);
                       nSynsets++;
                    }
                    else if (inputLine.indexOf("2 LITERAL")>-1) {
                       idx_s = inputLine.indexOf("\"");
                       idx_e = inputLine.lastIndexOf("\"");
                       sense = inputLine.substring(idx_s+1, idx_e).trim();
                      // System.out.println("sense = "+sense);
                    }
                    else if ((inputLine.indexOf("3 SENSE")>-1)) {
                       idx_s = inputLine.lastIndexOf(" ");
                       sense += "_"+inputLine.substring(idx_s+1).trim();
                    //   System.out.println("sense = " + sense);
                    }
                    if (inputLine.indexOf("3 STATUS")>-1) {
                       idx_s = inputLine.lastIndexOf(" ");
                       vlisId = inputLine.substring(idx_s+1).trim();
                       if (vlisId.equalsIgnoreCase("0")) {
                           newIds++;
                           vlisId = "n_"+pos+"-"+newIds;
                       }
                   //    System.out.println("vlisId = " + vlisId);
                       vlisIds.put(vlisId, synsetId);
//                       dwnSenses.put(sense, vlisId);
                       synsetIds.put(sense, synsetId);

                        //System.out.println("sense = \"" + sense+"\"");
 //                       System.out.println("synsetId = " + synsetId);

                       sense = "";
                       vlisId = "";
                    }
                }
            }
            System.out.println("read: "+nSynsets +" synsets");
            System.out.println("vlisIds.size() = "+vlisIds.size());
            System.out.println("dwnSenses.size() = "+dwnSenses.size());
            System.out.println("synsetIds.size() = " + synsetIds.size());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void getSenseIdFromEwn (String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            FileOutputStream fos = new FileOutputStream(filePath+".senses");
            InputStreamReader isr = new InputStreamReader(fis, ANSI_ENCODING);
            BufferedReader in = new BufferedReader(isr);
            int nLines = 0;
            String inputLine = "";
            String synsetId = "";
            String sense = "";
            String vlisId = "";
            int idx_s = -1;
            int idx_e = -1;
            long nSynsets=0;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                nLines++;
                if (inputLine.trim().length()>0) {
                    if (inputLine.indexOf("2 LITERAL")>-1) {
                       idx_s = inputLine.indexOf("\"");
                       idx_e = inputLine.lastIndexOf("\"");
                       sense = inputLine.substring(idx_s+1, idx_e).trim();
                      // System.out.println("sense = "+sense);
                    }
                    else if ((inputLine.indexOf("3 SENSE")>-1)) {
                       idx_s = inputLine.lastIndexOf(" ");
                       sense += "_"+inputLine.substring(idx_s+1).trim();
                    //   System.out.println("sense = " + sense);
                        FileProcessor.storeResult(fos, sense+"\n", "UTF-8");
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException
    {
         String dwnFile = args[0];
         getSenseIdFromEwn (dwnFile);
    }

}
