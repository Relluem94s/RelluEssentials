package de.relluem94.minecraft.server.spigot.essentials.permissions;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;

/**
 *
 * @author rellu
 */
public class Groups {
   public static GroupEntry getGroup(String name){
       for(GroupEntry ge : RelluEssentials.groupEntryList){
           if(ge.getName().equalsIgnoreCase(name)){
               return ge;
           }
       }
       return null;
   }
   
   public static GroupEntry getGroup(int id){
       for(GroupEntry ge : RelluEssentials.groupEntryList){
           if(ge.getId() == id){
               return ge;
           }
       }
       return null;
   }
}
