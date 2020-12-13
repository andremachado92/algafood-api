package com.algaworks.algafood.controller;

import com.algaworks.algafood.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.domain.model.dto.RestauranteModel;
import com.algaworks.algafood.domain.model.dto.input.RestauranteInput;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.sevice.CadastroRestauranteService;
import com.algaworks.algafood.domain.view.RestauranteView;
import com.algaworks.algafood.exceptions.*;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private RestauranteModelAssembler restauranteModelAssembler;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private RestauranteInputDisassembler restauranteInputDisassembler;

    @JsonView(RestauranteView.Resumo.class)
    @GetMapping
    public ResponseEntity<List<RestauranteModel>> listar(){
        return ResponseEntity.ok().body(restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll()));
    }

    @JsonView(RestauranteView.ApenasNome.class)
    @GetMapping(params = "projecao=apenas-nome")
    public List<RestauranteModel> listarApenasNomes() {
        return restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll());
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<RestauranteModel>buscar(@PathVariable Long restauranteId){
        return ResponseEntity.ok().body(restauranteModelAssembler.toModel(cadastroRestauranteService.buscarOuFalhar(restauranteId)));
    }

    @PostMapping("/adicionar")
    public ResponseEntity<?>adicionar(@RequestBody @Valid RestauranteInput restauranteInput){
        try{
            var restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
            return ResponseEntity.ok().body(restauranteModelAssembler.toModel(
                    cadastroRestauranteService.salvar(restaurante)));
        }catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }

    }

    @PutMapping("/atualizar/{restauranteId}")
    public ResponseEntity<?>atualizar(@RequestBody @Valid  RestauranteInput restauranteInput, @PathVariable Long restauranteId) {
        try {

            return ResponseEntity.ok().body(restauranteModelAssembler.toModel(
                    cadastroRestauranteService.atualizar(restauranteId, restauranteInput)));
        } catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @DeleteMapping("/{restauranteId}")
    public ResponseEntity<?> deletar(@PathVariable Long restauranteId){
    cadastroRestauranteService.deletar(restauranteId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PutMapping("/{restauranteId}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativo(@PathVariable Long restauranteId){
        cadastroRestauranteService.ativar(restauranteId);
    }

    @DeleteMapping("/{restauranteId}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativo(@PathVariable Long restauranteId){
        cadastroRestauranteService.inativar(restauranteId);
    }

    @PutMapping("/{restauranteId}/abertura")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void abrir(@PathVariable Long restauranteId) {
        cadastroRestauranteService.abrir(restauranteId);
    }

    @PutMapping("/{restauranteId}/fechamento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void fechar(@PathVariable Long restauranteId) {
        cadastroRestauranteService.fechar(restauranteId);
    }

    @PutMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarMultiplos(@RequestBody List<Long> restauranteIds) {
        try {
            cadastroRestauranteService.ativar(restauranteIds);
        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @DeleteMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativarMultiplos(@RequestBody List<Long> restauranteIds) {
        try {
            cadastroRestauranteService.inativar(restauranteIds);
        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage());
        }
    }

}
