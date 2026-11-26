package br.com.votacao.model;

public class Ideia {
    private int id;
    private String titulo;
    private String descricao;
    private Integer usuarioId;

    public Ideia() {
    }

    public Ideia(int id, String titulo, String descricao, Integer usuarioId) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.usuarioId = usuarioId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
}
