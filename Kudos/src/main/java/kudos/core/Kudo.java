package kudos.core;

import java.util.Date;
import java.util.UUID;



public class Kudo {

	private UUID id;
	private int fuente;
	private int destino;
	private String tema;
	private String fecha;
	private String lugar;
	private String texto;
	
	public Kudo() {}
	
	
	
	public Kudo(UUID id, int fuente, int destino, String tema, String fecha, String lugar, String texto) {
		this.id = id;
		this.fuente = fuente;
		this.destino = destino;
		this.tema = tema;
		this.fecha = fecha;
		this.lugar = lugar;
		this.texto = texto;
	}



	public int getFuente() {
		return fuente;
	}
	public void setFuente(int fuente) {
		this.fuente = fuente;
	}
	public int getDestino() {
		return destino;
	}
	public void setDestino(int destino) {
		this.destino = destino;
	}
	public String getTema() {
		return tema;
	}
	public void setTema(String tema) {
		this.tema = tema;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	
	
}
