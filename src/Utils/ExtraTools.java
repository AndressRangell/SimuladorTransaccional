package Utils;

import Implements.dbHelperImpl;
import static Globals.Variables.*;
import static Globals.SqlData.*;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.jpos.iso.ISOException;
import org.w3c.dom.Document;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jpos.iso.ISOUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import Interfaces.DbHelper;

public class ExtraTools extends ISOUtil {

    static Document doc = null;

    public String hex2AsciiStr(final String hex) {
        final StringBuilder sb = new StringBuilder();
        final StringBuilder temp = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            final String output = hex.substring(i, i + 2);
            final int decimal = Integer.parseInt(output, 16);
            sb.append((char) decimal);
            temp.append(decimal);
        }
        return sb.toString();
    }

    public String ascci2hex(String ascci) {
        StringBuilder hex = new StringBuilder();
        for (char temp : ascci.toCharArray()) {
            int decimal = (int) temp;
            hex.append(Integer.toHexString(decimal));
        }
        return hex.toString();
    }

    private void createFile(File file, String fileName) {
        try {
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            switch (fileName) {
                case SIMU_CONF:
                    createXml(file);
                default:
                    file.createNewFile();
                    writeFile(fileName, file);
                    break;
            }
        } catch (IOException ex) {
            tools.showDialogEx("IOException", getClasse(), ex);
        }
    }

    public boolean readXml(String opcion) {
        scLogs("Llegada a readXml");
        try {
            final File file = getFile(SIMU_CONF);
            return tools.readXml(file, opcion);
        } catch (NullPointerException ex) {
            tools.showDialogEx("NullPointerException", getClasse(), ex);
        }

        return false;
    }

    public String getFilePath(String fileName) {
        String filePath = null;
        String parent;
        switch (fileName) {
            case SIMU_CONF:
            case TRACK_CONFIG:
            case SUB_CAMPOS:
            case CONSULTA_SALDO:
            case VENTA:
            case VENTA_MANUAL:
            case ANULACION:
            case REVERSO:
            case ECHO:
                parent = "..\\..\\Transacciones";
                break;
            case DATABASE_NAME:
                parent = "..\\Database";
                break;
            case LOGS_TRAMAS:
            case LOGS_TRANS:
            case PACKAGER:
                parent = "..\\";
                break;
            case ISO_DATA:
            case ISO_PACK:
                parent = "..\\" + PACKAGER;
                break;
            default:
                parent = null;
                break;
        }
        if (parent != null) {
            filePath = tools.getBackPath(parent) + "\\" + fileName;
        }
        return filePath;
    }

    public static String getField(int idFl, String data) throws ISOException {
        String idFld = String.valueOf(idFl);
        String id = ISOUtil.padleft(idFld, 2, '0');
        int length = converToString(data).length() / 2;
        String lengthReal = String.valueOf(length + 2);
        return ISOUtil.padleft(lengthReal, 4, '0') + converToString(id) + converToString(data);
    }
    public static byte[] getAsByteAr(String data) {
        return ISOUtil.hex2byte(data);
    }

    private static String converToString(String data) {
        StringBuilder builder = new StringBuilder();
        for (char dta : data.toCharArray()) {
            String hexString = Integer.toHexString(dta);
            builder.append(hexString);
        }
        return builder.toString();
    }
    public void scLogs(String mensaje) {
        tools.scriptLogs(getClasse(), mensaje);
    }

    private Object getClasse() {
        return new ExtraTools();
    }

    public void deleted() {
        scLogs("Llegada a delete");
        String filePath = null;
        String[] folders = {LOGS_TRANS, LOGS_TRAMAS};
        for (String fol : folders) {
            filePath = getFilePath(fol);
            File f = new File(filePath);
            if (deleteDirectory(f)) {
                scLogs("Directory " + fol + " Deleted Succesfully");
            } else {
                scLogs("Directory " + fol + " Not Deleted");
            }
        }
        dropDatabase();
        modifyXml(IS_BORRAR, false);
    }

    public boolean deleteDirectory(File directory) {
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directory.delete();
    }

    private void modifyXml(String variable, boolean newValue) {
        try {
            File file = new File(getFilePath(SIMU_CONF));
            scLogs("Llegada a modifyXml");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document xmlDoc = docBuilder.parse(file);
            xmlDoc.getDocumentElement().normalize();
            Node node = xmlDoc.getElementsByTagName(variable).item(0);
            String value = String.valueOf(newValue);
            node.setTextContent(value);
            beautifyXML(xmlDoc, file);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            tools.showDialogEx("ParserConfigurationException | SAXException | IOException | TransformerException", getClasse(), ex);
        }
    }

    public File getFile(String fileName) {
        scLogs("Llegada a getFile");
        File file = new File(getFilePath(fileName));
        if (!file.exists()) {
            createFile(file, fileName);
        }
        return file;
    }

    public File getFolder(String fileName, boolean isLog) {
        if (isLog) {
            scLogs("Llegada a getFolder");
        }
        File file = new File(getFilePath(fileName));
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    private void writeFile(String fileName, File file) {
        String data = null;
        switch (fileName) {
            case SUB_CAMPOS:
                data = getSubCamposJson();
                break;
            default:
                break;
        }
        if (data != null) {
            try {
                final FileWriter fw = new FileWriter(file);
                try (BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.write(data);
                    bw.newLine();
                }
            } catch (IOException | NullPointerException ex) {
                tools.showDialogEx("IOException | NullPointerException ", getClasse(), ex);
            }
        }
    }

    private void createXml(File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document xmlDoc = docBuilder.newDocument();
            Element rootElement = xmlDoc.createElement(SIMU_TIT);
            xmlDoc.appendChild(rootElement);
            getSimuConf().forEach((campo) -> {
                addElement(xmlDoc, campo, "false");
            });
            beautifyXML(xmlDoc, file);
        } catch (ParserConfigurationException | IOException ex) {
            tools.showDialogEx("IOException | NullPointerException ", getClasse(), ex);
        }
    }

    private void beautifyXML(Document doc, File file) {
        scLogs("Llegada a beautifyXML");
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            tools.showDialogEx("IOException | NullPointerException ", getClasse(), ex);
        }
    }

    private void addElement(Document doc, String campo, String value) {
        Element root = doc.getDocumentElement();
        Element cd = doc.createElement(campo);
        cd.setTextContent(value);
        root.appendChild(cd);
    }

    private void dropDatabase() {
        DbHelper db = new dbHelperImpl();
        db.setParent(this);
        db.connect();
        db.createTable(TABLE_DROP);
        db.close();
        db.closeSt();
    }

    public byte[] readBytesRequest(DataInputStream dataClient) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            baos.write(buffer, 0, dataClient.read(buffer));
            return baos.toByteArray();
        } catch (IOException ex) {
            tools.showDialogEx("ISOException", this, ex);
            return null;
        }
    }
}
