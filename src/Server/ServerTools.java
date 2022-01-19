/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import static Globals.Variables.*;

import Entidades.Decode63;
import Utils.ExtraTools;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

/**
 *
 * @author ctolo
 */
public class ServerTools {

    protected static byte[] TPDU = null;
    protected static String MTI = null;

    protected static ServerSocket server;
    protected static Socket socketClient;
    protected static DataInputStream requestClient;
    protected static DataOutputStream responseClient;
    protected static StringBuilder stbData = new StringBuilder();
    protected static String APPLICATION = "";
    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static String SIGN = "<";
    public static char SIGNC = '<';
    String[] files = {"iso87binary.xml", "genericpackager.dtd"};

    private String formatId(String idCampo) {
        String res = tools.padText("[" + idCampo + "]", 7, PAD_LEFT, ' ') + " ";
        return res;
    }

    private String getField63(String field63) {
        Decode63 decode = new Decode63();
        decode.setSubCampos(field63);
        StringBuilder Hex = new StringBuilder();
        decode.getDecoFields().forEach((sub) -> {
            String data
                    = formatId(sub.getIdCampo())
                    + sub.getNombreCampo()
                    + sub.getDataCampo();
            Hex.append(getField(data));
        });
        return Hex.toString();
    }

    protected String getField(String data) {
        return data + "\n";
    }

    protected static String getHeader(String hexString) {
        switch (hexString) {
            case "6000010000":
                return LEALTAD;
            case "6000000000":
                return BANCARD;
            case "6000020000":
            case "6000430000":
                return COBRANZAS;
            default:
                return null;
        }
    }

    protected ISOMsg byteToISOMsg(byte[] isoByte, int transCount) {
        ISOMsg m = new ISOMsg();
        try {
            String isoHex = ExtraTools.hexString(isoByte);
            TPDU = ExtraTools.hex2byte(isoHex.substring(4, 14));
            MTI = getMTI(isoHex.substring(14, 18));

            isoByte = ExtraTools.hex2byte(isoHex.substring(14, isoHex.length()));

            m.setPackager(new GenericPackager(getBinary()));
            m.unpack(isoByte);
            m.setHeader(TPDU);
            m.setMTI(isoHex.substring(14, 18));
            if (exTools.readXml(IS_DUMP)) {
                stbData.append(ExtraTools.hexdump(isoByte));
            }
            stbData.append(logISOMsg(m));
            stbData.deleteCharAt(stbData.lastIndexOf("\n"));
            stbData.append(tools.padText(SIGN, 30, PAD_RIGHT, SIGNC)).append("\n").append("\n");
            APPLICATION = getHeader(ExtraTools.hexString(m.getHeader()));
        } catch (ISOException ex) {
            tools.showDialogEx("ISOException", this, ex);
            m=null;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerTools.class.getName()).log(Level.SEVERE, null, ex);
            m=null;
        }
        return m;
    }

    private String getMTI(String substring) {
        Long mti = Long.parseLong(substring);
        mti += 10;
        return "0" + mti;
    }

    public InputStream getBinary() throws FileNotFoundException {
        File folder = exTools.getFolder(PACKAGER, false);
        final File f = new File(folder + "\\" + files[0]);
        InputStream ips = new FileInputStream(f);
        return ips;
    }

    protected String getData(String fileName) throws FileNotFoundException, IOException {
        InputStream is = ServerTools.class.getResourceAsStream(fileName);
        String content = new BufferedReader(new InputStreamReader(is))
                .lines().collect(Collectors.joining("\n"));
        return content;
    }

    public String ExportResource(String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder;
        try {
            stream = ServerTools.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if (stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = new File(ServerTools.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            resStreamOut = new FileOutputStream(jarFolder + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }

        return jarFolder + resourceName;
    }

    protected String logISOMsg(ISOMsg msg) {
        StringBuilder stb = new StringBuilder();
        stb.append("----ISO MESSAGE-----").append("\n");
        try {
            stb.append(tools.padText("TPDU:", 8, PAD_CENTER, ' ')).append(ExtraTools.hexString(TPDU)).append("\n");
            stb.append(tools.padText("MTI: ", 8, PAD_CENTER, ' ')).append(msg.getMTI()).append("\n");
            StringBuilder desFields = new StringBuilder();
            for (int i = 1; i <= msg.getMaxField(); i++) {
                if (msg.hasField(i)) {
                    String idCampo = tools.padText(i + "", 3, PAD_LEFT, '0');
                    String desCampo = tools.padText(msg.getPackager().getFieldDescription(msg.getComponent(i), i), 50, PAD_RIGHT, ' ');
                    stb.append(tools.padText(idCampo, 9, PAD_CENTER, ' ')).append(desCampo).append(" ").append("\"").append(msg.getString(i)).append("\"");
                    switch (i) {
                        case 55:
                            if (msg.getValue(55) != null) {
                                if (exTools.readXml(DECODE_55)) {
                                    desFields.append("\n\n");
                                    desFields.append(tools.padText("Field 55 by Tag: ", 60, PAD_RIGHT, ' ')).append("\n");
                                    String hexStr = exTools.hex2AsciiStr(msg.getString(55));
                                    desFields.append(getField55(hexStr));
                                }
                            }
                            break;
                        case 63:
                            if (exTools.readXml(DECODE_63)) {
                                desFields.append("\n\n");
                                desFields.append(tools.padText("Field 63 by Tag: ", 60, PAD_RIGHT, ' ')).append("\n");
                                desFields.append(getField63(msg.getString(63)));
                            }
                            break;
                        default:
                            break;
                    }
                    stb.append("\n");
                }
            }
            stb.append(desFields);
            stb.append("\n");
        } catch (ISOException e) {
            tools.showDialogEx("IsoException", this, e);
            return null;
        }
        return stb.toString();
    }

    public static String getTipoTrans() {
        String tipoTrans = MTI.equals("0210") ? "Venta" : (MTI.equals("0810") ? "Eco-Test" : "Reverso");
        return tipoTrans;
    }

    private String getField55(String value) {
        return deco55.decompile(value);
    }

    public byte[] getBytes(byte[] byteData) {
        String hexStr = ExtraTools.hexString(byteData);
        byte[] data = ExtraTools.hex2byte(hexStr);
        return data;
    }
}
