package Server;

import Implements.FechasImpl;
import Interfaces.Fechas;
import Transaccion.Transaccion;
import Transaccion.Transacciones;
import Utils.ExtraTools;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static Globals.Variables.*;

public class Script {

    static ExtraTools isoTools;
    static Fechas fecha;

    public static ISOMsg exec(ISOMsg inputISO) throws IOException, ISOException {
        fecha = new FechasImpl();
        String procCode = inputISO.getString(3) != null ? inputISO.getString(3) : "";
        String fileName = getFileName(procCode);
        String folder = exTools.getFile(fileName).getAbsolutePath();
        Map<String, ArrayList<ModelTrans>> campos = new Script().getModelo2Transaccion(folder);
        ISOMsg isoRespuesta = null;
        if (campos != null) {
            Transaccion transaccion = new Transacciones(fecha, campos);
            isoRespuesta = transaccion.exec(inputISO);
        }
        return isoRespuesta;
    }

    private static String getFileName(String proCode) {
        return switch (proCode) {
            case "820000" -> CONSULTA_SALDO;
            case "800000" -> VENTA;
            case "810000" -> VENTA_MANUAL;
            case "830000" -> ANULACION;
            case "000000" -> ECHO;
            default -> REVERSO;
        };
    }

    private Map<String, ArrayList<ModelTrans>> getModelo2Transaccion(String folder) {
        Map<String, ArrayList<ModelTrans>> res = new HashMap<>();
        ArrayList<ModelTrans> campos = new ArrayList<>();
        try {
            JsonParser ps = new JsonParser();
            String jsonContent = readFile(folder);
            if (jsonContent != null && !"".equals(jsonContent)) {
                JsonObject jsonObject = (JsonObject) ps.parse(jsonContent);
                JsonArray js = jsonObject.getAsJsonArray("trama");
                if (js != null) {
                    if (!js.isJsonNull()) {
                        for (Object obj : js) {
                            JsonObject campo = (JsonObject) obj;
                            Map<String, String> keys = new Gson().fromJson(
                                    campo, new TypeToken<HashMap<String, String>>() {
                                    }.getType()
                            );
                            keys.forEach((key, data) -> {
                                int id = Integer.parseInt(key);
                                campos.add(new ModelTrans(id, data));
                            });
                        }
                        res.put(NORMALES, new ArrayList<>(campos));
                        campos.clear();
                    }
                }
                js = jsonObject.getAsJsonArray("field63");
                if (js != null) {
                    if (!js.isJsonNull()) {
                        for (Object obj : js) {
                            JsonObject campo = (JsonObject) obj;
                            Map<String, String> keys = new Gson().fromJson(
                                    campo, new TypeToken<HashMap<String, String>>() {
                                    }.getType()
                            );
                            keys.forEach((key, data) -> {
                                int id = Integer.parseInt(key);
                                campos.add(new ModelTrans(id, data));
                            });
                        }
                        try {
                            fecha.exec();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        campos.add(new ModelTrans(92, fecha.getDateTime()));
                        res.put(CAMPO63, new ArrayList<>(campos));
                    }
                }

            } else {
                //loadInfo(fileName);
                return getModelo2Transaccion(folder);
            }
        } catch (IOException e) {
            tools.showDialogEx("IOException: ", this, e);
        }
        return res;
    }

    public String readFile(String folder) throws IOException {
        File file = new File(folder);
        return tools.readFile(file);
    }

    private void loadInfo(String fileName) {
        File newfile = exTools.getFile(fileName);
        try (PrintWriter out = new PrintWriter(newfile)) {
            out.println(getData(fileName));
        } catch (FileNotFoundException ex) {
            tools.showDialogEx("FileNotFoundException", new Init(), ex);
        } catch (IOException ex) {
            tools.showDialogEx("IOException", new Init(), ex);
        }
    }

    protected String getData(String fileName) throws FileNotFoundException, IOException {
        InputStream is = ServerTools.class.getResourceAsStream(fileName);
        String content = new BufferedReader(new InputStreamReader(is))
                .lines().collect(Collectors.joining("\n"));
        return content;
    }
}
