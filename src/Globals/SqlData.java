/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Globals;

import static Globals.Variables.*;
import Utils.ExtraTools;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author ctolo
 */
public class SqlData {

    public static final String TABLE_DROP = "DROP";

    public final static String CAMPOS_ISO_TABLE = "CamposIso";
    public final static String MTI = "mti";
    public final static String NRO_CAMPO = "nroCampo";
    public final static String DATA = "data";
    public final static String IS_RETURN = "isReturn";
    public final static String TPU_CODE = "tpuCode";

    public final static String CUENTAS_TABLE = "Cuentas";
    public final static String TRACK = "trackTarjeta";
    public final static String NAME_CARD = "nameCard";
    public final static String NAME_CLIENTE = "nameCliente";
    public final static String ISSUER_ID = "issuerId";
    public final static String TIPO = "tipo";
    public final static String MONTO_MAX = "montoMax";
    public final static String ACTIVE = "active";
    public final static String PUNTOS = "puntos";

    public final static String ERRORES_TABLE = "Errores";
    public final static String PLANTILLA_ID = "plantillaid";
    public final static String CODIGO = "codigo";
    public final static String DESCRIPCION = "descripcion";
    public final static String TIPO_ERR = "tipo";
    public final static String ACTIVAR = "activar";
    public final static String APP = "app";

    public static final String TAGS_TABLE = "EmvTags";
    public static final String TAG = "tag";
    public static final String NAME = "name";
    public static final String DES = "description";

    public final static String[] LISTADO_SQL_CAMPOS = new String[]{MTI, NRO_CAMPO, DATA, IS_RETURN, TPU_CODE};
    public final static String[] LISTADO_SQL_CUENTAS = new String[]{TRACK, NAME_CARD, NAME_CLIENTE, ISSUER_ID, TIPO, MONTO_MAX, ACTIVE, PUNTOS};
    public final static String[] LISTADO_SQL_ERRORES = new String[]{PLANTILLA_ID, CODIGO, DESCRIPCION, TIPO_ERR, ACTIVAR, APP};
    public static final String[] LISTADO_SQL_TAGS = {TAG, NAME, DES};
    public static final String[] LISTADO_DB = {CAMPOS_ISO_TABLE, CUENTAS_TABLE, ERRORES_TABLE, TAGS_TABLE};

    public String getCreateTables(String tableName) {
        switch (tableName) {
            case CAMPOS_ISO_TABLE:
                return getCamposTable();
            case CUENTAS_TABLE:
                return getCUENTAS_TABLE();
            case ERRORES_TABLE:
                return getERRORES_TABLE();
            case TAGS_TABLE:
                return getTAGS_TABLE();
            default:
                return null;
        }
    }

    private String getCamposTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + CAMPOS_ISO_TABLE + " ("
                + MTI + " TEXT NOT NULL,"
                + NRO_CAMPO + " TEXT NOT NULL,"
                + DATA + " TEXT,"
                + IS_RETURN + " TEXT NOT NULL, "
                + TPU_CODE + " TEXT)";
        return sql;
    }

    private String getCUENTAS_TABLE() {
        String sql = "CREATE TABLE IF NOT EXISTS " + CUENTAS_TABLE + " ("
                + TRACK + " TEXT,"
                + NAME_CARD + " TEXT,"
                + NAME_CLIENTE + " TEXT,	"
                + ISSUER_ID + " TEXT,"
                + TIPO + " TEXT,"
                + MONTO_MAX + " TEXT,	"
                + ACTIVE + " TEXT,"
                + PUNTOS + " TEXT,"
                + "PRIMARY KEY(" + TRACK + "))\n";
        return sql;
    }

    private String getERRORES_TABLE() {
        String sql = "CREATE TABLE IF NOT EXISTS " + ERRORES_TABLE + " ("
                + PLANTILLA_ID + " TEXT,"
                + CODIGO + " TEXT,"
                + DESCRIPCION + " TEXT,	"
                + TIPO_ERR + " TEXT,"
                + ACTIVAR + " TEXT,"
                + APP + " TEXT)";
        return sql;
    }

    public String getTAGS_TABLE() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TAGS_TABLE + " ("
                + TAG + " TEXT,"
                + NAME + " TEXT,"
                + DES + " TEXT)";
        return sql;
    }

    public String getDropTables(String tableName) {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        return sql;
    }

    public String getInsertTables(String table) {
        String fileName = null;
        switch (table) {
            case ERRORES_TABLE:
                fileName = "insertErrores";
                break;
            case CAMPOS_ISO_TABLE:
                fileName = "insertCampos";
                break;
            case TAGS_TABLE:
                fileName = "insertTags";
            default:
                break;
        }
        InputStream fis = ExtraTools.class.getResourceAsStream(fileName + ".sql");
        BufferedInputStream bis = new BufferedInputStream(fis);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = 0;
        try {
            result = bis.read();
            while (result != -1) {
                byte b = (byte) result;
                buf.write(b);
                result = bis.read();
            }
        } catch (IOException ex) {
            tools.showDialogEx("IOException  ", new SqlData(), ex);
        }
        return buf.toString();
    }
}
