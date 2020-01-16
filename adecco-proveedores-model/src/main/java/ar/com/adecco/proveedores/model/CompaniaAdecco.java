// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

public enum CompaniaAdecco
{
    ADECCO(101L, "01"), 
    RRHH(122L, "02"), 
    SPECIALTIES(123L, "04");
    
    private long org_id;
    private String segmento1;
    
    private CompaniaAdecco(final long org_id, final String segmento1) {
        this.org_id = org_id;
        this.segmento1 = segmento1;
    }
    
    public long getOrg_id() {
        return this.org_id;
    }
    
    public String getSegmento1() {
        return this.segmento1;
    }
}
