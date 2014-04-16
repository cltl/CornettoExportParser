package vu.cornetto.pwn;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: kyoto
 * Date: 2/3/12
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class PwnUtilities {
    
    
    static public void getRelationChain (HashMap<String, ArrayList<String>> relations, String source, ArrayList<String> targetChain) {
        if (relations.containsKey(source)) {
            ArrayList<String> targets = relations.get(source);
            for (int i = 0; i < targets.size(); i++) {
                String target =  targets.get(i);
                if (!target.equals(source)) {
                    if (!targetChain.contains(target)) {
                        targetChain.add(target);
                        //  System.out.println("target = " + target);
                        getRelationChain(relations, target, targetChain);
                    }
                }
            }
        }
    }
}
