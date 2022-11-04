package sistema.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import sistema.model.TipoDeContato;

@Controller
public class TipoController {
    @Autowired
    JdbcTemplate db;

    @GetMapping("tipos")
    public String listagemDosTiposDeContatos(Model model) {
        List<TipoDeContato> listaDeTiposDeContatos = db.query(
                "select * from tipo",
                (res, rowNum) -> {
                    TipoDeContato tipo = new TipoDeContato(
                            res.getInt("id"),
                            res.getString("descricao"));
                    return tipo;
                });
        model.addAttribute("tipos", listaDeTiposDeContatos);
        return "tipo";
    }

    @GetMapping("novo_tipo")
    public String exibeFormTipo(Model model) {
        model.addAttribute("tipo", new TipoDeContato());
        return "formulario_tipo";
    }

    @PostMapping("novo_tipo")
    public String gravaDadosTipo(TipoDeContato tipo) {
        db.update("insert into tipo(descricao) values (?)",
                tipo.getDescricao());
        return "home";
    }
}
