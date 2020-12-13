package com.algaworks.algafood.controller;

import com.algaworks.algafood.assembler.UsuarioInputDisassembler;
import com.algaworks.algafood.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.domain.model.dto.UsuarioModel;
import com.algaworks.algafood.domain.model.dto.input.SenhaInput;
import com.algaworks.algafood.domain.model.dto.input.UsuarioComSenhaInput;
import com.algaworks.algafood.domain.model.dto.input.UsuarioInput;
import com.algaworks.algafood.domain.sevice.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private CadastroUsuarioService usuarioService;
    @Autowired
    private UsuarioModelAssembler modelAssembler;
    @Autowired
    private UsuarioInputDisassembler inputDisassembler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioModel adicionar(@RequestBody @Valid UsuarioComSenhaInput comSenhaInput){
        var usuario = inputDisassembler.toDomainObject(comSenhaInput);
        var usuarioDto = usuarioService.salvar(usuario);
        return modelAssembler.toModel(usuarioDto);
    }

    @PutMapping(value = "/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioModel atualizar(@Valid @PathVariable Long usuarioId, @RequestBody UsuarioInput usuarioInput){
       var usuarioAtual = usuarioService.buscarOuFalhar(usuarioId);
        inputDisassembler.copyToDomainObject(usuarioInput,usuarioAtual);
       return modelAssembler.toModel(usuarioService.salvar(usuarioAtual));
    }

    @PutMapping("/{usuarioId}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@PathVariable Long usuarioId, @RequestBody @Valid SenhaInput senha) {
        usuarioService.alterarSenha(usuarioId, senha.getSenhaAtual(), senha.getNovaSenha());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    private List<UsuarioModel>listar(){
       return modelAssembler.toCollectionModel(usuarioService.listar());
    }

    @GetMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    private UsuarioModel buscar(@PathVariable Long usuarioId){
        return modelAssembler.toModel(usuarioService.buscar(usuarioId));
    }

    @DeleteMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long usuarioId){
        usuarioService.deletar(usuarioId);
    }
}

