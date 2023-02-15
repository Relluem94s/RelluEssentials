package de.relluem94.minecraft.server.spigot.essentials.constants;

public enum ProtectionFlags {
    ALLOWHOPPER("allowHopper", false),
    ALLOWREDSTONE("allowRedstone", false),
    ALLOWPUBLIC("allowPublic", false),
    AUTOCLOSE("autoClose", false);

    
    private String name;
    private boolean isDefault;

    private ProtectionFlags(String name, boolean isDefault){
        this.name = name;
        this.isDefault = isDefault;
    }
   
    public String getName(){
        return this.name;
    }
   
    public boolean isDefault(){
        return this.isDefault;
    }
}