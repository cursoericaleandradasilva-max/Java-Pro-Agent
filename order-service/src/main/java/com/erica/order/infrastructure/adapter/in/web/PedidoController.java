package com.erica.order.infrastructure.adapter.in.web;

import com.erica.order.application.port.in.CriarPedidoCommand;
import com.erica.order.application.port.in.CriarPedidoUseCase;
import com.erica.order.domain.model.Pedido;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final CriarPedidoUseCase criarPedidoUseCase;

    public PedidoController(CriarPedidoUseCase criarPedidoUseCase) {
        this.criarPedidoUseCase = Objects.requireNonNull(criarPedidoUseCase, "CriarPedidoUseCase é obrigatório");
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody CriarPedidoRequest request) {
        var command = new CriarPedidoCommand(
            request.clienteId(),
            request.itens().stream()
                .map(item -> new CriarPedidoCommand.ItemCommand(
                    item.produtoId(),
                    item.nomeProduto(),
                    item.quantidade(),
                    item.precoUnitario()
                ))
                .toList()
        );

        Pedido pedidoCriado = criarPedidoUseCase.executar(command);
        PedidoResponse response = PedidoResponse.aPartirDe(pedidoCriado);

        URI location = URI.create("/api/v1/pedidos/" + response.id());
        return ResponseEntity.created(location).body(response);
    }
}
