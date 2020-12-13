package com.algaworks.algafood.domain.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FluxoPedidoService {

    @Autowired
    EmissaoPedidoService emissaoPedidoService;

    @Transactional
    public void confirmar(String codigoPedido){
        var pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
       pedido.confirmar();

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
