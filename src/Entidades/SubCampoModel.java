/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

/**
 *
 * @author ctolo
 */
public class SubCampoModel {
    String idCampo;
    String dataCampo;

    public SubCampoModel(String idCampo, String dataCampo) {
        this.idCampo = idCampo;
        this.dataCampo = dataCampo;
    }
    

    public String getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(String idCampo) {
        this.idCampo = idCampo;
    }

    public String getDataCampo() {
        return dataCampo;
    }

    public void setDataCampo(String dataCampo) {
        this.dataCampo = dataCampo;
    }
    
    
    
}
