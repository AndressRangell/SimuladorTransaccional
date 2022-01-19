package Transaccion;

import org.jpos.iso.ISOMsg;

public interface Transaccion {

     ISOMsg exec(ISOMsg inputISO);

 }
