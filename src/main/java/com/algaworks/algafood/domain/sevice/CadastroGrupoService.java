package com.algaworks.algafood.domain.sevice;

import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import com.algaworks.algafood.exceptions.CidadeNaoEncontradaException;
import com.algaworks.algafood.exceptions.EntidadeEmUsoException;
import com.algaworks.algafood.exceptions.GrupoNaoEncontradoException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CadastroGrupoService {
    @Autowired
    private GrupoRepository grupoRepository;
    @Autowired
    private CadastroPermissaoService cadastroPermissao;

    private static final String MSG_GRUPO_EM_USO
            = "Grupo de código %d não pode ser removido, pois está em uso";

    @Transactional
    public Grupo salvar(Grupo grupo){
        return grupoRepository.save(grupo);
    }

    public List<Grupo>listar(){
        return grupoRepository.findAll();
    }

    public Grupo buscar(Long id){
        return buscarOuFalhar(id);
    }

    @Transactional
    public void deletar(Long id){
        try {
            grupoRepository.deleteById(id);
            grupoRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new GrupoNaoEncontradoException(id);

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_GRUPO_EM_USO, id));
        }
    }

    @Transactional
    public Grupo atualizar(Long id, Grupo grupo){
        var grupoAtual = buscarOuFalhar(id);
        BeanUtils.copyProperties(grupo,grupoAtual,"id");
        return grupoRepository.save(grupoAtual);
    }
    public Grupo buscarOuFalhar(Long id) {
        return grupoRepository.findById(id).orElseThrow(
                ()-> new GrupoNaoEncontradoException("Grupo não encontrada para o id: "+id));
    }

    @Transactional
    public void desassociarPermissao(Long grupoId, Long permissaoId) {
        Grupo grupo = buscarOuFalhar(grupoId);
        Permissao permissao = cadastroPermissao.buscarOuFalhar(permissaoId);

        grupo.removerPermissao(permissao);
    }

    @Transactional
    public void associarPermissao(Long grupoId, Long permissaoId) {
        Grupo grupo = buscarOuFalhar(grupoId);
        Permissao permissao = cadastroPermissao.buscarOuFalhar(permissaoId);

        grupo.adicionarPermissao(permissao);
    }
}
