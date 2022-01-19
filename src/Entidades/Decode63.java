package Entidades;

import static Globals.Variables.*;
import java.util.ArrayList;

public class Decode63 {

    ArrayList<SubCampo> campos;

    private int indice = 0;
    private int longest = 0;
    ArrayList<String> fielName = new ArrayList<>();
    ArrayList<SubField> decoFields = new ArrayList<>();

    public Decode63() {
    }

    public void setSubCampos(String campo63) {
        SubCampo subCampo = new SubCampo();
        subCampo.cargarSubCampos();
        campos = subCampo.getSubCampos();
        campos.forEach((campo) -> {
            fielName.add(campo.getNombre());
        });
        longest = getLongest(fielName);
        indice = 0;
        sbCampos = new ArrayList<>();
        for (int i = 0; i < campo63.length(); i = indice) {
            String lenCampo = campo63.substring(indice, indice += 4);
            String idCampo = exTools.hex2AsciiStr(campo63.substring(indice, indice + 4));
            String dataCampo = obtenerCampo(campo63);
            decoFields.add(new SubField(idCampo, getFieldName(idCampo), dataCampo));
            indice += Integer.parseInt(lenCampo) * 2;

        }
    }

    private String getFieldName(String idCampo) {
        int position = 0;
        if (getPostion(idCampo) > 1) {
            position = getPostion(idCampo) - 1;
        }
        String nombreCampo = tools.padText(campos.get(position).getNombre(), 40, PAD_RIGHT, ' ');
        String res = tools.padText(nombreCampo, (longest + 3), PAD_RIGHT, ' ');
        return res;
    }

    private String obtenerCampo(String campo63) {
        if (!campo63.equals("")) {
            try {
                int lenCampo = Integer.parseInt(campo63.substring(indice - 4, indice)) * 2;
                int lenReal = (indice) + lenCampo;

                if (campo63.length() < ((indice) + lenCampo)) {
                    lenReal = campo63.length();
                }
                String data = campo63.substring(indice + 4, lenReal);
                return exTools.hex2AsciiStr(data);
            } catch (NumberFormatException ex) {
                tools.showDialogEx("NumberFormatException", this, ex);
                return null;
            }
        } else {
            return null;
        }
    }

    private int getPostion(String idCampo) {
        for (SubCampo campo : campos) {
            if (campo.getIdCampo().equals(idCampo)) {
                return campos.indexOf(campo) + 1;
            }
        }
        return 0;
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

    public ArrayList<SubField> getDecoFields() {
        return decoFields;
    }
}
