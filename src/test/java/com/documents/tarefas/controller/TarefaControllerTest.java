package com.documents.tarefas.controller;

import com.documents.tarefas.dto.CriarTarefaRequest;
import com.documents.tarefas.dto.TarefaDTO;
import com.documents.tarefas.model.PrioridadeTarefa;
import com.documents.tarefas.model.StatusTarefa;
import com.documents.tarefas.service.TarefaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TarefaController.class)
class TarefaControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private TarefaService tarefaService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private CriarTarefaRequest criarRequest;
    private TarefaDTO tarefaDTO;
    
    @BeforeEach
    void setUp() {
        criarRequest = new CriarTarefaRequest();
        criarRequest.setTitulo("Nova Tarefa");
        criarRequest.setDescricao("Descrição da nova tarefa");
        criarRequest.setPrioridade(PrioridadeTarefa.ALTA);
        criarRequest.setUsuarioResponsavel("João Silva");
        
        tarefaDTO = new TarefaDTO();
        tarefaDTO.setId(1L);
        tarefaDTO.setTitulo("Nova Tarefa");
        tarefaDTO.setDescricao("Descrição da nova tarefa");
        tarefaDTO.setStatus(StatusTarefa.PENDENTE);
        tarefaDTO.setPrioridade(PrioridadeTarefa.ALTA);
        tarefaDTO.setUsuarioResponsavel("João Silva");
        tarefaDTO.setDataCriacao(LocalDateTime.now());
    }
    
    @Test
    void criarTarefa_DeveRetornar201QuandoCriadaComSucesso() throws Exception {
        
        when(tarefaService.criarTarefa(any(CriarTarefaRequest.class))).thenReturn(tarefaDTO);

        mockMvc.perform(post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Nova Tarefa"))
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.prioridade").value("ALTA"));
    }
    
    @Test
    void criarTarefa_DeveRetornar400QuandoDadosInvalidos() throws Exception {

        CriarTarefaRequest requestInvalido = new CriarTarefaRequest();
        requestInvalido.setTitulo(""); // Título vazio

        mockMvc.perform(post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void buscarPorId_DeveRetornar200QuandoTarefaExiste() throws Exception {

        when(tarefaService.buscarPorId(1L)).thenReturn(tarefaDTO);

        mockMvc.perform(get("/api/tarefas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Nova Tarefa"));
    }
    
    @Test
    void listarTarefas_DeveRetornar200ComPaginaVazia() throws Exception {

        mockMvc.perform(get("/api/tarefas")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }
    
    @Test
    void marcarComoConcluida_DeveRetornar200QuandoSucesso() throws Exception {

        TarefaDTO tarefaConcluida = new TarefaDTO();
        tarefaConcluida.setId(1L);
        tarefaConcluida.setStatus(StatusTarefa.CONCLUIDA);
        tarefaConcluida.setDataConclusao(LocalDateTime.now());
        
        when(tarefaService.marcarComoConcluida(1L)).thenReturn(tarefaConcluida);

        mockMvc.perform(patch("/api/tarefas/1/concluir"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONCLUIDA"));
    }
    
    @Test
    void excluirTarefa_DeveRetornar204QuandoExcluidaComSucesso() throws Exception {

        mockMvc.perform(delete("/api/tarefas/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void healthCheck_DeveRetornar200() throws Exception {

        mockMvc.perform(get("/api/tarefas/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("API de Tarefas funcionando normalmente!"));
    }
}
