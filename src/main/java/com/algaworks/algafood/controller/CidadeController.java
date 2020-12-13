package com.algaworks.algafood.controller;

import com.algaworks.algafood.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.assembler.CidadeModelAssembler;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.dto.CidadeModel;
import com.algaworks.algafood.domain.model.dto.input.CidadeInput;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.sevice.CadastroCidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("cidades")
public class CidadeController {
    @Autowired
    private  CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @Autowired
    private CidadeModelAssembler cidadeModelAssembler;

    @Autowired
    private CidadeInputDisassembler cidadeInputDisassembler;

    @GetMapping("/listar")
    public ResponseEntity<List<CidadeModel>> listar(){
        return ResponseEntity.ok().body(cidadeModelAssembler.toCollectionModel(cidadeRepository.findAll()));
    }

    @GetMapping("buscar/{cidadeId}")
    public ResponseEntity<?>buscar(@PathVariable Long cidadeId){
       var cidade= cadastroCidadeService.buscarOuFalhar(cidadeId);
       return ResponseEntity.ok().body(cidadeModelAssembler.toModel(cidade));
    }

    @DeleteMapping("/{cidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long cidadeId){
        cadastroCidadeService.deletar(cidadeId);
    }

    @PutMapping("/atualizar/{cidadeId}")
    public ResponseEntity<?> atualizar(@PathVariable Long cidadeId, @RequestBody @Valid  CidadeInput cidadeInput) {
        var cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(cidadeModelAssembler.toModel(
                cadastroCidadeService.atualizar(cidadeId, cidade)));
    }


    @PostMapping("/adicionar")
    public ResponseEntity<?> adicionar(@RequestBody @Valid CidadeInput cidadeInput){
        var cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
        return ResponseEntity.ok().body(cidadeModelAssembler.toModel(
                cadastroCidadeService.adicionar(cidade)));
    }
}
