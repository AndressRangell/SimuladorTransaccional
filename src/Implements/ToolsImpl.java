/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implements;

import Interfaces.Dialog;
import Interfaces.Logger;
import Interfaces.Tools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import static Globals.Variables.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ToolsImpl implements Tools {

    @Override
    public String padText(String s, int size, int type, char c) {
        String result;
        switch (type) {
            case PAD_CENTER:
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < (size - s.length()) / 2; i++) {
                    sb.append(c);
                }
                sb.append(s);
                while (sb.length() < size) {
                    sb.append(c);
                }
                result = sb.toString();
                break;
            case PAD_LEFT:
                result = String.format("%" + size + "s", s).replace(' ', c);
                break;
            case PAD_RIGHT:
                result = String.format("%" + (-size) + "s", s).replace(' ', c);
                break;
            default:
                result = s;
                break;
        }
        return result;
    }

    @Override
    public boolean readXml(File xmlFile, String option) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
            String data = doc.getElementsByTagName(option).item(0).getTextContent();
            return Boolean.valueOf(data);
        } catch (IOException | NullPointerException | ParserConfigurationException | SAXException ex) {
            showDialogEx("IOException | NullPointerException | ParserConfigurationException | SAXException ", this, ex);
        }

        return false;
    }

    @Override
    public String getBackPath(String newFolder) {

        String newPath;
        int cont = 0;
        StringBuilder st = new StringBuilder(FOLDER).reverse();
        for (int i = 0; i < FOLDER.length(); i++) {
            if (st.toString().charAt(i) != File.separatorChar) {
                cont++;
            } else {
                break;
            }
        }
        newPath = st.reverse().toString().substring(0, FOLDER.length() - cont);
        newPath = newPath + newFolder;
        return newPath;
    }

    @Override
    public void scriptLogs(Object objClass, String data) {
        String classe = objClass.getClass().getSimpleName();
        Logger log = new LoggerImpl(new FechasImpl());
        log.write(classe, data);
    }

    @Override
    public boolean deleteDirectory(File directory) {
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directory.delete();
    }

    @Override
    public void showDialog(String message, Object objClass, int Type) {
        String classe = objClass.getClass().getSimpleName();
        Dialog dialog = new DialogImpl();
        switch (Type) {
            case INFO_DIALOG:
                dialog.showInfo(message, classe);
                break;
            case CONFIRM_DIALOG:
                dialog.showConfirmacion(message);
                break;
            default:
                break;
        }
    }

    @Override
    public String showInput(String message) {
        Dialog dialog = new DialogImpl();
        String res = dialog.showInput(message);
        if (res != null && !res.equals("")) {
            return res;
        } else {
            return " ";
        }
    }

    @Override
    public void showDialogEx(String message, Object objClass, Exception ex) {
        String classe = objClass.getClass().getSimpleName();
        Dialog dialog = new DialogImpl();
        dialog.showError(message, ex, classe);
    }

    @Override
    public String readFile(File file) {
        try {
            FileReader fileReader;
            if (!file.exists()) {
                return "";
            } else {
                fileReader = new FileReader(file.getAbsoluteFile());
                BufferedReader reader = new BufferedReader(fileReader);
                String content = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    content = content + line;
                }
                return content;
            }
        } catch (IOException ex) {
            showDialogEx("IOException | NullPointerException ", this, ex);
            return null;
        }
    }

    @Override
    public int getTimeTrans(Date fecIni, Date fecFin, TimeUnit unit) {
        long diff = fecFin.getTime() - fecIni.getTime();
        long diffrence = unit.convert(diff, TimeUnit.MILLISECONDS);
        int res = Integer.parseInt(String.valueOf(diffrence));
        return res;
    }

}
