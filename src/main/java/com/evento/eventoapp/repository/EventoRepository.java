package com.evento.eventoapp.repository;

import com.evento.eventoapp.models.Evento;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventoRepository extends JpaRepository<Evento, Long> {
    Evento  findByCodigo(long codigo);
}
