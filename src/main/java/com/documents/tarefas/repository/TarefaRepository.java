package com.documents.tarefas.repository;

import com.documents.tarefas.model.PrioridadeTarefa;
import com.documents.tarefas.model.StatusTarefa;
import com.documents.tarefas.model.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    
    
    List<Tarefa> findByStatus(StatusTarefa status);

    List<Tarefa> findByPrioridade(PrioridadeTarefa prioridade);

    List<Tarefa> findByUsuarioResponsavel(String usuarioResponsavel);

    List<Tarefa> findByCategoria(String categoria);

    List<Tarefa> findByStatusAndPrioridade(StatusTarefa status, PrioridadeTarefa prioridade);
    
    @Query("SELECT t FROM Tarefa t WHERE t.status = :status ORDER BY " +
           "CASE t.prioridade " +
           "WHEN 'URGENTE' THEN 1 " +
           "WHEN 'ALTA' THEN 2 " +
           "WHEN 'MEDIA' THEN 3 " +
           "WHEN 'BAIXA' THEN 4 " +
           "END, t.dataCriacao ASC")
    List<Tarefa> findByStatusOrderByPrioridadeAndDataCriacao(@Param("status") StatusTarefa status);

    @Query("SELECT t FROM Tarefa t WHERE t.dataCriacao BETWEEN :dataInicio AND :dataFim")
    List<Tarefa> findByPeriodo(@Param("dataInicio") LocalDateTime dataInicio, 
                               @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT t FROM Tarefa t WHERE LOWER(t.titulo) LIKE LOWER(CONCAT('%', :texto, '%')) " +
           "OR LOWER(t.descricao) LIKE LOWER(CONCAT('%', :texto, '%'))")
    Page<Tarefa> findByTexto(@Param("texto") String texto, Pageable pageable);

    @Query("SELECT t FROM Tarefa t WHERE t.status = 'PENDENTE' " +
           "AND t.estimativaHoras IS NOT NULL " +
           "AND t.dataCriacao < :dataLimite")
    List<Tarefa> findTarefasVencidas(@Param("dataLimite") LocalDateTime dataLimite);

    @Query("SELECT t.status, COUNT(t) FROM Tarefa t GROUP BY t.status")
    List<Object[]> countByStatus();

    @Query("SELECT t.prioridade, COUNT(t) FROM Tarefa t GROUP BY t.prioridade")
    List<Object[]> countByPrioridade();

    @Query("SELECT t FROM Tarefa t WHERE " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:prioridade IS NULL OR t.prioridade = :prioridade) AND " +
           "(:usuario IS NULL OR t.usuarioResponsavel = :usuario) AND " +
           "(:categoria IS NULL OR t.categoria = :categoria)")
    Page<Tarefa> findByFiltros(@Param("status") StatusTarefa status,
                                @Param("prioridade") PrioridadeTarefa prioridade,
                                @Param("usuario") String usuario,
                                @Param("categoria") String categoria,
                                Pageable pageable);

    boolean existsByTitulo(String titulo);

    Optional<Tarefa> findByTitulo(String titulo);
}
