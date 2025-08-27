package com.documents.tarefas.dto;

import com.documents.tarefas.model.PrioridadeTarefa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriarTarefaRequest {
    
    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    private String titulo;
    
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;
    
    @NotNull(message = "A prioridade é obrigatória")
    private PrioridadeTarefa prioridade = PrioridadeTarefa.MEDIA;
    
    private String usuarioResponsavel;
    private String categoria;
    private String tags;
    private Integer estimativaHoras;
    private String observacoes;
}
