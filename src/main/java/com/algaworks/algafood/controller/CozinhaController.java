package com.algaworks.algafood.controller;

import com.algaworks.algafood.assembler.CozinhaInputDisassembler;
import com.algaworks.algafood.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.dto.CozinhaModel;
import com.algaworks.algafood.domain.model.dto.input.CozinhaInput;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.sevice.CadastroCozinhaService;
import com.algaworks.algafood.exceptions.EntidadeEmUsoException;
import com.algaworks.algafood.exceptions.EntidadeNaoEncontradaException;
import com.algaworks.algafood.exceptions.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    @Autowired
    private CozinhaModelAssembler cozinhaModelAssembler;

    @Autowired
    private CozinhaInputDisassembler cozinhaInputDisassembler;

    @GetMapping
    public ResponseEntity<Page<CozinhaModel>>listar(Pageable pageable){
        Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);

        List<CozinhaModel> cozinhasModel = cozinhaModelAssembler
                .toCollectionModel(cozinhasPage.getContent());

        Page<CozinhaModel> cozinhasModelPage = new PageImpl<>(cozinhasModel, pageable,
                cozinhasPage.getTotalElements());

        return ResponseEntity.ok().body(cozinhasModelPage);
    }

    @GetMapping("/{cozinhaId}")
    public ResponseEntity<CozinhaModel>buscar(@PathVariable Long cozinhaId){
        return ResponseEntity.ok().body(cozinhaModelAssembler
                .toModel(cadastroCozinhaService.buscarOuFalhar(cozinhaId)));
    }

    @PostMapping
    public ResponseEntity<CozinhaModel> adicionar(@RequestBody @Valid CozinhaInput cozinhaInput){
        var cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(cozinhaModelAssembler
                .toModel(cadastroCozinhaService.salvar(cozinha)));
    }

    @PutMapping("/{cozinhaId}")
    public ResponseEntity<CozinhaModel>atualizar(@PathVariable Long cozinhaId, @RequestBody @Valid  CozinhaInput cozinhaInput){
        var cozinhaAtual = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
        var cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
        BeanUtils.copyProperties(cozinha,cozinhaAtual,"id");
        return ResponseEntity.ok().body(cozinhaModelAssembler
                .toModel(cadastroCozinhaService.salvar(cozinhaAtual)));
    }

    @DeleteMapping("/{cozinhaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cozinhaId){
      cadastroCozinhaService.remover(cozinhaId);
    }
}
