package kudos.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.google.common.base.Optional;

import kudos.core.Kudo;

public class KudoDao {
	
	private final Session session;
	 
    private final PreparedStatement createKudo;
    private final PreparedStatement deleteKudo;
    private final PreparedStatement getAllKudos;
	
	public KudoDao(Session session) {
		this.session = session;
		this.createKudo = session.prepare("insert into kudos "
				+ "(id, fuente, destino, tema, fecha,lugar,texto) values "
				+ "(?,?, ?, ?,?,?,?)");
		this.deleteKudo = session.prepare("Delete From kudos "
				+ "Where id=?");
		this.getAllKudos = session.prepare("Select * from kudos");
	}
	
	public void createKudo(Kudo kudo) {
		session.execute(this.createKudo.bind(
					kudo.getId(),
					kudo.getFuente(),
					kudo.getDestino(),
					kudo.getTema(),
					kudo.getFecha().toString(),
					kudo.getLugar(),
					kudo.getTexto()
				));
	}
	
	public void deleteKudo(UUID id) {
		session.execute(this.deleteKudo.bind(id));
	}
	
	public List<Kudo> getAllKudos() {
		ResultSet rs = session.execute(this.getAllKudos.bind());
		List<Row> rows =  rs.all();
		List<Kudo> resp = new ArrayList<Kudo>();
		for (Row row : rows) {
			UUID id = row.getUUID("id");
			int fuente = row.getInt("fuente");
			int destino = row.getInt("destino");
			String tema = row.getString("tema");
			String fecha = row.getString("fecha");
			String lugar = row.getString("lugar");
			String texto = row.getString("texto");
			
			Kudo kudo = new Kudo(id, fuente, destino, tema, fecha, lugar, texto);
			resp.add(kudo);
		}
		return resp;
	}
	
	public List<Kudo> getAllKudosPagination(int fetchSize, int page) {
		ResultSet rs = session.execute(this.getAllKudos.bind().setFetchSize(fetchSize));
		
		List<Row> rows = new ArrayList<>();
		Iterator<Row> iterator1 = rs.iterator();
		for(int i= 1; i<page; i++) {
			while (rs.getAvailableWithoutFetching() > 0) {
	        	iterator1.next();
			}
			rs.fetchMoreResults();
	        iterator1.hasNext();
		}
		
		while (rs.getAvailableWithoutFetching() > 0) {
		    rows.add(iterator1.next());
		}
		
		//List<Row> rows =  rs.all();
		List<Kudo> resp = new ArrayList<Kudo>();
		
		
		for (Row row : rows) {
			UUID id = row.getUUID("id");
			int fuente = row.getInt("fuente");
			int destino = row.getInt("destino");
			String tema = row.getString("tema");
			String fecha = row.getString("fecha");
			String lugar = row.getString("lugar");
			String texto = row.getString("texto");
			
			Kudo kudo = new Kudo(id, fuente, destino, tema, fecha, lugar, texto);
			resp.add(kudo);
		}
		return resp;
	}

}
