package com.algaworks.algafood.domain.sevice;

import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import com.algaworks.algafood.exceptions.NegocioException;
import com.algaworks.algafood.exceptions.UsuarioNaoEncontradoException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CadastroUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EntityManager manager;
    @Autowired
    private CadastroGrupoService cadastroGrupo;

    public List<Usuario> listar(){
        return usuarioRepository.findAll();
    }

    public Usuario buscar(Long id){
        return buscarOuFalhar(id);
    }

    @Transactional
    public void deletar(Long id){
        var usuario = buscarOuFalhar(id);
        try {
            usuarioRepository.delete(usuario);
            usuarioRepository.flush();
        }catch (UsuarioNaoEncontradoException e){
            throw new NegocioException(e.getMessage());
        }

    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        manager.detach(usuario);//tira usuario do contesto de gerenciamento do jpa

        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());

        if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
            throw new NegocioException(
                    String.format("Já existe um usuário cadastrado com o e-mail %s", usuario.getEmail()));
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void alterarSenha(Long userId, String senhaAtual, String novaSenha){
        var usuario = buscarOuFalhar(userId);
        if (usuario.senhaNaoCoincideCom(senhaAtual)) {
            throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
        }

        usuario.setSenha(novaSenha);
    }

    @Transactional
    public void desassociarGrupo(Long usuarioId, Long grupoId) {
        Usuario usuario = buscarOuFalhar(usuarioId);
        Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);

        usuario.removerGrupo(grupo);
    }

    @Transactional
    public void associarGrupo(Long usuarioId, Long grupoId) {
        Usuario usuario = buscarOuFalhar(usuarioId);
        Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);

        usuario.adicionarGrupo(grupo);
    }

    public Usuario buscarOuFalhar(Long id){
        return usuarioRepository.findById(id).orElseThrow(
                ()->new UsuarioNaoEncontradoException("Usuario não encontrado para o id: "+id));
    }
}
