/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import Implements.EmvTagsImpl;
import static Globals.Variables.*;
import Interfaces.EmvTags;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ctolo
 */
public class Decode551 {

    private int longest = 0;

    ArrayList<EmvTagsImpl> tags;
    ArrayList<String> tagsName = new ArrayList<>();

    public String decompile(String dataTvl) {
        EmvTags emvtags = new EmvTagsImpl();
        tags = emvtags.getAllData("");
        tags.forEach((tag) -> {
            tagsName.add(tag.getName());
        });
        longest = getLongest(tagsName);
        StringBuilder resultado = new StringBuilder();
        String[][] tvlData;
        Map<String, String> tvlMap = parseTLV(dataTvl);
        int cant = tvlMap.entrySet().size();
        tvlData = new String[cant][2];
        int cont = 0;
        for (Map.Entry entry : tvlMap.entrySet()) {
            tvlData[cont][0] = entry.getKey().toString();
            tvlData[cont][1] = entry.getValue().toString();
            cont++;
        }
        resultado.append(printTags(tvlData));
        return resultado.toString();
    }

    public Map<String, String> parseTLV(String tlv) {
        if (tlv == null || tlv.length() % 2 != 0) {
            tools.showDialogEx("Invalid tlv, null or odd length", this, new Exception());
        }
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < tlv.length();) {
            try {
                String key = tlv.substring(i, i = i + 2);

                if ((Integer.parseInt(key, 16) & 0x1F) == 0x1F) {
                    // extra byte for TAG field
                    key += tlv.substring(i, i = i + 2);
                }
                String len = tlv.substring(i, i = i + 2);
                int length = Integer.parseInt(len, 16);

                if (length > 127) {
                    // more than 1 byte for lenth
                    int bytesLength = length - 128;
                    len = tlv.substring(i, i = i + (bytesLength * 2));
                    length = Integer.parseInt(len, 16);
                }
                length *= 2;

                String value = tlv.substring(i, i = i + length);
                hashMap.put(key, value);
            } catch (NumberFormatException e) {
                tools.showDialogEx("Error parsing number", this, e);
            } catch (IndexOutOfBoundsException e) {
                tools.showDialogEx("Error processing field", this, e);

            }
        }

        return hashMap;
    }

    private int getPostion(String tagName) {
        for (EmvTagsImpl tag : tags) {
            if (tag.getTag().equals(tagName)) {
                return tags.indexOf(tag);
            }
        }
        return 0;
    }

    public String getTagName(String tag) {
        int position = 0;
        if (getPostion(tag) > 1) {
            position = getPostion(tag) - 1;
        }
        String nombreCampo = tools.padText(tags.get(position).getName(), longest, PAD_RIGHT, ' ');
        String res = tools.padText(tools.padText("[" + tag + "]", 9, PAD_LEFT, ' '), 9, PAD_RIGHT, ' ') + " " + tools.padText(nombreCampo, (longest + 3), PAD_RIGHT, ' ');
        return res;
    }

    private String printTags(String[][] tvlData) {
        StringBuilder stb = new StringBuilder();
        for (String[] strings : tvlData) {
            String tag = tools.padText(getTagName(strings[0]), longest, PAD_RIGHT, ' ');
            stb.append(tag);
            stb.append(strings[1]).append("\n");
        }
        return stb.toString();
    }

    public int getLongest(ArrayList<String> list) {
        int size = list.get(0).replace("\n", "").length();
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).length() > size) {
                size = list.get(i).length();
                index = i;
            }
        }
        return size;
    }
}
