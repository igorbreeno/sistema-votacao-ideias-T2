package br.com.votacao.service;
import br.com.votacao.client.ApiClient;
import br.com.votacao.model.*;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
public class VotoService {
 private final ApiClient api = ApiClient.getInstance();
 public int contarVotos(int id) throws Exception { return api.json.readTree(api.chamar("GET","/votos/contagem/"+id,null)).get("totalVotos").asInt(); }
 public void votar(int usuarioId,int ideiaId) throws Exception { api.chamar("POST","/votos",Map.of("usuarioId",usuarioId,"ideiaId",ideiaId)); }
}
