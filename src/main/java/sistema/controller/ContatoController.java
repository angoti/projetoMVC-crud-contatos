package sistema.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sistema.model.Contato;
import sistema.repository.ContatoRepository;
import sistema.repository.TipoRepository;

@Controller
public class ContatoController {

    @Autowired
    ContatoRepository repository;

    @Autowired
    TipoRepository repositoryTipo;

    @GetMapping("/contatos")
    public String contatos(Model model) {
        List<Contato> listaDeContatos = repository.todos();
        model.addAttribute("contatos", listaDeContatos);
        return "contato";
    }

    @GetMapping("novo")
    public String exibeForm(Model model) {
        model.addAttribute("contato", new Contato());
        model.addAttribute("tipos", repositoryTipo.todos());
        return "formulario";
    }

    @PostMapping("novo")
    public String gravaDados(Contato contato) {
        repository.grava(contato);
        return "home";
    }

    @GetMapping("excluir-contato")
    public String apagarContato(@RequestParam(value = "id", required = true) Integer cod) {
        repository.exlcuir(cod);
        return "redirect:/contatos";
    }

    @GetMapping("editar-contato")
    public String mostraFormAlteraContato(@RequestParam(value = "id", required = true) Integer cod, Model model) {
        Contato contato = repository.buscaPorId(cod);
        model.addAttribute("contat", contato);
        model.addAttribute("tipos", repositoryTipo.todos());
        return "formeditacontato";
    }

    @PostMapping("gravacontatomodificado")
    public String gravaContatoModificado(Contato contato) {
        repository.atualiza(contato);
        return "redirect:/contatos";
    }

}
