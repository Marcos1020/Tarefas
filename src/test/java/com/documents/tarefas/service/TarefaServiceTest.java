package com.documents.tarefas.service;

import com.documents.tarefas.dto.CriarTarefaRequest;
import com.documents.tarefas.dto.TarefaDTO;
import com.documents.tarefas.exception.TarefaJaExisteException;
import com.documents.tarefas.exception.TarefaNaoEncontradaException;
import com.documents.tarefas.model.PrioridadeTarefa;
import com.documents.tarefas.model.StatusTarefa;
import com.documents.tarefas.model.Tarefa;
import com.documents.tarefas.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {
    
    @Mock
    private TarefaRepository tarefaRepository;
    
    @InjectMocks
    private TarefaService tarefaService;
    
    private Tarefa tarefa;
    private CriarTarefaRequest criarRequest;
    private TarefaDTO tarefaDTO;
    
    @BeforeEach
    void setUp() {
        tarefa = new Tarefa();
        tarefa.setId(1L);
        tarefa.setTitulo("Teste de Tarefa");
        tarefa.setDescricao("Descrição da tarefa de teste");
        tarefa.setStatus(StatusTarefa.PENDENTE);
        tarefa.setPrioridade(PrioridadeTarefa.MEDIA);
        tarefa.setDataCriacao(LocalDateTime.now());
        tarefa.setUsuarioResponsavel("João Silva");
        
        criarRequest = new CriarTarefaRequest();
        criarRequest.setTitulo("Nova Tarefa");
        criarRequest.setDescricao("Descrição da nova tarefa");
        criarRequest.setPrioridade(PrioridadeTarefa.ALTA);
        criarRequest.setUsuarioResponsavel("Maria Santos");
        
        tarefaDTO = new TarefaDTO();
        tarefaDTO.setId(1L);
        tarefaDTO.setTitulo("Teste de Tarefa");
        tarefaDTO.setDescricao("Descrição da tarefa de teste");
        tarefaDTO.setStatus(StatusTarefa.PENDENTE);
        tarefaDTO.setPrioridade(PrioridadeTarefa.MEDIA);
        tarefaDTO.setDataCriacao("01/01/2024");
        tarefaDTO.setDataAtualizacao("01/01/2024");
    }
    
    @Test
    void criarTarefa_DeveCriarTarefaComSucesso() {

        when(tarefaRepository.existsByTitulo(anyString())).thenReturn(false);
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        TarefaDTO resultado = tarefaService.criarTarefa(criarRequest);

        assertNotNull(resultado);
        assertEquals(tarefa.getTitulo(), resultado.getTitulo());
        assertEquals(tarefa.getDescricao(), resultado.getDescricao());
        assertEquals(tarefa.getPrioridade(), resultado.getPrioridade());
        assertEquals(tarefa.getUsuarioResponsavel(), resultado.getUsuarioResponsavel());
        
        verify(tarefaRepository).existsByTitulo(criarRequest.getTitulo());
        verify(tarefaRepository).save(any(Tarefa.class));
    }
    
    @Test
    void criarTarefa_DeveLancarExcecaoQuandoTituloJaExiste() {

        when(tarefaRepository.existsByTitulo(anyString())).thenReturn(true);

        assertThrows(TarefaJaExisteException.class, () -> {
            tarefaService.criarTarefa(criarRequest);
        });
        
        verify(tarefaRepository).existsByTitulo(criarRequest.getTitulo());
        verify(tarefaRepository, never()).save(any(Tarefa.class));
    }
    
    @Test
    void buscarPorId_DeveRetornarTarefaQuandoExiste() {

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));

        TarefaDTO resultado = tarefaService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(tarefa.getId(), resultado.getId());
        assertEquals(tarefa.getTitulo(), resultado.getTitulo());
        
        verify(tarefaRepository).findById(1L);
    }
    
    @Test
    void buscarPorId_DeveLancarExcecaoQuandoTarefaNaoExiste() {

        when(tarefaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(TarefaNaoEncontradaException.class, () -> {
            tarefaService.buscarPorId(999L);
        });
        
        verify(tarefaRepository).findById(999L);
    }
    
    @Test
    void listarTarefas_DeveRetornarPaginaDeTarefas() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Tarefa> paginaTarefas = new PageImpl<>(Arrays.asList(tarefa));
        when(tarefaRepository.findAll(pageable)).thenReturn(paginaTarefas);

        Page<TarefaDTO> resultado = tarefaService.listarTarefas(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(tarefa.getTitulo(), resultado.getContent().get(0).getTitulo());
        
        verify(tarefaRepository).findAll(pageable);
    }
    
    @Test
    void buscarPorStatus_DeveRetornarTarefasDoStatus() {

        List<Tarefa> tarefas = Arrays.asList(tarefa);
        when(tarefaRepository.findByStatusOrderByPrioridadeAndDataCriacao(StatusTarefa.PENDENTE))
                .thenReturn(tarefas);

        List<TarefaDTO> resultado = tarefaService.buscarPorStatus(StatusTarefa.PENDENTE);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(tarefa.getStatus(), resultado.get(0).getStatus());
        
        verify(tarefaRepository).findByStatusOrderByPrioridadeAndDataCriacao(StatusTarefa.PENDENTE);
    }
    
    @Test
    void marcarComoConcluida_DeveMarcarTarefaComoConcluida() {
        // Arrange
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        TarefaDTO resultado = tarefaService.marcarComoConcluida(1L);
        
        assertNotNull(resultado);
        assertEquals(StatusTarefa.CONCLUIDA, tarefa.getStatus());
        assertNotNull(tarefa.getDataConclusao());
        
        verify(tarefaRepository).findById(1L);
        verify(tarefaRepository).save(tarefa);
    }
    
    @Test
    void marcarComoConcluida_DeveLancarExcecaoQuandoTarefaNaoExiste() {

        when(tarefaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(TarefaNaoEncontradaException.class, () -> {
            tarefaService.marcarComoConcluida(999L);
        });
        
        verify(tarefaRepository).findById(999L);
        verify(tarefaRepository, never()).save(any(Tarefa.class));
    }
    
    @Test
    void excluirTarefa_DeveExcluirTarefaComSucesso() {

        when(tarefaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(tarefaRepository).deleteById(1L);

        assertDoesNotThrow(() -> tarefaService.excluirTarefa(1L));

        verify(tarefaRepository).existsById(1L);
        verify(tarefaRepository).deleteById(1L);
    }
    
    @Test
    void excluirTarefa_DeveLancarExcecaoQuandoTarefaNaoExiste() {

        when(tarefaRepository.existsById(999L)).thenReturn(false);

        assertThrows(TarefaNaoEncontradaException.class, () -> {
            tarefaService.excluirTarefa(999L);
        });
        
        verify(tarefaRepository).existsById(999L);
        verify(tarefaRepository, never()).deleteById(anyLong());
    }
}
