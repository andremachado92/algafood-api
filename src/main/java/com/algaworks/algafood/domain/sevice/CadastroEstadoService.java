package com.algaworks.algafood.domain.sevice;

import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.exceptions.EntidadeEmUsoException;
import com.algaworks.algafood.exceptions.EntidadeNaoEncontradaException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroEstadoService {

    @Autowired
    EstadoRepository estadoRepository;

    public Estado buscarOuFalhar(Long id){
        return estadoRepository.findById(id).orElseThrow(
                ()-> new EntidadeNaoEncontradaException("Estado não encontrado para o id: "+id));
    }

    @Transactional
    public void deletar(Long id){
        var estado = buscarOuFalhar(id);
        try {
            estadoRepository.delete(estado);
            estadoRepository.flush();
        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException("Estado não pode ser excluído pois tem cidades associadas");
        }

    }

    @Transactional
    public Estado atualizar(Long id, Estado estado){
     var estadoAtual = buscarOuFalhar(id);

        BeanUtils.copyProperties(estado,estadoAtual,"id");
        return estadoRepository.save(estadoAtual);
    }


    @Transactional
    public Estado adicionar(Estado estado){
        return estadoRepository.save(estado);
    }

}
