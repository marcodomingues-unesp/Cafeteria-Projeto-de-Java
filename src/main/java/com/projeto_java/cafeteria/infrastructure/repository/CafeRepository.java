
package com.projeto_java.cafeteria.infrastructure.repository;

import com.projeto_java.cafeteria.infrastructure.entitys.Cafe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Integer> {

    // ==================== LISTAR ====================

    @Query("""
            SELECT c
            FROM Cafe c
            ORDER BY c.id ASC
            """)
    List<Cafe> selecionarCafe();

    // ==================== EXCLUIR TODOS ====================

    @Modifying(
            clearAutomatically = true,
            flushAutomatically = true
    )
    @Transactional
    @Query(
            value = """
                    TRUNCATE TABLE public.tabela_cafe
                    RESTART IDENTITY
                    """,
            nativeQuery = true
    )
    void excluirTodosPedidos();
}
