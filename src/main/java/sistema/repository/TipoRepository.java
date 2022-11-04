package sistema.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sistema.model.TipoDeContato;

@Repository
public class TipoRepository {

    @Autowired
    JdbcTemplate db;

    public List<TipoDeContato> todos() {
        List<TipoDeContato> listaDeTiposDeContatos = db.query(
                "select * from tipo",
                (res, rowNum) -> {
                    TipoDeContato tipo = new TipoDeContato(
                            res.getInt("id"),
                            res.getString("descricao"));
                    return tipo;
                });
        return listaDeTiposDeContatos;
    }
}
