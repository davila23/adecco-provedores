// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos.helpers;

public enum ErrorMessage
{
    ERROR_CRITICAL("Error Cr\u00edtico: Se produjo un error desconocido."), 
    ERROR_BD("Error Inesperado: Hubo un error en una operaci\u00f3n con la Base de Datos."), 
    ERROR_SA("Error en Env\u00edo de Mail: No se pudo enviar un mail."), 
    ERROR_ADJUNTO("Error Adjunto: Hubo al cargar el archivo adjunto."), 
    ERROR_DESCARGA("Error Adjunto: Hubo al descargar el archivo adjunto."), 
    ERROR_IAP("Error Interface: No se pudo aprobar el Proveedor."), 
    ERROR_IAF("Error Interface: No se pudo aprobar la Factura."), 
    ERROR_CON_SD("Error de Conversi\u00f3n: No se pudo convertir el valor ingresado."), 
    DENIED_ACCESS("Usted no puede realizar la operaci\u00f3n por falta de permisos.");
    
    private String message;
    
    public String getMessage() {
        return this.message;
    }
    
    private ErrorMessage(final String message) {
        this.message = message;
    }
}
