// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import javax.inject.Named;
import ar.com.adecco.dominio.ebs.gl.JuegoLibros;

@Named
public class JuegoLibrosDao extends AbstractDao<JuegoLibros>
{
    private static final long serialVersionUID = 7302227006752080220L;
    
    @Override
    public Class<JuegoLibros> getClaseEntidad() {
        return JuegoLibros.class;
    }
}
