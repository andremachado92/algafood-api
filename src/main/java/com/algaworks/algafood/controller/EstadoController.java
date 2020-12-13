package com.algaworks.algafood.controller;

import com.algaworks.algafood.assembler.EstadoInputDisassembler;
import com.algaworks.algafood.assembler.EstadoModelAssembler;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.model.dto.EstadoModel;
import com.algaworks.algafood.domain.model.dto.input.EstadoInput;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.sevice.CadastroEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("estados")
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastroEstadoService cadastroEstadoService;

    @Autowired
    private EstadoModelAssembler estadoModelAssembler;

    @Autowired
    private EstadoInputDisassembler estadoInputDisassembler;

    @GetMapping("/listar")
    public ResponseEntity<List<EstadoModel>> listar(){
        return ResponseEntity.ok().body(estadoModelAssembler.toCollectionModel(estadoRepository.findAll()));
    }

    @GetMapping("buscar/{estadoId}")
    public ResponseEntity<?>buscar(@PathVariable Long estadoId){
     var estado= cadastroEstadoService.buscarOuFalhar(estadoId);
     return ResponseEntity.ok().body(estadoModelAssembler.toModel(estado));

    }

    @DeleteMapping("/{estadoId}")
    public ResponseEntity<?> deletar(@PathVariable Long estadoId){
     cadastroEstadoService.deletar(estadoId);
     return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/atualizar/{estadoId}")
    public ResponseEntity<?> atualizar(@PathVariable Long estadoId, @RequestBody @Valid  EstadoInput estadoInput){
        var estado = estadoInputDisassembler.toDomainObject(estadoInput);
        return ResponseEntity.status(HttpStatus.CREATED).body( estadoModelAssembler.toModel(
                cadastroEstadoService.atualizar(estadoId,estado)));
    }

   @PostMapping("/adicionar")
   public ResponseEntity<?> adicionar(@RequestBody @Valid EstadoInput estadoInput){
       var estado = estadoInputDisassembler.toDomainObject(estadoInput);
        return ResponseEntity.ok().body(estadoModelAssembler.toModel( cadastroEstadoService.adicionar(estado)));
   }


}
