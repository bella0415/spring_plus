package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용 트랜잭션
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    @Transactional // 쓰기 메서드는 별도로 선언해서 readOnly 해제
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(authUser);
        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
            todoSaveRequest.getTitle(),
            todoSaveRequest.getContents(),
            weather,
            user
        );

        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
            savedTodo.getId(),
            savedTodo.getTitle(),
            savedTodo.getContents(),
            weather,
            new UserResponse(user.getId(), user.getEmail())
        );
    }


    public Page<TodoResponse> getTodos(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Todo> todos = todoRepository.findAllByOrderByModifiedAtDesc(pageable);

        return todos.map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }

    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }

    /**
     * weather, 수정일 기간 조건을 이용하여 할 일 목록을 검색
     *
     * @param weather   검색할 날씨 조건 (nullable)
     * @param startDate 수정일 시작 범위 (nullable)
     * @param endDate   수정일 종료 범위 (nullable)
     * @param pageable 페이징 정보
     * @return 조건에 맞는 할 일 목록 페이지
     */
    public Page<Todo> searchTodos(
        String weather,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Pageable pageable
    ) {
        return todoRepository.searchByConditions(weather, startDate, endDate, pageable);
    }
}
