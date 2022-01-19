package Entidades;

import static Globals.Variables.*;
import java.io.BufferedReader;
import java.io.FileReader;
import com.google.gson.JsonElement;
import java.util.Map;
import java.util.Set;
import com.google.gson.JsonArray;
import java.io.IOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.util.ArrayList;

public class SubCampo {

    String idCampo;
    String nombre;
    SubCampo subCampo;
    ArrayList<SubCampo> subCampos;
    private File file;
    static String data = "";

    public SubCampo() {
        subCampos = new ArrayList<>();
    }

    public SubCampo(String idCampo, String nombre) {
        subCampos = new ArrayList<>();
        this.idCampo = idCampo;
        this.nombre = nombre;
    }

    public String getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(String idCampo) {
        this.idCampo = idCampo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void cargarSubCampos() {
        scriptLogs("Llegada a cargarSubCampos");
        file = exTools.getFile(SUB_CAMPOS);
        try {
            JsonParser ps = new JsonParser();
            String jsonContent = readFile();
            if (jsonContent != null && !"".equals(jsonContent)) {
                JsonObject jsonObject = (JsonObject) ps.parse(jsonContent);
                JsonArray js = jsonObject.getAsJsonArray(SUB_CAMPO);
                for (Object obj : js) {
                    subCampo = new SubCampo();
                    JsonObject campo = (JsonObject) obj;
                    Set<Map.Entry<String, JsonElement>> keys = campo.entrySet();
                    keys.forEach((key) -> {
                        data = String.valueOf(campo.get(key.getKey())).replace("\"", "");
                        subCampo.setApp(key.getKey(), data);
                    });
                    subCampos.add(subCampo);
                }
            }
        } catch (IOException e) {
            tools.showDialogEx("IOException", this, e);
        }
    }

    public String readFile() throws IOException {
        if (!file.exists()) {
            exTools.getFile(SUB_CAMPOS);
            return "";
        }
        FileReader fileReader = new FileReader(file.getAbsoluteFile());
        BufferedReader reader = new BufferedReader(fileReader);
        String content = "";
        String line;
        while ((line = reader.readLine()) != null) {
            content += line;
        }
        return content;
    }

    public void scriptLogs(String data) {
        tools.scriptLogs(this, data);
    }

    private void setApp(String key, String data) {
        switch (key) {
            case SUB_CAMPO_ID:
                setIdCampo(data);
                break;
            case SUB_CAMPO_NAME:
                setNombre(data);
                break;
        }
    }

    public ArrayList<SubCampo> getSubCampos() {
        return subCampos;
    }
}
