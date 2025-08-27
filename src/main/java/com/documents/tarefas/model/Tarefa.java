package com.documents.tarefas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarefas_tb")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarefa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String titulo;
    
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String descricao;
    
    @NotNull(message = "O status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTarefa status = StatusTarefa.PENDENTE;
    
    @NotNull(message = "A prioridade é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadeTarefa prioridade = PrioridadeTarefa.MEDIA;
    
    @Column(name = "data_criacao")
    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    @UpdateTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataAtualizacao;
    
    @Column(name = "data_conclusao")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataConclusao;
    
    @Column(name = "usuario_responsavel", length = 100)
    private String usuarioResponsavel;
    
    @Column(name = "categoria", length = 50)
    private String categoria;
    
    @Column(name = "tags", length = 200)
    private String tags;
    
    @Column(name = "estimativa_horas")
    private Integer estimativaHoras;
    
    @Column(name = "tempo_real_horas")
    private Integer tempoRealHoras;
    
    @Column(name = "observacoes", length = 1000)
    private String observacoes;

    public void marcarComoConcluida() {
        this.status = StatusTarefa.CONCLUIDA;
        this.dataConclusao = LocalDateTime.now();
    }
    
    public void marcarComoEmAndamento() {
        this.status = StatusTarefa.EM_ANDAMENTO;
    }
    
    public void marcarComoPendente() {
        this.status = StatusTarefa.PENDENTE;
        this.dataConclusao = null;
    }
    
    public boolean estaConcluida() {
        return StatusTarefa.CONCLUIDA.equals(this.status);
    }
    
    public boolean estaEmAndamento() {
        return StatusTarefa.EM_ANDAMENTO.equals(this.status);
    }
    
    public boolean estaPendente() {
        return StatusTarefa.PENDENTE.equals(this.status);
    }
}
