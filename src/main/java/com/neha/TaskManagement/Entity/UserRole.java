package com.neha.TaskManagement.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roleId;
    private String roleName;
    private String roleCode;
    private String description;

    @ManyToMany(mappedBy = "roles")
    private List<User> user;

    @PrePersist
    public  void onPrePersist(){
        this.roleCode = this.roleName.toUpperCase().replaceAll("[^a-zA-Z]", "_");
    }

}
