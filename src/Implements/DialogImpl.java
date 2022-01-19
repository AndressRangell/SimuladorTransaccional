/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implements;

import Interfaces.Dialog;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import static Globals.Variables.*;

/**
 *
 * @author ctolo
 */
public class DialogImpl implements Dialog {

    @Override
    public void showError(String titulo, Exception error, String clase) {
        StringBuilder mensaje = new StringBuilder();
        StackTraceElement[] strArray = error.getStackTrace();
        mensaje.append(getError(error)).append("\n");
        for (StackTraceElement str : strArray) {
            mensaje.append("CLASE --> ").append(str.getClassName())
                    .append(" METODO  --> ").append(str.getMethodName())
                    .append(" LINEA CODIGO --> ").append(str.getLineNumber()).append("\n");
        }
        tools.scriptLogs(clase, titulo + "  >> " + mensaje.toString());
        showDialog(mensaje.toString(), titulo + clase, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showInfo(String mensaje, String clase) {
        showDialog(mensaje, "Info.. " + clase, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public int showConfirmacion(String mensaje) {
        return JOptionPane.showConfirmDialog(null, mensaje);
    }

    @Override
    public String showInput(String mensaje) {
        return JOptionPane.showInputDialog(mensaje);
    }

    private void showDialog(String msg, String title, int type) {
        if (exTools.readXml(IS_SOUND)) {
            Toolkit.getDefaultToolkit().beep();
        }
        JOptionPane optionPane = new JOptionPane(msg, type);
        JDialog dialog = optionPane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    private String getError(Exception error) {
        StringBuilder msg = new StringBuilder();
        msg.append("Mensaje de Error: ");
        if (error.getCause() != null) {
            msg.append(error.getCause());
        }
        if (error.getLocalizedMessage() != null) {
            msg.append(error.getLocalizedMessage());
        }
        if (error.getMessage() != null) {
            msg.append(error.getMessage());
        }
        return msg.toString();

    }

}
