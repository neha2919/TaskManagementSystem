package com.neha.TaskManagement.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//FRONTEND, BACKEND, RECRUITMENT TEAM ETC....
@Entity
@Table(name="sub_departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubDepartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subDeptId;

    @Column(unique = true, nullable = false)
    private String subDeptName;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "subDepartment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;

}
