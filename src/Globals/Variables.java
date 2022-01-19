/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Globals;

import Entidades.*;
import Implements.ToolsImpl;
import Interfaces.Tools;
import Utils.ExtraTools;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * @author ctolo
 */
public class Variables {
    //============TIPOS=============== //
    public static final String NORMALES = "normales";
    public static final String CAMPO63 = "campo63";

    //=============APPS==============//
    public static final String BANCARD = "Bancard";
    public static final String LEALTAD = "Lealtad";
    public static final String COBRANZAS = "Cobranzas";
    public static final String ALL = "All";

    public static Tools tools = new ToolsImpl();
    public static ExtraTools exTools = new ExtraTools();
    public static Faker rData = new Faker(new Locale("es-MX"));

    public static SqlData sqlDat = new SqlData();
    public static Decode55 deco55 = new Decode55();
    public static ArrayList<SubCampoModel> sbCampos = new ArrayList<>();
    public static final Campo63 cp63=new Campo63();
    public static ArrayList<SubField> subFields = new ArrayList<>();
    public static ArrayList<String> fielNames = new ArrayList<>();

    public static final int PAD_CENTER = 0;
    public static final int PAD_LEFT = 1;
    public static final int PAD_RIGHT = 2;
    public static final int INFO_DIALOG = 0;
    public static final int CONFIRM_DIALOG = 1;
    public static String FOLDER = getFolder();

    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static final String REQUEST_PIN = "¿Ingresa un pin por Favor?";

    public static String SIMU_TIT = "configuracion";
    public static boolean IS_CONSOLE = false;
    public static String IS_CONFIRM = "IsConfirm";
    public static String IS_SOUND = "IsSound";
    public static String DECODE_63 = "Decode63";
    public static String DECODE_55 = "Decode55";
    public static String IS_ECO_REV = "Mensaje_eco_reverso";
    public static String IS_BORRAR = "IsBorrar";
    public static String IS_MANUAL_ADD = "ManualAdd";
    public static String IS_DUMP = "IsDump";

    public static final String SIMU_CONF = "SimuConf.xml";
    public static final String TRACK_CONFIG = "TrackConfig.json";
    public static final String SUB_CAMPOS = "SubCampos.json";
    public static final String DATABASE_NAME = "sistema.db";
    public static final String LOGS_TRAMAS = "LogsTramas";
    public static final String LOGS_TRANS = "LogsTransacciones";
    public static final String CONSULTA_SALDO = "ConsultaSaldo.json";
    public static final String VENTA = "Venta.json";
    public static final String VENTA_MANUAL = "VentaManual.json";
    public static final String ANULACION = "Anulacion.json";
    public static final String ECHO = "Echo.json";
    public static final String REVERSO = "Reverso.json";

    public static final String PACKAGER = "Packager";
    public static final String ISO_DATA = "iso87binary.xml";
    public static final String ISO_PACK = "genericpackager.dtd";
    public static String SUB_CAMPO = "SubCampos";

    public static final String SUB_CAMPO_ID = "id";
    public static final String SUB_CAMPO_NAME = "nombre";

    public static StringBuilder JsonSubCampos = new StringBuilder();
    public static ArrayList<String> opcionesXml = new ArrayList<>();
    public static ArrayList<SubFieldModel> subFieldsModel = new ArrayList<>();

    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String getSubCamposJson() {
        JsonArray array = new JsonArray();
        String[][] sub = getSubcampos();
        for (String[] subcampo : sub) {
            JsonObject obj = new JsonObject();
            obj.addProperty(SUB_CAMPO_ID, subcampo[0]);
            obj.addProperty(SUB_CAMPO_NAME, subcampo[1]);
            if (!array.contains(obj)) {
                array.add(obj);
            }
        }
        JsonObject subcampos = new JsonObject();
        subcampos.add(SUB_CAMPO, array);
        String prettyJsonString = gson.toJson(subcampos);
        return prettyJsonString;
    }

    public static ArrayList<String> getSimuConf() {
        opcionesXml.clear();
        opcionesXml.add(IS_ECO_REV);
        opcionesXml.add(DECODE_63);
        opcionesXml.add(DECODE_55);
        opcionesXml.add(IS_BORRAR);
        opcionesXml.add(IS_MANUAL_ADD);
        opcionesXml.add(IS_SOUND);
        opcionesXml.add(IS_CONFIRM);
        opcionesXml.add(IS_DUMP);
        return opcionesXml;
    }

    public static String getUrl(String filePath) {
        return "jdbc:sqlite:" + filePath;
    }

    private static String getFolder() {
        try {
            File f = new File(Variables.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            return f.getPath();
        } catch (URISyntaxException | IllegalArgumentException ex) {
            System.out.println("URISyntaxException | IllegalArgumentException " + ex.getMessage());
        }
        return System.getProperty("user.dir");
    }

    private static String[][] getSubcampos() {
        return new String[][]{
            {"01", "PIN playero"},
            {"02", "Kilometraje"},
            {"03", "Código producto 1"},
            {"04", "Cantidad producto 1"},
            {"05", "Código producto 2"},
            {"06", "Cantidad producto 2"},
            {"07", "PIN supervisor"},
            {"08", "Fecha transacción"},
            {"09", "Nro. Comprobante"},
            {"10", "nombre del producto 1"},
            {"11", "Nombre del producto 2"},
            {"13", "Fallback"},
            {"14", "Tipo Plan"},
            {"15", "Cuenta/Entidad"},
            {"16", "Código de Operación"},
            {"17", "Identificador del Producto"},
            {"18", "Descripcion del servicio"},
            {"19", "nombre del servicio - ticket"},
            {"22", "Mensaje del display"},
            {"23", "datos extra del pago de servicio"},
            {"24", "Flag Caja POS"},
            {"25", "monto 1  transaccion - descuento"},
            {"26", "Indicador de ingreso manual o lectura de NFC en billeteras"},
            {"27", "HST_API"},
            {"28", "Mensajes de credito de puntos"},
            {"29", "Mensaje al pie del ticket"},
            {"30", "Mensaje para seleccion de cuenta"},
            {"31", "desencripcion por hardware"},
            {"32", "KSN DATA"},
            {"33", "KSN DUKPT"},
            {"34", "Devices \"Miura\"o \"Walker"},
            {"35", "Tracks encriptados"},
            {"36", "Puntos generados"},
            {"37", "Saldo de puntos"},
            {"38", "Nombre del punto"},
            {"39", "No definido"},
            {"40", "No definido"},
            {"41", "Monto Cash Back"},
            {"42", "No definido"},
            {"43", "No definido"},
            {"44", "No definido"},
            {"45", "Cuotas"},
            {"46", "Error en el Chip"},
            {"47", "Codigo Operador Telefonico"},
            {"46", "No definido"},
            {"47", "No definido"},
            {"48", "No definido"},
            {"49", "No definido"},
            {"50", "No definido"},
            {"51", "No definido"},
            {"52", "No definido"},
            {"53", "LEALTAD: importe del Vale de descuento"},
            {"70", "Serie del Firmware del POS"},
            {"71", "Tipo Transaccion"},
            {"81", "Numeros de serie de dispositivos"},
            {"82", "Datos de Servicio"},
            {"85", "Cabecera del Ticket"},
            {"86", "Preguntas de los datos adicionales"},
            {"87", "Nombre de la tarjeta"},
            {"88", "Cantidad de tickets"},
            {"89", "IssuerId para Caja POS"},
            {"90", "Version del Aplicativo"},
            {"91", "disponible solo al primer ticket"},
            {"92", "Date and time Stapm"},
            {"93", "Nro de Documento"},
            {"94", "DIRECCION IP"},
            {"95", "IMEI"},
            {"96", "PIN DEL SUPERVISOR Flota"},
            {"97", "Pin 1 del cliente"},
            {"98", "pin 2 del cliente"}
        };
    }

    public static void setSubTrans() {
        if (subFieldsModel != null) {
            subFieldsModel.clear();
        } else {
            subFieldsModel = new ArrayList<>();
        }

        subFieldsModel.add(new SubFieldModel("0400", "", new int[]{81, 89, 92, 22}));
        subFieldsModel.add(new SubFieldModel("0800", "", new int[]{0}));
        subFieldsModel.add(new SubFieldModel("0200", "000000", new int[]{22, 29, 81, 88, 89, 92, 30}));
        subFieldsModel.add(new SubFieldModel("0200", "090000", new int[]{22, 29, 81, 88, 89, 92, 30}));
        subFieldsModel.add(new SubFieldModel("0200", "600000", new int[]{22, 24, 29, 81, 86, 88, 89, 92}));
        subFieldsModel.add(new SubFieldModel("0200", "6000002", new int[]{22, 24, 29, 81, 88, 89}));
        subFieldsModel.add(new SubFieldModel("0200", "700000", new int[]{22, 29, 53, 81, 85, 90, 92, 94, 95}));
        subFieldsModel.add(new SubFieldModel("0200", "730000", new int[]{22, 28, 29, 81, 88, 89, 92}));
        subFieldsModel.add(new SubFieldModel("0200", "720000", new int[]{38, 81, 88, 90, 92}));
        subFieldsModel.add(new SubFieldModel("0200", "7200002", new int[]{15, 16, 17, 18, 19, 22, 23, 81, 86, 92}));
        subFieldsModel.add(new SubFieldModel("0200", "310000", new int[]{22, 81, 92}));
        subFieldsModel.add(new SubFieldModel("0200", "920000", new int[]{22, 24, 81, 89, 92, 30}));
        subFieldsModel.add(new SubFieldModel("0200", "8200002", new int[]{22, 28, 29, 81, 85, 87, 88, 89}));
    }

    public static ArrayList<SubFieldModel> getSubTrans() {
        return subFieldsModel;
    }
}
