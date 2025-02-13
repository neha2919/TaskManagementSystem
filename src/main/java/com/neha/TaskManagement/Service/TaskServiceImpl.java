package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Dtos.TaskDto;
import com.neha.TaskManagement.Dtos.UserDto;
import com.neha.TaskManagement.Entity.Task;
import com.neha.TaskManagement.Entity.User;
import com.neha.TaskManagement.Exception.NotFoundException;
import com.neha.TaskManagement.Exception.NullException;
import com.neha.TaskManagement.Exception.UnauthorizedException;
import com.neha.TaskManagement.Model.Progress;
import com.neha.TaskManagement.Repository.TaskRepository;
import com.neha.TaskManagement.Repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    //for api access, once the token based system is configured, there would be no need to additionally check for isAdmin. - understood
    @Override
    public TaskDto createTask(TaskDto taskDto, String email) {
//        userRepository.findByEmailAndIsAdmin(email, true)
//                .orElseThrow(() -> new NotFoundException("User is not an admin or not found"));
        //by default it is pending.
        taskDto.setAssignedOn(LocalDate.now());
        taskDto.setProgress(Progress.PENDING);
        return TaskDto.entityToDto(taskRepository.save(TaskDto.dtoToEntity(taskDto)));
    }

    @Override
    public TaskDto getTaskById(UUID taskId, String email) {
//        userRepository.findByEmailAndIsAdmin(email, true)
//                .orElseThrow(() -> new UnauthorizedException("Only admin can view tasks"));
        return TaskDto.entityToDto(taskRepository.findById(taskId)
                .orElseThrow(()->new NotFoundException("Task not found")));
    }

    @Override
    public List<TaskDto> getAllTasks(String email) {
//        userRepository.findByEmailAndIsAdmin(email, true).orElseThrow(() -> new NotFoundException("Only admin can view all tasks"));
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
    //admin can edit if required after assigning the task and employee can only update the status. If i will give full access to employee to edit then the employee will be able to edit the dueDate, title and everything which should be restricted.

    /**@Override
    public TaskDto updateTask(TaskDto taskDto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new NotFoundException("User not found"));
        Task existingTask = taskRepository.findById(taskDto.getTaskId()).orElseThrow(()-> new NotFoundException("Task not found"));

        if(user.getIsAdmin){
            existingTask.setTitle(taskDto.getTitle());
            existingTask.setDescription(taskDto.getDescription());
            existingTask.setProgress(taskDto.getProgress());
            existingTask.setDueDate(taskDto.getDueDate());
            existingTask.setPriority(taskDto.getPriority());
        }
        else if(existingTask.getAssignedUser() != null && existingTask.getAssignedUser().equals(user)){
            existingTask.setProgress(taskDto.getProgress());
        }
        else{
            throw new UnauthorizedException("User do not have the permission to update task");
        }
        Task updatedTask = taskRepository.save(existingTask);
        return TaskDto.entityToDto(updatedTask);

    }**/

    @Override
    @Transactional
    public Task deleteTask(UUID taskId, String email) {
//        userRepository.findByEmailAndIsAdmin(email, true).orElseThrow(() -> new UnauthorizedException("Only admin can delete tasks"));
        Task taskToBeDeleted = taskRepository.findById(taskId).orElseThrow(()->new NotFoundException("Task id does not exist"));
        taskRepository.delete(taskToBeDeleted);
        return taskToBeDeleted;
    }

    @Override
    public List<TaskDto> createSubtask(UUID parentTaskId, List<TaskDto> subTasks) {
        //get the parent task
        Task parentTask = taskRepository.findById(parentTaskId)
                .orElseThrow(()->new NotFoundException("Task does not exists."));
        //check if the subTasks is null || empty
        if (subTasks==null || subTasks.isEmpty()) throw new NullException("Sub-task was empty. No task was created.");
        if(parentTask.getSubTasks()==null) parentTask.setSubTasks(new ArrayList<>());
        //convert dto to entity
        List<Task> subTasksToBeSaved = new ArrayList<>();
        for (TaskDto subTask : subTasks){
            Task converted = TaskDto.dtoToEntity(subTask);
            //child parentTask update
            converted.setParentTask(parentTask);
            //parent's subTask update
            parentTask.getSubTasks().add(converted);

            subTasksToBeSaved.add(converted);
        }
        parentTask = taskRepository.save(parentTask);
        //List<taskDto> -> for-loop for converting the entity list to dto.
        return parentTask.getSubTasks().stream().map(TaskDto :: entityToDto).toList();
    }

    /**
     * Make a recursive progress calculation since we will have sub-tasks included and a sub-task can also have a sub-task.
     * If the code doesnt runs, then fix the logic-i did this directly in this comment section.
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

     *       recursive case:-
     *       for(Task task : task.getSubTask){
     *           percentage = percentage + calculateProgressPercentage(task);
     *       }

     *       return percentage/subCount;
     *   }
     *
     **/
}
