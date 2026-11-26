package br.com.votacao.model;

public class Voto {
    private int id;
    private int usuarioId;
    private int ideiaId;

    public Voto() {
    }

    public Voto(int id, int usuarioId, int ideiaId) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.ideiaId = ideiaId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public int getIdeiaId() { return ideiaId; }
    public void setIdeiaId(int ideiaId) { this.ideiaId = ideiaId; }
}
