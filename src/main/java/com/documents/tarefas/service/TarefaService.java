package com.documents.tarefas.service;

import com.documents.tarefas.dto.AtualizarTarefaRequest;
import com.documents.tarefas.dto.CriarTarefaRequest;
import com.documents.tarefas.dto.TarefaDTO;
import com.documents.tarefas.exception.TarefaNaoEncontradaException;
import com.documents.tarefas.exception.TarefaJaExisteException;
import com.documents.tarefas.model.PrioridadeTarefa;
import com.documents.tarefas.model.StatusTarefa;
import com.documents.tarefas.model.Tarefa;
import com.documents.tarefas.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaDTO criarTarefa(CriarTarefaRequest request) {
        log.info("Criando nova tarefa: {}", request.getTitulo());

        if (tarefaRepository.existsByTitulo(request.getTitulo())) {
            throw new TarefaJaExisteException("Já existe uma tarefa com o título: " + request.getTitulo());
        }

        Tarefa tarefa = getTarefa(request);

        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
        log.info("Tarefa criada com sucesso. ID: {}", tarefaSalva.getId());

        return converterParaDTO(tarefaSalva);
    }

    @Transactional(readOnly = true)
    public TarefaDTO buscarPorId(Long id) {
        log.info("Buscando tarefa com ID: {}", id);

        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada com ID: " + id));

        return converterParaDTO(tarefa);
    }

    @Transactional(readOnly = true)
    public Page<TarefaDTO> listarTarefas(Pageable pageable) {
        log.info("Listando tarefas com paginação: {}", pageable);

        Page<Tarefa> tarefas = tarefaRepository.findAll(pageable);
        return tarefas.map(this::converterParaDTO);
    }

    @Transactional(readOnly = true)
    public Page<TarefaDTO> listarTarefasComFiltros(StatusTarefa status,
            PrioridadeTarefa prioridade,
            String usuario,
            String categoria,
            Pageable pageable) {
        log.info("Listando tarefas com filtros - Status: {}, Prioridade: {}, Usuário: {}, Categoria: {}",
                status, prioridade, usuario, categoria);

        Page<Tarefa> tarefas = tarefaRepository.findByFiltros(status, prioridade, usuario, categoria, pageable);
        return tarefas.map(this::converterParaDTO);
    }

    @Transactional(readOnly = true)
    public List<TarefaDTO> buscarPorStatus(StatusTarefa status) {
        log.info("Buscando tarefas com status: {}", status);

        List<Tarefa> tarefas = tarefaRepository.findByStatusOrderByPrioridadeAndDataCriacao(status);
        return tarefas.stream().map(this::converterParaDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TarefaDTO> buscarPorPrioridade(PrioridadeTarefa prioridade) {
        log.info("Buscando tarefas com prioridade: {}", prioridade);

        List<Tarefa> tarefas = tarefaRepository.findByPrioridade(prioridade);
        return tarefas.stream().map(this::converterParaDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TarefaDTO> buscarPorUsuario(String usuario) {
        log.info("Buscando tarefas do usuário: {}", usuario);

        List<Tarefa> tarefas = tarefaRepository.findByUsuarioResponsavel(usuario);
        return tarefas.stream().map(this::converterParaDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<TarefaDTO> buscarPorTexto(String texto, Pageable pageable) {
        log.info("Buscando tarefas com texto: {}", texto);

        Page<Tarefa> tarefas = tarefaRepository.findByTexto(texto, pageable);
        return tarefas.map(this::converterParaDTO);
    }

    public TarefaDTO atualizarTarefa(Long id, AtualizarTarefaRequest request) {
        log.info("Atualizando tarefa com ID: {}", id);

        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada com ID: " + id));

        if (request.getTitulo() != null) {
            tarefa.setTitulo(request.getTitulo());
        }
        if (request.getDescricao() != null) {
            tarefa.setDescricao(request.getDescricao());
        }
        if (request.getStatus() != null) {
            tarefa.setStatus(request.getStatus());

            if (request.getStatus() == StatusTarefa.CONCLUIDA) {
                tarefa.marcarComoConcluida();
            }
        }
        if (request.getPrioridade() != null) {
            tarefa.setPrioridade(request.getPrioridade());
        }
        if (request.getUsuarioResponsavel() != null) {
            tarefa.setUsuarioResponsavel(request.getUsuarioResponsavel());
        }
        if (request.getCategoria() != null) {
            tarefa.setCategoria(request.getCategoria());
        }
        if (request.getTags() != null) {
            tarefa.setTags(request.getTags());
        }
        if (request.getEstimativaHoras() != null) {
            tarefa.setEstimativaHoras(request.getEstimativaHoras());
        }
        if (request.getTempoRealHoras() != null) {
            tarefa.setTempoRealHoras(request.getTempoRealHoras());
        }
        if (request.getObservacoes() != null) {
            tarefa.setObservacoes(request.getObservacoes());
        }

        Tarefa tarefaAtualizada = tarefaRepository.save(tarefa);
        log.info("Tarefa atualizada com sucesso. ID: {}", tarefaAtualizada.getId());

        return converterParaDTO(tarefaAtualizada);
    }

    public TarefaDTO marcarComoConcluida(Long id) {
        log.info("Marcando tarefa como concluída. ID: {}", id);

        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada com ID: " + id));

        tarefa.marcarComoConcluida();
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);

        log.info("Tarefa marcada como concluída. ID: {}", tarefaSalva.getId());
        return converterParaDTO(tarefaSalva);
    }

    public TarefaDTO marcarComoEmAndamento(Long id) {
        log.info("Marcando tarefa como em andamento. ID: {}", id);

        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada com ID: " + id));

        tarefa.marcarComoEmAndamento();
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);

        log.info("Tarefa marcada como em andamento. ID: {}", tarefaSalva.getId());
        return converterParaDTO(tarefaSalva);
    }

    public TarefaDTO marcarComoPendente(Long id) {
        log.info("Marcando tarefa como pendente. ID: {}", id);

        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada com ID: " + id));

        tarefa.marcarComoPendente();
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);

        log.info("Tarefa marcada como pendente. ID: {}", tarefaSalva.getId());
        return converterParaDTO(tarefaSalva);
    }

    public void excluirTarefa(Long id) {
        log.info("Excluindo tarefa com ID: {}", id);

        if (!tarefaRepository.existsById(id)) {
            throw new TarefaNaoEncontradaException("Tarefa não encontrada com ID: " + id);
        }

        tarefaRepository.deleteById(id);
        log.info("Tarefa excluída com sucesso. ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<TarefaDTO> buscarTarefasVencidas() {
        log.info("Buscando tarefas vencidas");

        LocalDateTime dataLimite = LocalDateTime.now().minusDays(7); // Tarefas criadas há mais de 7 dias
        List<Tarefa> tarefas = tarefaRepository.findTarefasVencidas(dataLimite);

        return tarefas.stream().map(this::converterParaDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> obterEstatisticas() {
        log.info("Obtendo estatísticas das tarefas");

        List<Object[]> statusCount = tarefaRepository.countByStatus();
        List<Object[]> prioridadeCount = tarefaRepository.countByPrioridade();

        return Map.of(
                "status", statusCount,
                "prioridade", prioridadeCount,
                "total", tarefaRepository.count());
    }

    private Tarefa getTarefa(CriarTarefaRequest request) {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(request.getTitulo());
        tarefa.setDescricao(request.getDescricao());
        tarefa.setPrioridade(request.getPrioridade());
        tarefa.setUsuarioResponsavel(request.getUsuarioResponsavel());
        tarefa.setCategoria(request.getCategoria());
        tarefa.setTags(request.getTags());
        tarefa.setEstimativaHoras(request.getEstimativaHoras());
        tarefa.setObservacoes(request.getObservacoes());
        return tarefa;
    }

    private TarefaDTO converterParaDTO(Tarefa tarefa) {
        TarefaDTO dto = new TarefaDTO();
        dto.setId(tarefa.getId());
        dto.setTitulo(tarefa.getTitulo());
        dto.setDescricao(tarefa.getDescricao());
        dto.setStatus(tarefa.getStatus());
        dto.setPrioridade(tarefa.getPrioridade());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        if (tarefa.getDataCriacao() != null) {
            dto.setDataCriacao(tarefa.getDataCriacao().format(formatter));
        }
        if (tarefa.getDataAtualizacao() != null) {
            dto.setDataAtualizacao(tarefa.getDataAtualizacao().format(formatter));
        }
        if (tarefa.getDataConclusao() != null) {
            dto.setDataConclusao(tarefa.getDataConclusao().format(formatter));
        }
        
        dto.setUsuarioResponsavel(tarefa.getUsuarioResponsavel());
        dto.setCategoria(tarefa.getCategoria());
        dto.setTags(tarefa.getTags());
        dto.setEstimativaHoras(tarefa.getEstimativaHoras());
        dto.setTempoRealHoras(tarefa.getTempoRealHoras());
        dto.setObservacoes(tarefa.getObservacoes());

        return dto;
    }
}
