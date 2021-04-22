package com.evento.eventoapp.repository;

import com.evento.eventoapp.models.Convidado;
import com.evento.eventoapp.models.Evento;
import org.springframework.data.repository.CrudRepository;

public interface ConvidadoRepository extends CrudRepository<Convidado,String> {
    Iterable<Convidado>  findByEvento(Evento evento);
    Convidado  findByRg(String rg);
}
