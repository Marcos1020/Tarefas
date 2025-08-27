package com.documents.tarefas.dto;

import com.documents.tarefas.model.PrioridadeTarefa;
import com.documents.tarefas.model.StatusTarefa;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarTarefaRequest {

    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    private String titulo;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;

    private StatusTarefa status;
    private PrioridadeTarefa prioridade;
    private String usuarioResponsavel;
    private String categoria;
    private String tags;
    private Integer estimativaHoras;
    private Integer tempoRealHoras;
    private String observacoes;
}
