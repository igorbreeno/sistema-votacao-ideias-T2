package br.com.votacao.service;
import br.com.votacao.client.ApiClient;
import br.com.votacao.model.*;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
public class IdeiaService {
 private final ApiClient api = ApiClient.getInstance();
 public List<Ideia> listar() throws Exception { return api.json.readValue(api.chamar("GET","/ideias",null), new TypeReference<List<Ideia>>(){}); }
 public Ideia buscarPorId(int id) throws Exception { return api.json.readValue(api.chamar("GET","/ideias/"+id,null),Ideia.class); }
 public void salvar(Ideia i) throws Exception { api.chamar(i.getId()>0?"PUT":"POST", "/ideias"+(i.getId()>0?"/"+i.getId():""),i); }
 public void excluir(int id) throws Exception { api.chamar("DELETE","/ideias/"+id,null); }
}
