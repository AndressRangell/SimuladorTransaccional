package Entidades;

import static Globals.Variables.*;
import static Globals.Variables.subFields;

import java.util.*;
import java.util.stream.Collectors;

public class Campo63 {

    private int indice = 0;
    private int longest = 0;

    ArrayList<SubCampo> campos;

    public Campo63() {
    }

    public void setSubCampos(String campo63) {
        SubCampo subCampo = new SubCampo();
        subCampo.cargarSubCampos();
        campos = subCampo.getSubCampos();
        longest = getLongest();
        indice = 0;
        for (int i = 0; i < campo63.length(); i = indice) {
            String lenCampo = campo63.substring(indice, indice += 4);
            String idCampo = exTools.hex2AsciiStr(campo63.substring(indice, indice + 4));
            String dataCampo = obtenerCampo(campo63);
            subFields.add(new SubField(idCampo, getFieldName(idCampo), dataCampo));
            indice += Integer.parseInt(lenCampo) * 2;
        }
    }

    private String getFieldName(String idCampo) {
        final String[] fName = {"Unknown"};
        campos.stream().filter(campo -> campo.getIdCampo().equals(idCampo)).forEach(campo -> fName[0] = campo.getNombre());
        String nombreCampo = tools.padText(fName[0], 40, PAD_RIGHT, ' ');
        return tools.padText(nombreCampo, (longest + 3), PAD_RIGHT, ' ');
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

    public int getLongest() {
        String longestStr = campos.stream()
                .map(SubCampo::getIdCampo).toList().stream()
                .max(Comparator.comparing(String::length))
                .orElse(null);
        return longestStr.length();

    }

    public ArrayList<SubField> getSubFields() {
        return subFields;
    }

    public void addOrUpdate(int id, String data) {
        String idCampo = String.valueOf(id);
        boolean contains = subFields.stream()
                .map(SubField::getIdCampo).toList().contains(idCampo);
        if (!contains) {
            subFields.add(new SubField(idCampo, getFieldName(idCampo), data));
        } else {
            subFields.stream().filter(subField -> subField.getIdCampo().equals(idCampo)).forEach(subField -> subField.setDataCampo(data));
        }

    }

    public String getField(int idCampo) {
        String id = String.valueOf(idCampo);
        final String[] data = {null};
        subFields.stream().filter(subField -> subField.getIdCampo().equals(id)).forEach(subField -> data[0] = subField.getDataCampo());
        return (data[0] != null) ? data[0] : null;
    }

    public void remove(int id) {
        String idField = String.valueOf(id);
        subFields.remove(idField);
    }
}
