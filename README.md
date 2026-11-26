# Sistema de Votação de Ideias — T2

## Arquitetura
Dois WARs independentes no Tomcat 10.1+, Java 17 e Maven 3.9+. Frontend MVC (JSP, Controller, Service, cliente HTTP Singleton) → HTTP/JSON com JWT → Backend REST (Controller, Service, DAO, Model) → MySQL. O frontend não contém JDBC nem acessa o banco. O Singleton reutiliza o HttpClient e o token; o HttpClient Java substitui cURL dentro da aplicação.

## Instalação no Windows
1. Execute `backend/database/banco.sql` no MySQL. Preserve os dados existentes; não é necessário apagar o banco.
2. Na raiz do projeto, execute `mvn clean package`.
3. Pare o Tomcat. Substitua o WAR antigo e remova a pasta expandida antiga `sistema-votacao-ideias` do webapps (não o banco). Copie `backend/target/sistema-votacao-ideias.war` e `frontend/target/sistema-votacao-frontend.war` para webapps.
4. No PowerShell, configure as variáveis antes de iniciar o Tomcat pelo mesmo terminal (se usa serviço Windows ou IDE, configure no ambiente desse processo):

```powershell
$env:DB_USER = 'root'
$env:DB_PASSWORD = 'SUA_SENHA_MYSQL'
$env:DB_URL = 'jdbc:mysql://localhost:3306/sistema_votacao_ideias?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo'
$env:JWT_SECRET = ([guid]::NewGuid().ToString('N') + [guid]::NewGuid().ToString('N'))
$env:API_CLIENT_KEY = ([guid]::NewGuid().ToString('N') + [guid]::NewGuid().ToString('N'))
$env:API_BASE_URL = 'http://localhost:8080/sistema-votacao-ideias/api'
& 'C:\CAMINHO\apache-tomcat\bin\catalina.bat' run
```

Substitua o caminho do Tomcat. Para testar em outro terminal, use a mesma API_CLIENT_KEY configurada no servidor. Abra http://localhost:8080/sistema-votacao-frontend/ .

## Teste CRUD no PowerShell
```powershell
$base = 'http://localhost:8080/sistema-votacao-ideias/api'
$chave = Read-Host 'Informe a API_CLIENT_KEY do Tomcat'
$auth = Invoke-RestMethod -Method Post -Uri "$base/auth/token" -ContentType 'application/json' -Body (@{chave=$chave} | ConvertTo-Json)
$h = @{Authorization="Bearer $($auth.token)"}
$titulo = 'Ideia teste ' + [guid]::NewGuid().ToString('N')
Invoke-RestMethod -Method Post -Uri "$base/ideias" -Headers $h -ContentType 'application/json' -Body (@{titulo=$titulo;descricao='Teste CRUD'} | ConvertTo-Json)
$lista = Invoke-RestMethod -Uri "$base/ideias" -Headers $h
$id = ($lista | Where-Object titulo -eq $titulo).id
Invoke-RestMethod -Uri "$base/ideias/$id" -Headers $h
Invoke-RestMethod -Method Put -Uri "$base/ideias/$id" -Headers $h -ContentType 'application/json' -Body (@{titulo=$titulo;descricao='Atualizada'} | ConvertTo-Json)
Invoke-RestMethod -Method Delete -Uri "$base/ideias/$id" -Headers $h
Invoke-RestMethod -Uri "$base/ideias" -Headers $h
# Sem token: deve retornar 401
curl.exe -i "$base/ideias"
```

## Endpoints (prefixo /api)
| Método | Rota | Corpo JSON | Sucesso |
|---|---|---|---|
| POST | /auth/token | chave | 200 |
| GET | /ideias | — | 200 |
| GET | /ideias/{id} | — | 200 |
| POST | /ideias | titulo, descricao, usuarioId opcional | 201 |
| PUT | /ideias/{id} | titulo, descricao, usuarioId opcional | 200 |
| DELETE | /ideias/{id} | — | 200 |
| GET | /usuarios ou /usuarios/{id} | — | 200 |
| POST | /usuarios | nome, email, senha | 201 |
| PUT | /usuarios/{id} | nome, email, senha opcional | 200 |
| DELETE | /usuarios/{id} | — | 200 |
| POST | /votos | usuarioId, ideiaId | 201 |
| GET | /votos/contagem/{ideiaId} | — | 200 |

Todas as rotas exceto emissão de token exigem Authorization: Bearer JWT. Token HS256 com validade de 15 minutos. Postman: importe a coleção em backend/postman, configure clientKey e execute Obter JWT primeiro. Use IDs existentes. Erros previstos: 400, 401, 404, 409, 500.

## Escopo e segurança
JWT autentica a aplicação cliente, não cada usuário final; seleção de usuário para votar foi preservada do projeto original. Não é um sistema de autorização individual. Novas senhas e senhas alteradas são armazenadas com PBKDF2; registros antigos não são migrados automaticamente. Use somente ambiente local para demonstração; produção exige HTTPS, autenticação individual, autorização e proteção CSRF. Não publique segredos no GitHub. O código foi revisado estaticamente; execute a compilação e os testes locais antes da entrega.
