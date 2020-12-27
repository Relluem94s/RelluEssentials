/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rellu
 */
public class StringHelper {

    Map<String, String> symbols = new HashMap<>();

    public StringHelper() {
        initSymbols();
    }

    public String symbole(String sym) {
        for (Map.Entry pair : symbols.entrySet()) {
            sym = sym.replace((String)pair.getKey(), (String)pair.getValue());
        }
        return sym;
    }

    private void initSymbols() {
        symbols.put("[~]", "\u0020");
        symbols.put("[DICK]", "\u2593");
        symbols.put("[STROKES]", "\u2591");
        symbols.put("[PICKAXE]", "\u26CF");
        symbols.put("[HOT]", "\u2668");
        symbols.put("[SKULL]", "\u2620");
        symbols.put("[:)]", "\u263A");
        symbols.put("[:C]", "\u2639");
        symbols.put("[ATLANTIS]", "\u212B");
        symbols.put("[:D]", "\u1CE9");
        symbols.put("[CAM]", "\u1CC0");
        symbols.put("[CROSS]", "\u16ED");
        symbols.put("[<3]", "\u2764");
        symbols.put("[NINJA]", "             ");
        symbols.put("[ARROW]", "\u279c");
        symbols.put("[TICK]", "\u2714");
        symbols.put("[X]", "\u2716");
        symbols.put("[STAR]", "\u2605");
        symbols.put("[DOT]", "\u25Cf");
        symbols.put("[FLOWER]", "\u273f");
        symbols.put("[XD]", "\u263b");
        symbols.put("[ATTENTION]", "\u26a0");
        symbols.put("[MAIL]", "\u2709");
        symbols.put("[ARROW2]", "\u27a4");
        symbols.put("[STAR2]", "\u2730");
        symbols.put("[SUIT]", "\u2666");
        symbols.put("[+]", "\u2726");
        symbols.put("[CIRCLE]", "\u25CF");
        symbols.put("[SUN]", "\u2739");
        symbols.put("[RTM]", "\u00AE");
        symbols.put("[COPY]", "\u00A9");
        symbols.put("[OMEGA]", "\u03A9");
        symbols.put("[LAMBDA]", "\u03BB");
        symbols.put("[ROUND]", "\u058D");
        symbols.put("[^^]", "\u0E05(\u0E4F.\u0E4F)\u0E05");
        symbols.put("[TM]", "\u2122");
        symbols.put("[No]", "\u2116");
        symbols.put("[H]", "\u210B");
        symbols.put("[h]", "\u210C");
        symbols.put("[L]", "\u2112");
        symbols.put("[l]", "\u2113");
        symbols.put("[R]", "\u211C");
        symbols.put("[B]", "\u212C");
        symbols.put("[E]", "\u2130");
        symbols.put("[e]", "\u212F");
        symbols.put("[F]", "\u2131");
        symbols.put("[M]", "\u2133");
        symbols.put("[1]", "\u2160");
        symbols.put("[2]", "\u2161");
        symbols.put("[3]", "\u2162");
        symbols.put("[4]", "\u2163");
        symbols.put("[5]", "\u2164");
        symbols.put("[6]", "\u2165");
        symbols.put("[7]", "\u2166");
        symbols.put("[8]", "\u2167");
        symbols.put("[9]", "\u2168");
        symbols.put("[10]", "\u2169");
        symbols.put("[11]", "\u216A");
        symbols.put("[12]", "\u216B");
        symbols.put("[RELOAD]", "\u21BA");
        symbols.put("[>]", "\u00BB");
        symbols.put("[<]", "\u00AB");
        symbols.put("[1/2]", "\u00BD");
        symbols.put("[1/4]", "\u00BC");
        symbols.put("[B|]", "\u16D4\u16DA");
        symbols.put("[xD]", "\u16DD\u16A6");
    }
}
