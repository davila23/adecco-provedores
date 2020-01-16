// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

public enum Letra
{
    A("A"), 
    B("B"), 
    C("C"), 
    E("E"), 
    M("M"), 
    X("X");
    
    private String letra;
    
    public String getLetra() {
        return this.letra;
    }
    
    private Letra(final String letra) {
        this.letra = letra;
    }
}
