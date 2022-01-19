/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

/**
 *
 * @author ctolo
 */
public interface Dialog {

    void showError(String titulo, Exception error, String clase);

    void showInfo(String mensaje, String clase);

    int showConfirmacion(String mensaje);

    String showInput(String mensaje);
}
