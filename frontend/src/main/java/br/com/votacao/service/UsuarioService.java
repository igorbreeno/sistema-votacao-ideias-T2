package br.com.votacao.service;
import br.com.votacao.client.ApiClient;
import br.com.votacao.model.*;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
public class UsuarioService {
 private final ApiClient api = ApiClient.getInstance();
 public List<Usuario> listar() throws Exception { return api.json.readValue(api.chamar("GET","/usuarios",null),new TypeReference<List<Usuario>>(){}); }
 public void cadastrar(Usuario u) throws Exception { api.chamar("POST","/usuarios",Map.of("nome",u.getNome(),"email",u.getEmail(),"senha",u.getSenha())); }
}
