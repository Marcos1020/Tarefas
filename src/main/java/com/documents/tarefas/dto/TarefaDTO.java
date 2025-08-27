package com.documents.tarefas.dto;

import com.documents.tarefas.model.PrioridadeTarefa;
import com.documents.tarefas.model.StatusTarefa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaDTO {
    
    private Long id;
    
    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    private String titulo;
    
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;
    
    @NotNull(message = "O status é obrigatório")
    private StatusTarefa status;
    
    @NotNull(message = "A prioridade é obrigatória")
    private PrioridadeTarefa prioridade;
    
    private String dataCriacao;
    private String dataAtualizacao;
    private String dataConclusao;
    private String usuarioResponsavel;
    private String categoria;
    private String tags;
    private Integer estimativaHoras;
    private Integer tempoRealHoras;
    private String observacoes;
}
