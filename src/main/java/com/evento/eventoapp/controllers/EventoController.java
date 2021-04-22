package com.evento.eventoapp.controllers;


import com.evento.eventoapp.models.Convidado;
import com.evento.eventoapp.models.Evento;
import com.evento.eventoapp.repository.ConvidadoRepository;
import com.evento.eventoapp.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class EventoController {

    @Autowired
    public EventoRepository eventorepository;

    @Autowired
    public ConvidadoRepository convidadorepository;

    @RequestMapping(value = "/cadastrarEvento", method = RequestMethod.GET)
    public String form(){
        return "evento/formEvento";
    }

    @RequestMapping(value = "/cadastrarEvento", method = RequestMethod.POST)
    public String form(@Valid Evento evento, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/cadastrarEvento";
        }
        eventorepository.save(evento);
        attributes.addFlashAttribute("mensagem", "Evento cadastrado com sucesso!");
        return "redirect:/cadastrarEvento";
    }

    @RequestMapping("/eventos")
    public ModelAndView listaEventos(){
        ModelAndView mv = new ModelAndView("index");
        Iterable<Evento> eventos = eventorepository.findAll();
        mv.addObject("eventos",eventos);
        return mv;
    }

    @RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
    public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo){
        Evento evento = eventorepository.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("evento/detalhesEvento");
        mv.addObject("evento", evento);
        Iterable<Convidado> convidados = convidadorepository.findByEvento(evento);
        mv.addObject("convidados",convidados);
        return mv;
    }


    @RequestMapping(value = "/{codigo}", method = RequestMethod.POST)
    public String detalhesEventoPost(@PathVariable("codigo") long codigo, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/{codigo}";
        }
        Evento evento = eventorepository.findByCodigo(codigo);
        convidado.setEvento(evento);
        convidadorepository.save(convidado);
        attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
        return "redirect:/{codigo}";
    }

    @RequestMapping("/deletarEvento")
    public String deletarEvento(long codigo){
        Evento evento = eventorepository.findByCodigo(codigo);
        eventorepository.delete(evento);
        return "redirect:/eventos";
    }

    @RequestMapping("/deletarConvidado")
    public String deletarConvidado(String rg){
        Convidado convidado = convidadorepository.findByRg(rg);
        convidadorepository.delete(convidado);
        Evento evento = convidado.getEvento();
        long codigoLong = evento.getCodigo();
        String codigo = "" + codigoLong;
        return "redirect:/" + codigo;
    }

    @GetMapping("/atualizarEvento/{codigo}")
    public ModelAndView atualizarEvento(@PathVariable("codigo") Long codigo) {
        ModelAndView mv = new ModelAndView();
        Evento evento = eventorepository.getOne(codigo);
        mv.setViewName("evento/atualizarEvento");
        mv.addObject("evento", evento);
        return mv;
    }

    @PostMapping("/atualizarEvento")
    public ModelAndView atualizarEvento(Evento evento) {
        ModelAndView mv = new ModelAndView();
        eventorepository.save(evento);
        mv.setViewName("redirect:/eventos");
        return mv;
    }
}
