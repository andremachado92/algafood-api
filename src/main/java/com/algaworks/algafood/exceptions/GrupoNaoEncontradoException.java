package com.algaworks.algafood.exceptions;

public class GrupoNaoEncontradoException extends EntidadeNaoEncontradaException {
    public GrupoNaoEncontradoException(String msg){
        super(msg);
    }

    public GrupoNaoEncontradoException(Long id) {
        this(String.format("Não existe um cadastro de grupo com código %d", id));
    }
}