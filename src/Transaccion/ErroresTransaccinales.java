package Transaccion;

import Interfaces.Fechas;
import static Globals.Variables.*;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

public class ErroresTransaccinales implements Transaccion {

    String clase = "ErroresTransaccinales";

    Fechas fechas;

    @Override
    public ISOMsg exec(ISOMsg inputISO) {
        try {
            ISOMsg isoRespuesta = new ISOMsg();
            isoRespuesta.setMTI("0210");
            String proCode = inputISO.getValue(3).toString();
            isoRespuesta.set(3, proCode);
            String systemAudit = inputISO.getValue(11).toString();
            isoRespuesta.set(11, systemAudit);
            isoRespuesta.set(12, fechas.getTime());
            isoRespuesta.set(13, fechas.getDate());
            isoRespuesta.set(24, "000");
            String nroBoleta = getNroBoleta(fechas);
            isoRespuesta.set(37, nroBoleta);
            isoRespuesta.set(39, "51");
            String fieldValue = inputISO.getString(41);
            isoRespuesta.set(41, fieldValue);
            isoRespuesta.set(61, "");
            isoRespuesta.set(63, "001238313932323030313334333700413232443030312D4E4F204150524F424144412D4552524F522044454C205345525649444F52001639323035323032303230313233383133");

            return isoRespuesta;
        } catch (ISOException | NullPointerException e) {
            tools.showDialogEx("ISOException | NullPointerException ", clase, e);
        }
        return null;
    }

    public static String getNroBoleta(Fechas fechas) throws ISOException {
        String fecha = fechas.getDate();
        String hora = fechas.getTime();
        String rta = fecha + hora;
        return ISOUtil.padleft(rta, 12, '0');
    }
}
