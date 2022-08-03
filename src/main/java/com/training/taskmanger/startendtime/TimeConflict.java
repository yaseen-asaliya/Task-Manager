package com.training.taskmanger.startendtime;

import com.training.taskmanger.entity.Task;
import com.training.taskmanger.service.TaskServiceImplementation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TimeConflict {
    private TaskServiceImplementation taskServiceImplementation;
    private int userId;
    private int taskId;

    public TimeConflict(int userId, TaskServiceImplementation taskServiceImplementation, int taskId) {
        this.userId =userId;
        this.taskServiceImplementation = taskServiceImplementation;
        this.taskId = taskId;
    }

    public boolean isConflict(String start,String finish) throws ParseException {
        boolean overlap = false;
        List<Task> tasks = taskServiceImplementation.getTasks(userId);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        Date startDate = dateFormat.parse(start);
        Date endDate = dateFormat.parse(finish);

        for (int i=0 ;i < tasks.size() && tasks.get(i).getId() != taskId; i++) {
            Date newStart =  dateFormat.parse(tasks.get(i).getStart());
            Date newEnd =  dateFormat.parse(tasks.get(i).getFinish());
            overlap = newStart.compareTo(endDate) <= 0 && newEnd.compareTo(startDate) >= 0;
            if(overlap)
                break;
        }
        return overlap;
    }
}
