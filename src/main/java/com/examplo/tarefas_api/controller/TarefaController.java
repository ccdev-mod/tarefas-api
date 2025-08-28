package com.examplo.tarefas_api.controller;

import com.examplo.tarefas_api.model.Tarefa;
import com.examplo.tarefas_api.repository.TarefaRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final TarefaRepository repository;

    public TarefaController(TarefaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Tarefa> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Tarefa buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Tarefa não encontrada"));
    }

    @PostMapping
    public ResponseEntity<Tarefa> criar(@Valid @RequestBody Tarefa tarefa) {
        Tarefa salva = repository.save(tarefa);
        return ResponseEntity.created(URI.create("/api/tarefas/" + salva.getId())).body(salva);
    }

    @PutMapping("/{id}")
    public Tarefa atualizar(@PathVariable Long id, @Valid @RequestBody Tarefa dados) {
        Tarefa existente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Tarefa não encontrada"));
        existente.setNome(dados.getNome());
        existente.setDataEntrega(dados.getDataEntrega());
        existente.setResponsavel(dados.getResponsavel());
        return repository.save(existente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        Tarefa existente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Tarefa não encontrada"));
        repository.delete(existente);
        return ResponseEntity.noContent().build();
    }
}
