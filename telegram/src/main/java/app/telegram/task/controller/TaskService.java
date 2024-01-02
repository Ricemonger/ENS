package app.telegram.task.controller;

import app.telegram.task.model.db.TaskRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepositoryService repositoryService;
}
