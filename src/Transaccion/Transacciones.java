package Transaccion;

import static Globals.Variables.*;

import Entidades.Campo63;
import Interfaces.Dialog;
import Interfaces.Fechas;
import Interfaces.Logger;
import Server.ModelTrans;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import Utils.ExtraTools;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import javax.swing.*;

public class Transacciones implements Transaccion {

    Fechas fechas;
    Dialog dialog;
    Logger log;
    Map<String, ArrayList<ModelTrans>> campos;

    public Transacciones(Fechas fechas, Map<String, ArrayList<ModelTrans>> campos) {
        this.fechas = fechas;
        this.campos = campos;
    }

    @Override
    public ISOMsg exec(ISOMsg inputISO) {

        try {
            fechas.exec();
            ISOMsg isoRespuesta = new ISOMsg();
            campos.get(NORMALES).forEach((campo) -> {
                try {
                    isoRespuesta.set(campo.getId(), campo.getData());
                } catch (ISOException ex) {
                    tools.showDialogEx("ISOException | NullPointerException", this, ex);
                }
            });

            String procCode = inputISO.getString(3);

            isoRespuesta.set(3, procCode);
            isoRespuesta.set(11, inputISO.getString(11));

            if (procCode.equals("000000")) {
                return isoRespuesta;
            }
            int input = optionTrasaccion();
            if (input == 0) {
                isoRespuesta.set(39, "00");
            } else if (input == 1) {
                isoRespuesta.set(39, "99");
            } else {
                return null;
            }

            isoRespuesta.set(12, fechas.getTime());
            isoRespuesta.set(13, fechas.getDate());
            isoRespuesta.set(24, inputISO.getString(24));
            //isoRespuesta.set(35, (byte[]) null);
            isoRespuesta.set(35, inputISO.getString(35));
            isoRespuesta.set(41, inputISO.getString(41));
            isoRespuesta.set(42, inputISO.getString(42));

            isoRespuesta.set(37, getNumeroBoleta());
            if (inputISO.getString(63) != null) {
                byte[] field63 = ExtraTools.getAsByteAr(getField63(input, inputISO.getString(63)));
                isoRespuesta.set(63, field63);
            }


            return isoRespuesta;
        } catch (
                NullPointerException ex) {
            tools.showDialogEx("ISOException | NullPointerException", this, ex);
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private int optionTrasaccion() {
        int input = JOptionPane.showOptionDialog(null,
                "Aceptar transaccion?",
                "opcion de transaccion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"SI", "NO", "REVERSO"},
                "SI");
        return input;
    }

    private String getNumeroBoleta() {
        return tools.padText((fechas.getDate() + fechas.getTime()), 12, PAD_LEFT, '0');
    }

    private String getField63(int input, String iso) throws ISOException {
        StringBuilder campo63 = new StringBuilder();
        cp63.setSubCampos(iso);
        campos.get(CAMPO63).forEach((campo) -> {
            try {
                if (input == 1) {
                    if (campo.getId() == 22 || campo.getId() == 92) {
                        campo63.append(ExtraTools.getField(campo.getId(), campo.getData()));
                    }
                } else {
                    campo63.append(ExtraTools.getField(campo.getId(), campo.getData()));
                }

            } catch (ISOException e) {
                e.printStackTrace();
            }
        });
        return campo63.toString();
    }
}
