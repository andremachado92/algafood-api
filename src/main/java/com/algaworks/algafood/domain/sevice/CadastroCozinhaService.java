package com.algaworks.algafood.domain.sevice;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.exceptions.CozinhaNaoEncontradaException;
import com.algaworks.algafood.exceptions.EntidadeEmUsoException;
import com.algaworks.algafood.exceptions.EntidadeNaoEncontradaException;
import com.algaworks.algafood.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroCozinhaService {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Transactional
    public Cozinha salvar(Cozinha cozinha){
        return cozinhaRepository.save(cozinha);
    }

    @Transactional
    public void remover(Long cozinhaId){
        var cozinhaDto = buscarOuFalhar(cozinhaId);
        try {
            cozinhaRepository.deleteById(cozinhaDto.getId());
            cozinhaRepository.flush();
        }catch (DataIntegrityViolationException ex){
            throw new EntidadeEmUsoException("Não foi possivel exxcluir esta cozinha pois ela está em uso ");
        }

    }

    public Cozinha buscarOuFalhar(Long cozinhaId){
        return cozinhaRepository.findById(cozinhaId)
                .orElseThrow(()-> new CozinhaNaoEncontradaException("Cozinha não encontrada para o id"+cozinhaId ));
    }
}
