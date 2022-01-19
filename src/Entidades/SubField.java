/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

/**
 *
 * @author Christian
 */
public class SubField {

    String idCampo;
    String nombreCampo;
    String dataCampo;

    public SubField(String idCampo, String nombreCampo, String dataCampo) {
        this.idCampo = idCampo;
        this.nombreCampo = nombreCampo;
        this.dataCampo = dataCampo;
    }
    

    public String getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(String idCampo) {
        this.idCampo = idCampo;
    }

    public String getNombreCampo() {
        return nombreCampo;
    }

    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    public String getDataCampo() {
        return dataCampo;
    }

    public void setDataCampo(String dataCampo) {
        this.dataCampo = dataCampo;
    }

}
