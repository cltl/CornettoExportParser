package vu.cornetto.conversions;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: kyoto
 * Date: Nov 14, 2010
 * Time: 9:08:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class FixSynsetIds {
    /*
    d_n-27129 d_n-43114
d_n-27129 d_n-37070
d_v-591 d_v-8630
d_v-591 d_v-1365
d_v-591 c_777
     */

    static public String fixSynsetId (String id) {
        String newId = id;
        if (id.contains("_n-")) {
            newId+="-n";
        }
        else if (id.contains("_v-")) {
            newId+="-v";
        }
        else if (id.contains("_a-")) {
            newId+="-a";
        }
        else if (id.contains("_r-")) {
            newId+="-r";
        }
        else {
            ///
        }
        return newId;
    }

    static public void main (String[] args) {
        String fileName = args[0];
        if (new File(fileName).exists() ) {
            try {
                FileOutputStream fos = new FileOutputStream (fileName+".fix");
                FileInputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                   // System.out.println(inputLine);
                    if (inputLine.trim().length()>0) {
                      String [] fields = inputLine.split(" ");
                      if (fields.length==2) {
                          String id1 = fixSynsetId(fields[0]);
                          String id2 = fixSynsetId(fields[1]);
                          String str = id1+" "+id2+"\n";
                          fos.write(str.getBytes());
                      }
                      else {
                          fos.write((inputLine+"\n").getBytes());
                      }
                    }
                }
                in.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
