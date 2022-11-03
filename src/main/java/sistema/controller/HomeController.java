package sistema.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import sistema.model.Contato;
import sistema.model.Tipo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    JdbcTemplate db;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/contatos")
    public String contatos(Model model) {
        List<Contato> listaDeContatos = db.query(
                "select	c.id as id, c.nome as nome, c.telefone as telefone, c.endereco as endereco, t.descricao as tipo, t.id as tipo_id from contatos c, tipo t where c.tipo_id=t.id;",
                (res, rowNum) -> {
                    Contato contato = new Contato(
                            res.getInt("id"),
                            res.getString("nome"),
                            res.getString("telefone"),
                            res.getString("endereco"),
                            new Tipo(res.getInt("tipo_id"), res.getString("tipo")));
                    return contato;
                });
        model.addAttribute("contatos", listaDeContatos);
        return "contato";
    }

    @GetMapping("novo")
    public String exibeForm(Model model) {
        model.addAttribute("contato", new Contato());
        // trecho de código para alimentar o <select> do form
        List<Tipo> tipos = db.query(
                "select * from tipo",
                (res, rowNum) -> {
                    Tipo tipo = new Tipo(
                            res.getInt("id"),
                            res.getString("descricao"));
                    return tipo;
                });
        model.addAttribute("tipos", tipos);
        // fim do trecho
        return "formulario";
    }

    @PostMapping("novo")
    public String gravaDados(Contato contato) {
        db.update("insert into contatos(endereco, telefone, nome, tipo_id) values (?, ?, ?, ?)",
                contato.getEndereco(), contato.getTelefone(), contato.getNome(), contato.getTipo().getId());
        return "home";
    }

    @GetMapping("excluir-contato")
    public String apagarContato(@RequestParam(value = "id", required = true) Integer cod) {
        System.out.println("--------------------> " + cod);
        db.update("delete from contatos where id = ?", cod);
        return "redirect:/contatos";
    }

    @GetMapping("editar-contato")
    public String mostraFormAlteraContato(@RequestParam(value = "id", required = true) Integer cod, Model model) {
        Contato contato = db.queryForObject(
                "select	c.id as id, c.nome as nome, c.telefone as telefone, c.endereco as endereco, t.descricao as tipo, t.id as tipo_id from contatos c, tipo t where c.tipo_id=t.id and c.id = ?;",
                (rs, rowNum) -> {
                    Contato c = new Contato();
                    Tipo t = new Tipo(rs.getInt("tipo_id"), rs.getString("tipo"));
                    c.setTipo(t);
                    c.setId(rs.getInt("id"));
                    c.setEndereco(rs.getString("endereco"));
                    c.setNome(rs.getString("nome"));
                    c.setTelefone(rs.getString("telefone"));
                    return c;
                },
                cod);
        model.addAttribute("contat", contato);
        // trecho de código para alimentar o <select> do form
        List<Tipo> tipos = db.query(
                "select * from tipo",
                (res, rowNum) -> {
                    Tipo tipo = new Tipo(
                            res.getInt("id"),
                            res.getString("descricao"));
                    return tipo;
                });
        model.addAttribute("tipos", tipos);
        // fim do trecho
        return "formeditacontato";
    }

    @PostMapping("gravacontatomodificado")
    public String gravaContatoModificado(Contato contato) {
        db.update(
                "update contatos set nome=?, telefone=?, endereco=?, tipo_id=? where id = ?",
                contato.getNome(),
                contato.getTelefone(),
                contato.getEndereco(),
                contato.getTipo().getId(),
                contato.getId());
        return "redirect:/contatos";
    }

    @GetMapping("novo_tipo")
    public String exibeFormTipo(Model model) {
        model.addAttribute("tipo", new Tipo());
        return "formulario_tipo";
    }

    @PostMapping("novo_tipo")
    public String novoTipo(Tipo tipo) {
        db.update("insert into tipo(descricao) values (?)",
                tipo.getDescricao());
        return "home";
    }

    @GetMapping("/tipos")
    public String tipos(Model model) {
        List<Tipo> listaDeTipos = db.query(
                "select * from tipo",
                (res, rowNum) -> {
                    Tipo tipo = new Tipo(
                            res.getInt("id"),
                            res.getString("descricao"));
                    return tipo;
                });
        model.addAttribute("tipos", listaDeTipos);
        return "tipo";
    }

}
