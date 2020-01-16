// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.action;

import ar.com.syntagma.helpers.HolderHelper;
import javax.annotation.PostConstruct;
import ar.com.adecco.proveedores.model.Perfil;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import ar.com.adecco.proveedores.action.base.AprobacionProveedorAction;

@Named
@ViewScoped
public class ListadoProveedoresTesoreriaAction extends AprobacionProveedorAction implements Serializable
{
    private static final long serialVersionUID = 2215626695322597441L;
    
    @PostConstruct
    public void init() {
        this.initialize();
        this.panelProveedorAction.setPerfil(Perfil.TESORERIA);
        this.buscar();
    }
    
    public void doAprobar(final HolderHelper<Boolean> holderHelper) {
        this.panelProveedorAction.aprobarTesoreria((HolderHelper)holderHelper);
    }
    
    public void doRechazar(final HolderHelper<Boolean> holderHelper) {
        this.panelProveedorAction.rechazarTesoreria((HolderHelper)holderHelper);
    }
}
