package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SettingEntry {
    private int id;
    private LocalDateTime created;
    private int createdBy;
    private LocalDateTime updated;
    private Integer updatedBy;
    private String name;
}
