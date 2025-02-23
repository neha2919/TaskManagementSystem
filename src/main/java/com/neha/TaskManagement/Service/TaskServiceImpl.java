package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Dtos.TaskDto;
import com.neha.TaskManagement.Dtos.UserDto;
import com.neha.TaskManagement.Entity.Task;
import com.neha.TaskManagement.Entity.User;
import com.neha.TaskManagement.Exception.NotAllowedException;
import com.neha.TaskManagement.Exception.NotFoundException;
import com.neha.TaskManagement.Exception.NullException;
import com.neha.TaskManagement.Exception.UnauthorizedException;
import com.neha.TaskManagement.Model.Progress;
import com.neha.TaskManagement.Repository.TaskRepository;
import com.neha.TaskManagement.Repository.UserRepository;
import com.neha.TaskManagement.Security.Jwt.Model.JwtUser;
import com.neha.TaskManagement.Security.LocalAuthStore;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    //for api access, once the token based system is configured, there would be no need to additionally check for isAdmin. - understood
    @Override
    @Transactional
    public TaskDto createTask(TaskDto taskDto) {
        String currentUser = LocalAuthStore.getLocalAuthStore().getJwtUser().getPrincipal();

        taskDto.setProgress(Progress.PENDING);
        taskDto.setAssignedOn(LocalDate.now());
        taskDto.setCreatedBy(currentUser);
        taskDto.setAssignedBy(currentUser);

        List<User> assignedUsers = userRepository.findByUsernameIn(new HashSet<>(taskDto.getUsername()));
        if (assignedUsers.isEmpty()) {
            throw new NotFoundException("No users found for the given emails.");
        }

        Task task = TaskDto.dtoToEntity(taskDto);
        task.setUsers(new ArrayList<>(assignedUsers));

        for (User user : assignedUsers) {
            user.getTasks().add(task);
        }
        task = taskRepository.save(task);
        userRepository.saveAll(assignedUsers);
        return TaskDto.entityToDto(task);
    }


    @Override
    public TaskDto getTaskById(UUID taskId) {
        //if the user is SUPER_ADMIN/one of the assigned user/ the one who created the task, only they can view. Anyone else, cannot.
        //logic yet to be implemented since we are in the testing phase. In the master branch, we should merge with these checks and cases.
        return TaskDto.entityToDto(taskRepository.findById(taskId)
                .orElseThrow(()->new NotFoundException("Task not found")));
    }

    @Override
    public List<TaskDto> getAllTasks() {
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

    @Override
    public TaskDto updateTask(TaskDto taskDto) {
        if (taskDto.getTaskId()==null) throw new NullException("Task ID cannot be empty.");
        JwtUser currentUser = LocalAuthStore.getLocalAuthStore().getJwtUser();
        if (!userRepository.existsByUsername(currentUser.getPrincipal())) throw new NotFoundException("User does not exists.");
        Task existingTask = taskRepository.findById(taskDto.getTaskId()).orElseThrow(()-> new NotFoundException("Task not found"));
        //if user is SUPER_ADMIN or the one who has created the task then he/she can update the task overall
        //else if the current user has the task assigned for him, he/she can update its progress only.
        List<String> assignedUsers = existingTask.getUsers().stream().map(User::getUsername).toList();
        if (currentUser.getAuthorities().contains("SUPER_ADMIN") || currentUser.getPrincipal().equalsIgnoreCase(taskDto.getCreatedBy()) || currentUser.getPrincipal().equalsIgnoreCase(taskDto.getAssignedBy())){
            if(taskDto.getTitle()!=null && !taskDto.getTitle().isBlank()) existingTask.setTitle(taskDto.getTitle());
            if (taskDto.getDescription()!=null && !taskDto.getDescription().isBlank()) existingTask.setDescription(taskDto.getDescription());
            if (taskDto.getDueDate()!=null){
                if (taskDto.getDueDate().isBefore(LocalDate.now())) throw new NotAllowedException("Due date cannot be before today.");
                existingTask.setDueDate(taskDto.getDueDate());
            }
            if (taskDto.getPriority()!=null) existingTask.setPriority(taskDto.getPriority());
        }
        if (assignedUsers.contains(currentUser.getPrincipal())){
            if (taskDto.getProgress()!=null) existingTask.setProgress(taskDto.getProgress());
        }
        else throw new UnauthorizedException("You are not authorized to update this task.");

        return TaskDto.entityToDto(taskRepository.save(existingTask));

    }

    @Override
    @Transactional
    public Task deleteTask(UUID taskId) {
        Task taskToBeDeleted = taskRepository.findById(taskId).orElseThrow(()->new NotFoundException("Task id does not exist"));
        taskRepository.delete(taskToBeDeleted);
        return taskToBeDeleted;
    }

    @Override
    public List<TaskDto> createSubtask(UUID parentTaskId, List<TaskDto> subTasks) {
        String currentUser = LocalAuthStore.getLocalAuthStore().getJwtUser().getPrincipal();
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
            converted.setCreatedBy(parentTask.getCreatedBy());
            converted.setAssignedBy(currentUser);
            converted.setProgress(Progress.PENDING);
            //assigning users to sub-tasks.
            List<User> assignedUser = userRepository.findByUsernameIn(new HashSet<>(subTask.getUsername()));
            converted.setUsers(new ArrayList<>(assignedUser));

            //child parentTask update
            converted.setParentTask(parentTask);
            //parent's subTask update
            parentTask.getSubTasks().add(converted);

            //assigning the user the sub-task for bidirectional relationship
            for(User user : assignedUser){
                if (user.getTasks()==null){
                    user.setTasks(new ArrayList<>());
                }
                user.getTasks().add(converted);
            }
            subTasksToBeSaved.add(converted);
        }
        parentTask = taskRepository.save(parentTask);
        //List<taskDto> -> for-loop for converting the entity list to dto.
        return parentTask.getSubTasks().stream().map(TaskDto :: entityToDto).toList();
    }

    @Override
    public List<TaskDto> getTaskByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new NotFoundException("User does not exists."));
        return taskRepository.findByUsers_Id(user.getId()).stream().map(TaskDto::entityToDto).toList();
    }

    @Override
    public List<String> assignNewUsers(TaskDto taskDto) {
        if (taskDto==null || taskDto.getUsername()==null || taskDto.getUsername().isEmpty()) throw new NotAllowedException("You can assign at least 1 new user.");
        if (taskDto.getTaskId()==null) throw new NullException("Task ID cannot be empty.");
        Task task = taskRepository.findById(taskDto.getTaskId()).orElseThrow(()->new NotFoundException("Task not found."));
        if (task.getUsers()==null || task.getUsers().isEmpty()) task.setUsers(new ArrayList<>());
        List<User> users = userRepository.findByUsernameIn(new HashSet<>(taskDto.getUsername()))
                .stream()
                .filter(user -> user!=null && !user.getUsername().equalsIgnoreCase(LocalAuthStore.getLocalAuthStore().getJwtUser().getPrincipal()))
                .toList();
        if (users.isEmpty()) throw new NotFoundException("No user was found from the given list.");
        for (User user : users){
            if (user.getTasks()==null) user.setTasks(new ArrayList<>());
            user.getTasks().add(task);
        }
        task.getUsers().addAll(users);
        task = taskRepository.save(task);
        return task.getUsers().stream().map(User::getUsername).toList();
    }

    @Override
    public List<String> removeAssignedUser(TaskDto taskDto) {
        if (taskDto==null || taskDto.getUsername()==null || taskDto.getUsername().isEmpty()) throw new NotAllowedException("You can assign at least 1 new user.");
        if (taskDto.getTaskId()==null) throw new NullException("Task ID cannot be empty.");
        Task task = taskRepository.findById(taskDto.getTaskId()).orElseThrow(()->new NotFoundException("Task not found."));
        if (task.getUsers()==null || task.getUsers().isEmpty()) task.setUsers(new ArrayList<>());
        List<User> users = userRepository.findByUsernameIn(new HashSet<>(taskDto.getUsername()));
        if (users.isEmpty()) throw new NotFoundException("No user was found from the given list.");
        for (User user : users){
            if (user.getTasks()==null) user.setTasks(new ArrayList<>());
            user.getTasks().remove(task);
        }
        task.getUsers().removeAll(users);
        task = taskRepository.save(task);
        return task.getUsers().stream().map(User::getUsername).toList();
    }

    @Override
    public List<TaskDto> getTaskByIds(List<UUID> uuidList) {
        List<Task> taskList = taskRepository.findAllById(uuidList);
        if (taskList.isEmpty()) throw new NotFoundException("No tasks exists.");

        return taskList.stream().map(TaskDto::entityToDto).toList();
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
