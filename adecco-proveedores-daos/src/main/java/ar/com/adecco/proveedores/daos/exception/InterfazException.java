// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos.exception;

public class InterfazException extends Exception
{
    private static final long serialVersionUID = -3578547639333255998L;
    private long codigoError;
    
    public InterfazException(final String s) {
        super(s);
    }
    
    public long getCodigoError() {
        return this.codigoError;
    }
    
    public void setCodigoError(final long codigoError) {
        this.codigoError = codigoError;
    }
}
