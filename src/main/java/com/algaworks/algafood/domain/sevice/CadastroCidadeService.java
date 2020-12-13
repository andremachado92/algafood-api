package com.algaworks.algafood.domain.sevice;

import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.exceptions.CidadeNaoEncontradaException;
import com.algaworks.algafood.exceptions.EntidadeEmUsoException;
import com.algaworks.algafood.exceptions.EntidadeNaoEncontradaException;
import com.algaworks.algafood.exceptions.NegocioException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroCidadeService {

    @Autowired
    CidadeRepository cidadeRepository;

    @Autowired
    EstadoRepository estadoRepository;

    public Cidade buscarOuFalhar(Long id){
        return cidadeRepository.findById(id).orElseThrow(
                ()-> new CidadeNaoEncontradaException("Cidade não encontrada para o id: "+id));
    }

    @Transactional
    public void deletar(Long id){
        var cidade = buscarOuFalhar(id);
        try {
            cidadeRepository.delete(cidade);
            cidadeRepository.flush();
        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException("Cidade não pode ser excluída pois tem clientes associadas");
        }

    }

    @Transactional
    public Cidade atualizar(Long id, Cidade cidade){
        var estadoAtual = buscarOuFalhar(id);
        var estado = buscarEstado(cidade.getEstado().getId());
        cidade.setEstado(estado);

        

        BeanUtils.copyProperties(cidade,estadoAtual,"id","estado");
        return cidadeRepository.save(estadoAtual);
    }


    @Transactional
    public Cidade adicionar(Cidade cidade){
        var estado =buscarEstado(cidade.getEstado().getId());
        cidade.setEstado(estado);
        return cidadeRepository.save(cidade);
    }

    public Estado buscarEstado(Long id){
        return estadoRepository.findById(id).orElseThrow(()->
                new NegocioException("Estado nao encontrado para id "+id));
    }
}
