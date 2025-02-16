package com.neha.TaskManagement.Entity;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;



@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String username;
    private String password;
    private String firstName, lastName, fullName;

    @Column(unique = true, nullable = false)
    @Email(message = "Please enter valid email")
    private String email;
    private Long phoneNumber;

    @ManyToMany
    private Set<UserRole> roles;

    @Column(unique = true, nullable = false)
    private String employeeId;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "sub_department_id")
    private SubDepartment subDepartment;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    //Many To Many mapping for Users to Task
    //This should be the owning side.
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_tasks",
            joinColumns = @JoinColumn(name = "user_id"),//foreign key for User
            inverseJoinColumns = @JoinColumn(name = "task_id")//foreign key for Task
    )
    private List<Task> tasks; //list of task assigned to the user
    public void setFullName(){
        this.fullName=firstName+" "+lastName;
    }

    @PrePersist
    public void onPrePersist() {
        this.employeeId = "COMP" + this.email.hashCode();
        this.fullName = this.firstName.trim() + " " + this.lastName.trim();
    }

//    private void onPrePersist(){
//        this.employeeId = "NS"+this.email.hashCode();
//    }
}
