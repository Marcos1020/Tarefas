package com.documents.tarefas.controller;

import com.documents.tarefas.dto.AtualizarTarefaRequest;
import com.documents.tarefas.dto.CriarTarefaRequest;
import com.documents.tarefas.dto.TarefaDTO;
import com.documents.tarefas.model.PrioridadeTarefa;
import com.documents.tarefas.model.StatusTarefa;
import com.documents.tarefas.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tarefas", description = "API para gerenciamento de tarefas")
@CrossOrigin(origins = "*")
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    @Operation(summary = "Criar nova tarefa", description = "Cria uma nova tarefa no sistema")
    public ResponseEntity<TarefaDTO> criarTarefa(@Valid @RequestBody CriarTarefaRequest request) {
        log.info("Recebendo requisição para criar tarefa: {}", request.getTitulo());
        TarefaDTO tarefa = tarefaService.criarTarefa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefa);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarefa por ID", description = "Retorna uma tarefa específica pelo seu ID")
    public ResponseEntity<TarefaDTO> buscarPorId(@PathVariable Long id) {
        log.info("Recebendo requisição para buscar tarefa com ID: {}", id);
        TarefaDTO tarefa = tarefaService.buscarPorId(id);
        return ResponseEntity.ok(tarefa);
    }

    @GetMapping
    @Operation(summary = "Listar tarefas", description = "Lista todas as tarefas com paginação e filtros")
    public ResponseEntity<Page<TarefaDTO>> listarTarefas(
            @Parameter(description = "Número da página (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação") @RequestParam(defaultValue = "dataCriacao") String sortBy,
            @Parameter(description = "Direção da ordenação") @RequestParam(defaultValue = "desc") String sortDir,
            @Parameter(description = "Status da tarefa") @RequestParam(required = false) StatusTarefa status,
            @Parameter(description = "Prioridade da tarefa") @RequestParam(required = false) PrioridadeTarefa prioridade,
            @Parameter(description = "Usuário responsável") @RequestParam(required = false) String usuario,
            @Parameter(description = "Categoria da tarefa") @RequestParam(required = false) String categoria) {

        log.info("Recebendo requisição para listar tarefas - Page: {}, Size: {}, Sort: {}", page, size, sortBy);

        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<TarefaDTO> tarefas;
        if (status != null || prioridade != null || usuario != null || categoria != null) {
            tarefas = tarefaService.listarTarefasComFiltros(status, prioridade, usuario, categoria, pageable);
        } else {
            tarefas = tarefaService.listarTarefas(pageable);
        }

        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar tarefas por status", description = "Lista todas as tarefas com um status específico")
    public ResponseEntity<List<TarefaDTO>> buscarPorStatus(@PathVariable StatusTarefa status) {
        log.info("Recebendo requisição para buscar tarefas com status: {}", status);
        List<TarefaDTO> tarefas = tarefaService.buscarPorStatus(status);
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/prioridade/{prioridade}")
    @Operation(summary = "Buscar tarefas por prioridade", description = "Lista todas as tarefas com uma prioridade específica")
    public ResponseEntity<List<TarefaDTO>> buscarPorPrioridade(@PathVariable PrioridadeTarefa prioridade) {
        log.info("Recebendo requisição para buscar tarefas com prioridade: {}", prioridade);
        List<TarefaDTO> tarefas = tarefaService.buscarPorPrioridade(prioridade);
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/usuario/{usuario}")
    @Operation(summary = "Buscar tarefas por usuário", description = "Lista todas as tarefas de um usuário específico")
    public ResponseEntity<List<TarefaDTO>> buscarPorUsuario(@PathVariable String usuario) {
        log.info("Recebendo requisição para buscar tarefas do usuário: {}", usuario);
        List<TarefaDTO> tarefas = tarefaService.buscarPorUsuario(usuario);
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/busca")
    @Operation(summary = "Buscar tarefas por texto", description = "Busca tarefas por texto no título ou descrição")
    public ResponseEntity<Page<TarefaDTO>> buscarPorTexto(
            @Parameter(description = "Texto para busca") @RequestParam String texto,
            @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size) {

        log.info("Recebendo requisição para buscar tarefas com texto: {}", texto);
        Pageable pageable = PageRequest.of(page, size);
        Page<TarefaDTO> tarefas = tarefaService.buscarPorTexto(texto, pageable);
        return ResponseEntity.ok(tarefas);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tarefa", description = "Atualiza uma tarefa existente")
    public ResponseEntity<TarefaDTO> atualizarTarefa(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarTarefaRequest request) {

        log.info("Recebendo requisição para atualizar tarefa com ID: {}", id);
        TarefaDTO tarefa = tarefaService.atualizarTarefa(id, request);
        return ResponseEntity.ok(tarefa);
    }

    @PatchMapping("/{id}/concluir")
    @Operation(summary = "Marcar tarefa como concluída", description = "Marca uma tarefa como concluída")
    public ResponseEntity<TarefaDTO> marcarComoConcluida(@PathVariable Long id) {
        log.info("Recebendo requisição para marcar tarefa como concluída. ID: {}", id);
        TarefaDTO tarefa = tarefaService.marcarComoConcluida(id);
        return ResponseEntity.ok(tarefa);
    }

    @PatchMapping("/{id}/andamento")
    @Operation(summary = "Marcar tarefa como em andamento", description = "Marca uma tarefa como em andamento")
    public ResponseEntity<TarefaDTO> marcarComoEmAndamento(@PathVariable Long id) {
        log.info("Recebendo requisição para marcar tarefa como em andamento. ID: {}", id);
        TarefaDTO tarefa = tarefaService.marcarComoEmAndamento(id);
        return ResponseEntity.ok(tarefa);
    }

    @PatchMapping("/{id}/pendente")
    @Operation(summary = "Marcar tarefa como pendente", description = "Marca uma tarefa como pendente")
    public ResponseEntity<TarefaDTO> marcarComoPendente(@PathVariable Long id) {
        log.info("Recebendo requisição para marcar tarefa como pendente. ID: {}", id);
        TarefaDTO tarefa = tarefaService.marcarComoPendente(id);
        return ResponseEntity.ok(tarefa);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir tarefa", description = "Exclui uma tarefa do sistema")
    public ResponseEntity<Void> excluirTarefa(@PathVariable Long id) {
        log.info("Recebendo requisição para excluir tarefa com ID: {}", id);
        tarefaService.excluirTarefa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vencidas")
    @Operation(summary = "Buscar tarefas vencidas", description = "Lista tarefas que podem estar vencidas")
    public ResponseEntity<List<TarefaDTO>> buscarTarefasVencidas() {
        log.info("Recebendo requisição para buscar tarefas vencidas");
        List<TarefaDTO> tarefas = tarefaService.buscarTarefasVencidas();
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/estatisticas")
    @Operation(summary = "Obter estatísticas", description = "Retorna estatísticas das tarefas")
    public ResponseEntity<Object> obterEstatisticas() {
        log.info("Recebendo requisição para obter estatísticas");
        Object estatisticas = tarefaService.obterEstatisticas();
        return ResponseEntity.ok(estatisticas);
    }
}
