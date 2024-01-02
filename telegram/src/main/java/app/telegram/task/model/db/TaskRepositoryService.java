package app.telegram.task.model.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskRepositoryService {

    private final TaskRepository taskRepository;
}
