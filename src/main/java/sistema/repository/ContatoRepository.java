package sistema.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sistema.model.Contato;
import sistema.model.TipoDeContato;

@Repository
public class ContatoRepository {

    @Autowired
    JdbcTemplate db;

    public List<Contato> todos() {
        List<Contato> listaDeContatos = db.query(
                "select c.id as id, c.nome as nome, c.telefone as telefone, c.endereco as endereco, t.descricao as tipo, t.id as tipo_id from contatos c, tipo t where c.tipo_id=t.id;",
                (res, rowNum) -> {
                    Contato contato = new Contato(
                            res.getInt("id"),
                            res.getString("nome"),
                            res.getString("telefone"),
                            res.getString("endereco"),
                            new TipoDeContato(res.getInt("tipo_id"), res.getString("tipo")));
                    return contato;
                });
        return listaDeContatos;
    }

    public void grava(Contato contato) {
        db.update("insert into contatos(endereco, telefone, nome, tipo_id) values (?, ?, ?,?)",
                contato.getEndereco(), contato.getTelefone(), contato.getNome(), contato.getTipo().getId());

    }

    public void exlcuir(Integer cod) {
        db.update("delete from contatos where id = ?", cod);
    }

    public Contato buscaPorId(Integer cod) {
        return db.queryForObject(
                "select * from contatos where id = ?",
                (rs, rowNum) -> {
                    Contato c = new Contato();
                    c.setId(rs.getInt("id"));
                    c.setEndereco(rs.getString("endereco"));
                    c.setNome(rs.getString("nome"));
                    c.setTelefone(rs.getString("telefone"));
                    return c;
                },
                cod);
    }

    public void atualiza(Contato contato) {
        db.update(
                "update contatos set nome=?, telefone=?, endereco=?, tipo_id=? where id = ?",
                contato.getNome(),
                contato.getTelefone(),
                contato.getEndereco(),
                contato.getTipo().getId(),
                contato.getId());
    }
}
