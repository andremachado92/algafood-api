package com.algaworks.algafood.domain.sevice;

import com.algaworks.algafood.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FluxoPedidoService {

    @Autowired
    EmissaoPedidoService emissaoPedidoService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional
    public void confirmar(String codigoPedido){
        var pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
       pedido.confirmar();
       pedidoRepository.save(pedido);
    }

    @Transactional
    public void cancelar(String codigoPedido){
        var pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
       pedido.cancelar();
    }

    @Transactional
    public void entregar(String codigoPedido){
        var pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
         pedido.entregar();
    }
}
