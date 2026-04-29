package com.prosa.workshop.rest.todo.controller;

import com.prosa.workshop.rest.todo.dto.CreateTodoRequest;
import com.prosa.workshop.rest.todo.dto.TodoDto;
import com.prosa.workshop.rest.todo.dto.UpdateTodoRequest;
import com.prosa.workshop.rest.todo.model.TodoStatus;
import com.prosa.workshop.rest.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // -------------------------------------------------------------------------
    // TODO A — GET /api/v1/todos
    // -------------------------------------------------------------------------
    // Return all todos. Support optional filtering via ?status=OPEN|IN_PROGRESS|DONE
    // Response: 200 OK with a list of TodoDto (empty list [] if none found)
    //
    // Hint: ResponseEntity.ok(todoService.findAll(status))
    // -------------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<TodoDto>> getAllTodos(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(todoService.findAll(status));
    }

    // -------------------------------------------------------------------------
    // TODO B — GET /api/v1/todos/{id}
    // -------------------------------------------------------------------------
    // Return a single todo by its id.
    // Response: 200 OK with the TodoDto, or 404 if not found (handled globally)
    //
    // Hint: ResponseEntity.ok(todoService.findById(id))
    // -------------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<TodoDto> getTodoById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.findById(id));
    }

    // -------------------------------------------------------------------------
    // TODO C — POST /api/v1/todos
    // -------------------------------------------------------------------------
    // Create a new todo from the request body.
    // Response: 201 Created with the new TodoDto AND a Location header
    //           pointing to the new resource: /api/v1/todos/{id}
    //
    // Hint: ResponseEntity.created(location).body(created)
    // Hint: URI location = uriBuilder.path("/api/v1/todos/{id}")
    //                                .buildAndExpand(created.getId()).toUri();
    // -------------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<TodoDto> createTodo(
            @Valid @RequestBody CreateTodoRequest request,
            UriComponentsBuilder uriBuilder) {
        TodoDto created = todoService.create(request);
        var location = uriBuilder.path("/api/v1/todos/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    // -------------------------------------------------------------------------
    // TODO D — PUT /api/v1/todos/{id}
    // -------------------------------------------------------------------------
    // Full update of a todo (replace non-null fields from request body).
    // Response: 200 OK with updated TodoDto, or 404 if not found
    //
    // Hint: ResponseEntity.ok(todoService.update(id, request))
    // -------------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<TodoDto> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTodoRequest request) {
        return ResponseEntity.ok(todoService.update(id, request));
    }

    // -------------------------------------------------------------------------
    // TODO E — PATCH /api/v1/todos/{id}/status
    // -------------------------------------------------------------------------
    // Update the status of a todo only.
    // Example: PATCH /api/v1/todos/1/status?status=IN_PROGRESS
    // Response: 200 OK with updated TodoDto, or 404 if not found
    // -------------------------------------------------------------------------
    @PatchMapping("/{id}/status")
    public ResponseEntity<TodoDto> updateStatus(
            @PathVariable Long id,
            @RequestParam TodoStatus status) {
        return ResponseEntity.ok(todoService.updateStatus(id, status));
    }

    // -------------------------------------------------------------------------
    // TODO F — DELETE /api/v1/todos/{id}
    // -------------------------------------------------------------------------
    // Delete a todo by id.
    // Response: 204 No Content (no body), or 404 if not found
    //
    // Hint: ResponseEntity.noContent().build()
    // -------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
