package com.erica.order.infrastructure.adapter.out.persistence;

import com.erica.order.application.port.out.PedidoRepositoryPort;
import com.erica.order.domain.model.Dinheiro;
import com.erica.order.domain.model.ItemPedido;
import com.erica.order.domain.model.Pedido;
import com.erica.order.infrastructure.adapter.out.persistence.entity.ItemPedidoJpaEntity;
import com.erica.order.infrastructure.adapter.out.persistence.entity.PedidoJpaEntity;
import com.erica.order.infrastructure.adapter.out.persistence.repository.SpringDataPedidoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class PedidoPersistenceAdapter implements PedidoRepositoryPort {

    private final SpringDataPedidoRepository springDataRepository;

    public PedidoPersistenceAdapter(SpringDataPedidoRepository springDataRepository) {
        this.springDataRepository = Objects.requireNonNull(springDataRepository, "SpringDataPedidoRepository é obrigatório");
    }

    @Override
    public Pedido salvar(Pedido pedido) {
        PedidoJpaEntity jpaEntity = new PedidoJpaEntity(
            pedido.getId(),
            pedido.getClienteId(),
            pedido.getValorTotal().valor(),
            pedido.getStatus(),
            pedido.getCriadoEm(),
            pedido.getAtualizadoEm()
        );

        for (ItemPedido item : pedido.getItens()) {
            ItemPedidoJpaEntity itemJpa = new ItemPedidoJpaEntity(
                UUID.randomUUID(),
                item.produtoId(),
                item.nomeProduto(),
                item.quantidade(),
                item.precoUnitario().valor()
            );
            jpaEntity.adicionarItem(itemJpa);
        }

        PedidoJpaEntity salvo = springDataRepository.save(jpaEntity);
        return mapearParaDominio(salvo);
    }

    @Override
    public Optional<Pedido> buscarPorId(UUID id) {
        return springDataRepository.findById(id).map(this::mapearParaDominio);
    }

    private Pedido mapearParaDominio(PedidoJpaEntity entity) {
        List<ItemPedido> itensDominio = entity.getItens().stream()
            .map(item -> new ItemPedido(
                item.getProdutoId(),
                item.getNomeProduto(),
                item.getQuantidade(),
                Dinheiro.de(item.getPrecoUnitario())
            ))
            .toList();

        return new Pedido(
            entity.getId(),
            entity.getClienteId(),
            itensDominio,
            Dinheiro.de(entity.getValorTotal()),
            entity.getStatus(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm()
        );
    }
}
