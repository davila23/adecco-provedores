// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import java.util.ArrayList;
import ar.com.adecco.proveedores.model.EstadoFactura;
import java.util.List;
import ar.com.adecco.proveedores.model.Perfil;
import java.util.HashMap;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class ReglasVisualizacion implements Serializable
{
    private static final long serialVersionUID = 3307102251224244938L;
    private HashMap<Perfil, HashMap<String, List<EstadoFactura>>> matriz;
    
    public ReglasVisualizacion() {
        this.matriz = new HashMap<Perfil, HashMap<String, List<EstadoFactura>>>();
        this.agregar(Perfil.PROVEEDOR, "VER", EstadoFactura.INGRESADA);
        this.agregar(Perfil.PROVEEDOR, "VER", EstadoFactura.RECHAZADA);
        this.agregar(Perfil.ACEPTADOR, "VER", null);
        this.agregar(Perfil.APROBADOR, "VER", null);
        this.agregar(Perfil.PAGO, "VER", null);
    }
    
    private void agregar(final Perfil perfil, final String s, final EstadoFactura estadoFactura) {
        if (this.matriz.get(perfil) == null) {
            this.matriz.put(perfil, new HashMap<String, List<EstadoFactura>>());
        }
        if (this.matriz.get(perfil).get(s) == null) {
            this.matriz.get(perfil).put(s, new ArrayList<EstadoFactura>());
        }
        if (!this.matriz.get(perfil).get(s).contains(estadoFactura)) {
            this.matriz.get(perfil).get(s).add(estadoFactura);
        }
    }
    
    public boolean mostrar(final EstadoFactura estadoFactura, final Perfil perfil, final String s) {
        return this.matriz.get(perfil).get(s).contains(estadoFactura);
    }
}
