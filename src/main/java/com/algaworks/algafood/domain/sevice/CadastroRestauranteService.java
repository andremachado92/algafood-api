package com.algaworks.algafood.domain.sevice;

import com.algaworks.algafood.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.domain.model.*;
import com.algaworks.algafood.domain.model.dto.input.RestauranteInput;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.exceptions.CidadeNaoEncontradaException;
import com.algaworks.algafood.exceptions.CozinhaNaoEncontradaException;
import com.algaworks.algafood.exceptions.EntidadeEmUsoException;
import com.algaworks.algafood.exceptions.RestauranteNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CadastroRestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private RestauranteInputDisassembler restauranteInputDisassembler;

    @Autowired
    private CadastroFormaPagamento cadastroFormaPagamento;

    @Autowired
    private CadastroUsuarioService cadastroUsuario;

    @Transactional
    public Restaurante salvar(Restaurante restaurante) {
        var cozinha = buscarCozinha(restaurante.getCozinha().getId());
        var cidade = buscarCidade(restaurante.getEndereco().getCidade().getId());
        restaurante.setCozinha(cozinha);
        restaurante.getEndereco().setCidade(cidade);
        return restauranteRepository.save(restaurante);
    }

    @Transactional
    public Restaurante atualizar(Long id, RestauranteInput restauranteInput) {
        var restauranteAtual = buscarOuFalhar(id);

        restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);

        return salvar(restauranteAtual);
    }

    @Transactional
    public void deletar(Long id) {
        var restaurante = buscarOuFalhar(id);
        try {
            restauranteRepository.delete(restaurante);
            restauranteRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException("Restaurante não pode ser excluído pois");
        }

    }

    @Transactional
    public void ativar(Long restauranteId) {
        var restauranteAtual = buscarOuFalhar(restauranteId);
        restauranteAtual.ativar();
    }

    @Transactional
    public void inativar(Long restauranteId) {
        var restauranteAtual = buscarOuFalhar(restauranteId);
        restauranteAtual.inativar();
    }

    @Transactional
    public void desassociarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
        Restaurante restaurante = buscarOuFalhar(restauranteId);
        FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOufalhar(formaPagamentoId);

        restaurante.removerFormaPagamento(formaPagamento);
    }

    @Transactional
    public void associarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
        Restaurante restaurante = buscarOuFalhar(restauranteId);
        FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOufalhar(formaPagamentoId);

        restaurante.adicionarFormaPagamento(formaPagamento);
    }

    @Transactional
    public void abrir(Long restauranteId) {
        var restaurante = buscarOuFalhar(restauranteId);
        restaurante.abrir();
    }

    @Transactional
    public void fechar(Long restauranteId) {
        var restaurante = buscarOuFalhar(restauranteId);
        restaurante.fechar();
    }


    public Restaurante buscarOuFalhar(Long restauranteId) {
        return restauranteRepository.findById(restauranteId).
                orElseThrow(() -> new RestauranteNaoEncontradoException("Restaurante não encontrado"));
    }

    public Cozinha buscarCozinha(Long id) {
        return cozinhaRepository.findById(id).
                orElseThrow(() -> new CozinhaNaoEncontradaException("Cozinha não encontrada para o id: "
                        + id));
    }

    public Cidade buscarCidade(Long id) {
        return cidadeRepository.findById(id).
                orElseThrow(() -> new CidadeNaoEncontradaException("Cidade não encontrada para o id: "
                        + id));
    }

    @Transactional
    public void desassociarResponsavel(Long restauranteId, Long usuarioId) {
        Restaurante restaurante = buscarOuFalhar(restauranteId);
        Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);

        restaurante.removerResponsavel(usuario);
    }

    @Transactional
    public void associarResponsavel(Long restauranteId, Long usuarioId) {
        Restaurante restaurante = buscarOuFalhar(restauranteId);
        Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);

        restaurante.adicionarResponsavel(usuario);
    }

    @Transactional
    public void ativar(List<Long> restauranteIds) {
        restauranteIds.forEach(this::ativar);
    }

    @Transactional
    public void inativar(List<Long> restauranteIds) {
        restauranteIds.forEach(this::inativar);
    }
}
