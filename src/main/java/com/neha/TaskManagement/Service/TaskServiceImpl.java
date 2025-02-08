package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Dtos.TaskDto;
import com.neha.TaskManagement.Entity.Task;
import com.neha.TaskManagement.Exception.NotFoundException;
import com.neha.TaskManagement.Exception.UnauthorizedException;
import com.neha.TaskManagement.Model.Progress;
import com.neha.TaskManagement.Repository.TaskRepository;
import com.neha.TaskManagement.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    //for api access, once the token based system is configured, there would be no need to additionally check for isAdmin.
    @Override
    public TaskDto createTask(Task task, String email) {
        userRepository.findByEmailAndIsAdmin(email, true)
                .orElseThrow(() -> new NotFoundException("User is not an admin or not found"));
        //by default it is pending.
        task.setAssignedOn(LocalDate.now());
        task.setProgress(Progress.PENDING);
        return TaskDto.entityToDto(taskRepository.save(task));
    }

    @Override
    public TaskDto getTaskById(UUID taskId, String email) {
        userRepository.findByEmailAndIsAdmin(email, true)
                .orElseThrow(() -> new UnauthorizedException("Only admin can view tasks"));
        return TaskDto.entityToDto(taskRepository.findById(taskId)
                .orElseThrow(()->new NotFoundException("Task not found")));
    }

    @Override
    public List<TaskDto> getAllTasks(String email) {
        userRepository.findByEmailAndIsAdmin(email, true).orElseThrow(() -> new NotFoundException("Only admin can view all tasks"));
        return taskRepository.findAll().stream().map(TaskDto::entityToDto).toList();
        //if you have difficulty to understand the above, follow the below code for reference
//        return taskRepository.findAll()
//                .stream()
//                .map(task -> {
//                    return TaskDto.entityToDto(task);
//                })
//                .toList();
    }

    //how can only admin update task? I am assigned a task how then am I not supposed to update its progress/status?
    @Override
    public Task updateTask(Task task, String email) {
        userRepository.findByEmailAndIsAdmin(email, true).orElseThrow(() -> new UnauthorizedException("Only admin can update task"));
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task deleteTask(UUID taskId, String email) {
        userRepository.findByEmailAndIsAdmin(email, true).orElseThrow(() -> new UnauthorizedException("Only admin can delete tasks"));
        Task taskToBeDeleted = taskRepository.findById(taskId).orElseThrow(()->new NotFoundException("Task id does not exist"));
        taskRepository.delete(taskToBeDeleted);
        return taskToBeDeleted;
    }

    @Override
    public TaskDto createSubtask(UUID parentTaskId, List<Task> subTasks) {
        return null;
    }

    /**
     * Make a recursive progress calculation since we will have sub-tasks included and a sub-task can also have a sub task.
     *  private double progressPercentage(String progress){
     *      if(task.progress == IN_PROGRESS) return 50.0;
     *      if(task.progress == COMPLETED) return 100.0
     *      return 0.0
     *   }
     *   private calculateProgressPercentage(Task task){
     *       base case -
     *       if(task.getSubTask()==null || task.getSubTask().isEmpty()){
     *           return progressPercentage(task.getProgress());
     *       }
     *       int subCount = task.getSubTask().size();
     *       double percentage = 0.0;
     *
     *       recursive case:-
     *       for(Task task : task.getSubTask){
     *           percentage = percentage + calculateProgressPercentage(task);
     *       }
     *
     *       return percentage/subCount;
     *   }
     *
     **/
}
