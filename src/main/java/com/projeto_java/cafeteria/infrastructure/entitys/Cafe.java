
package com.projeto_java.cafeteria.infrastructure.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tabela_cafe", schema = "public")
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome_produto", nullable = false, length = 255)
    private String nomeProduto;

    @Column(name = "tamanho_produto", nullable = false, length = 50)
    private String tamanhoProduto;

    @Column(name = "tipo_torra", nullable = false, length = 50)
    private String tipoTorra;

    @Column(name = "preco", nullable = false)
    private Double preco;
}
