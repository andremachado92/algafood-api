package com.algaworks.algafood.controller;

import com.algaworks.algafood.assembler.FormaPagamentoInputDisassembler;
import com.algaworks.algafood.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.domain.model.dto.FormaPagamentoModel;
import com.algaworks.algafood.domain.model.dto.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.sevice.CadastroFormaPagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/formas-pagamentos")
public class FormaPagamentoController {
    @Autowired
    private CadastroFormaPagamento formaPagamentoService;
    @Autowired
    FormaPagamentoInputDisassembler disassembler;
    @Autowired
    FormaPagamentoModelAssembler assembler;

    @PostMapping("/adicionar")
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoModel salvar(@Valid @RequestBody FormaPagamentoInput pagamentoInput){
        var pagamento = disassembler.toDomainObject(pagamentoInput);
        return assembler.toModel(formaPagamentoService.salvar(pagamento));
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long id){
        FormaPagamentoModel formaPagamentoModel = assembler.toModel(formaPagamentoService.buscar(id));
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10L, TimeUnit.SECONDS))
                .body(formaPagamentoModel);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<FormaPagamentoModel>> listar(){
        List<FormaPagamentoModel> formaPagamentoModels = assembler.toCollectionModel(formaPagamentoService.listar());
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10L, TimeUnit.SECONDS))
                .body(formaPagamentoModels);
    }

    @DeleteMapping("deletar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id){
        formaPagamentoService.deletar(id);
    }

    @PutMapping("/atualizar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FormaPagamentoModel atualizar(@Valid @RequestBody FormaPagamentoInput pagamentoInput, @PathVariable Long id){
        var pagamento = disassembler.toDomainObject(pagamentoInput);
        return assembler.toModel(formaPagamentoService.atualizar(pagamento,id));
    }
}
