package com.algaworks.algafood.domain.sevice;

import com.algaworks.algafood.assembler.FormaPagamentoInputDisassembler;
import com.algaworks.algafood.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.dto.FormaPagamentoModel;
import com.algaworks.algafood.domain.model.dto.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.exceptions.EntidadeEmUsoException;
import com.algaworks.algafood.exceptions.EntidadeNaoEncontradaException;
import com.algaworks.algafood.exceptions.FormaPagamentoNaoEncontradaException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class CadastroFormaPagamento {
    private static final String MSG_FORMA_PAGAMENTO_EM_USO
            = "Forma de pagamento de código %d não pode ser removida, pois está em uso";
    @Autowired
    private FormaPagamentoRepository pagamentoRepository;
    @Autowired
    FormaPagamentoInputDisassembler disassembler;
    @Autowired
    FormaPagamentoModelAssembler  assembler;

    @Transactional
    public FormaPagamento salvar(FormaPagamento pagamento){
      return  pagamentoRepository.save(pagamento);
    }

    public FormaPagamento buscar(Long pagamentoId){
        return buscarOufalhar(pagamentoId);
    }

    public List<FormaPagamento>listar(){
        return pagamentoRepository.findAll();
    }

    @Transactional
    public FormaPagamento atualizar(FormaPagamento pagamentoInput, Long pagamentoId){
        var pagamento = buscarOufalhar(pagamentoId);
        BeanUtils.copyProperties(pagamentoInput,pagamento,"id");
        return pagamentoRepository.save(pagamento);
    }

    @Transactional
    public void deletar( Long pagamentoId){
        try {
            pagamentoRepository.deleteById(pagamentoId);
            pagamentoRepository.flush();
        }catch (EmptyResultDataAccessException e) {
            throw new FormaPagamentoNaoEncontradaException(pagamentoId);

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_FORMA_PAGAMENTO_EM_USO, pagamentoId));
        }
    }

    public FormaPagamento buscarOufalhar(Long pagamentoId){
        return pagamentoRepository.findById(pagamentoId).orElseThrow(
                ()->new EntidadeNaoEncontradaException("Forma de pagamento não encontrada para o id: "+pagamentoId));
    }
}
