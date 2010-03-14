package com.tickets.controllers.handlers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.WordUtils;

public class TransliterationHandler {

    public static Map<Character, String> cyrLatMap = new HashMap<Character, String>();
    public static Map<String, String> exceptions = new HashMap<String, String>();
    public Map<String, String> correspondences = new HashMap<String, String>();

    static {
        cyrLatMap.put('а', "a");
        cyrLatMap.put('б', "b");
        cyrLatMap.put('в', "v");
        cyrLatMap.put('г', "g");
        cyrLatMap.put('д', "d");
        cyrLatMap.put('е', "e");
        cyrLatMap.put('ж', "zh");
        cyrLatMap.put('з', "z");
        cyrLatMap.put('и', "i");
        cyrLatMap.put('й', "y");
        cyrLatMap.put('к', "k");
        cyrLatMap.put('л', "l");
        cyrLatMap.put('м', "m");
        cyrLatMap.put('н', "n");
        cyrLatMap.put('о', "o");
        cyrLatMap.put('п', "p");
        cyrLatMap.put('р', "r");
        cyrLatMap.put('с', "s");
        cyrLatMap.put('т', "t");
        cyrLatMap.put('у', "u");
        cyrLatMap.put('ф', "f");
        cyrLatMap.put('х', "h");
        cyrLatMap.put('ц', "ts");
        cyrLatMap.put('ч', "ch");
        cyrLatMap.put('ш', "sh");
        cyrLatMap.put('щ', "sht");
        cyrLatMap.put('ъ', "a");
        cyrLatMap.put('ь', "y");
        cyrLatMap.put('ю', "yu");
        cyrLatMap.put('я', "ya");
    }

    static {
        exceptions.put("София", "Sofia");
    }

    public String toLatin(String name) {
        for (String exception : exceptions.keySet()) {
            name = name.replace(exception, exceptions.get(exception));
        }

        StringBuilder sb = new StringBuilder();
        for (char c : name.toLowerCase().toCharArray()) {
            String lat = cyrLatMap.get(c);
            if (lat == null) {
                lat = Character.toString(c);
            }
            sb.append(lat);
        }

        String result = WordUtils.capitalizeFully(sb.toString());
        correspondences.put(result, name);

        return result;
    }

    public String getCyrillicOriginal(String latinName) {
        return correspondences.get(latinName);
    }
}
