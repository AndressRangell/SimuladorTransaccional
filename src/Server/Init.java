/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static Globals.Variables.*;
import Implements.ToolsImpl;
import Utils.ExtraTools;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

/**
 *
 * @author ctolo
 */
public class Init extends ServerTools {

    String tempIsoHex;
    String isoHex;
    ISOMsg isoRta;
    int transCount = 0;
    Date fecIni;
    Date fecFin;

    public static void start(int port) {
        Init init = new Init();
        init.initServer(port);
    }

    private void initServer(int port) {
        System.out.println("# Iniciando server... " + port);
        try {
            server = new ServerSocket(port);
            while (true) {
                socketClient = server.accept();
                fecIni = new Date();
                transCount++;
                requestClient = new DataInputStream(socketClient.getInputStream());
                responseClient = new DataOutputStream(socketClient.getOutputStream());
                unpackedIso();
                if (isoRta != null) {
                    socketClient.close();
                }
            }
        } catch (IOException | ISOException | NullPointerException ex) {
            ex.printStackTrace();
            new ToolsImpl().showDialogEx("IOException | ISOException | NullPointerException", new Init(), ex);
            System.exit(0);
        }
    }

    public void unpackedIso() throws ISOException, IOException {
        clearAll();
        loadPackager();
        setSubTrans();
        byte[] result = exTools.readBytesRequest(requestClient);
        ISOMsg isom = byteToISOMsg(result, transCount);
        if (isom != null) {
            isoRta = Script.exec(isom);
            if (isoRta != null) {
                isoRta.setPackager(new GenericPackager(getBinary()));
                isoRta.setMTI(MTI);
                isoRta.setHeader(TPDU);
                tempIsoHex = ExtraTools.hexString(TPDU) + ExtraTools.hexString(isoRta.pack());
                isoHex = "1234" + tempIsoHex.substring(0, tempIsoHex.length());
                byte[] resp = ExtraTools.hex2byte(isoHex);
                if (exTools.readXml(IS_DUMP)) {
                    stbData.append(ExtraTools.hexdump(resp));
                }
                stbData.append(logISOMsg(isoRta));
                stbData.deleteCharAt(stbData.lastIndexOf("\n"));
                stbData.append(tools.padText(SIGN, 30, PAD_RIGHT, SIGNC)).append("\n");
                dataAditional();
                scriptTrans(stbData.toString());
                responseClient.write(resp);
            }
        }
    }

    private void scriptTrans(String data) {
        System.out.println(data.replaceAll(SIGN, ""));
    }

    public void loadPackager() {
        for (String file : files) {
            File newfile = exTools.getFile(file);
            try (PrintWriter out = new PrintWriter(newfile)) {
                out.println(getData(file));
            } catch (FileNotFoundException ex) {
                tools.showDialogEx("FileNotFoundException", new Init(), ex);
            } catch (IOException ex) {
                tools.showDialogEx("IOException", new Init(), ex);
            }
        }

    }

    private void clearAll() {
        stbData = new StringBuilder();
        if (exTools.readXml(IS_BORRAR)) {
            exTools.deleted();
        }
    }

    private void dataAditional() {
        fecFin = new Date();
        stbData.append(tools.padText(SIGN, 30, PAD_RIGHT, SIGNC)).append("\n");
        stbData.append("Transaccion recibida desde el POS [").append(APPLICATION).append("]: ").append(df.format(fecIni)).append("\n");
        stbData.append("Transaccion enviada al POS [").append(APPLICATION).append("]: ").append(df.format(fecFin)).append("\n");
        String time = String.valueOf(tools.getTimeTrans(fecIni, fecFin, TimeUnit.MILLISECONDS));
        stbData.append("Tiempo de respuesta POS [").append(APPLICATION).append("]: ").append(time).append(" Milisegundos").append("\n");
        stbData.append(tools.padText(SIGN, 120, PAD_RIGHT, SIGNC)).append("\n");
    }

}
