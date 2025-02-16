package com.neha.TaskManagement.Entity;

import com.neha.TaskManagement.Model.Priority;
import com.neha.TaskManagement.Model.Progress;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) //GenerationType.IDENTITY - it works well with the auto increment feature
    @Column(nullable = false, unique = true)
    private UUID taskId;
    //remove status.
    private String title, description;
    private LocalDate assignedOn;
    private LocalDate dueDate;
    //Addition of task priority enum class.
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private String createdBy;
    private String assignedBy;
    @Enumerated(EnumType.STRING)
    //instead of taking String status we will be taking enum Progress.
    private Progress progress;
    //calculates the percentage of the task completion.
    private Double progressPercentage;
    //Many to Many tasks to users
    @ManyToMany(mappedBy = "tasks", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<User> users;
    /**
     * if a task is assigned as "Develop Backend for user authentication"
     * this is the parent task, however, when one developer is assigned to this task, he can make sub-branches and assign them
     * different developers. So we should give an option for creating branches for parent Task.
     **/

    /**
     * If a task will be a sub-task, the parent task will be populated. Many(Task) to One(parentTask)
     **/
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_task")
    private Task parentTask;
    /**
     * Multiple sub-tasks with the same parent task. One(Task) to Many(subTasks)
     **/
    @OneToMany(mappedBy = "parentTask",orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Task> subTasks;

    @PrePersist
    private void onPrePersist(){
        /**
         *
         * IT IS NOT NECESSARY TO ADD THE LOGIC HERE ONLY, YOU CAN ADD IT TO UPDATE TASK SERVICE TOO. IT IS UP TO YOU WHERE YOU WANT TO ADD.
         * The calculation logic, trigger it here to update the progress percentage. Later, we will make it event based.
         * This wil trigger before the data is getting saved to the db. So in case for creation or updation of the task,
         * we will always calculate the progress.
         *
         * Make sure to understand the concept for progress percentage. Add a logic to make sure the progress percentage
         * is update for the parent Task. I mean, a task is updated, we will have to check if it has any parent or not,
         * if it doesnt, then we only update its percentage. else we do the recursive thing to update its current progress percentae
         * and keep update its parent.
         *
         *Maybe like: (NOT SURE)
         * #update current task's progressPercentage()
         * task = task.getParentTask();
         * while(task==null){
         *     calculatePercentage(task);
         *     task = task.getParentTask()
         * }
         **/
    }
}
