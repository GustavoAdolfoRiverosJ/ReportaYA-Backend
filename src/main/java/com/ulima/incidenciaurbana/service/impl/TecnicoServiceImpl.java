package com.ulima.incidenciaurbana.service.impl;

import com.ulima.incidenciaurbana.dto.TecnicoDTO;
import com.ulima.incidenciaurbana.model.Tecnico;
import com.ulima.incidenciaurbana.repository.TecnicoRepository;
import com.ulima.incidenciaurbana.service.ITecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TecnicoServiceImpl implements ITecnicoService {

    private final TecnicoRepository tecnicoRepository;

    @Autowired
    public TecnicoServiceImpl(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TecnicoDTO> obtenerTodosTecnicos(int page) {
        return tecnicoRepository.findAll(PageRequest.of(page, 10))
                .map(this::convertirADTO);
    }

    private TecnicoDTO convertirADTO(Tecnico tecnico) {
        var persona = tecnico.getPersona();
        return new TecnicoDTO(
                tecnico.getId(),
                tecnico.getUsuario(),
                persona != null ? persona.getNombres() : null,
                persona != null ? persona.getApellidos() : null,
                persona != null ? persona.getDni() : null,
                persona != null ? persona.getTelefono() : null,
                persona != null ? persona.getCorreo() : null,
                tecnico.isActivo()
        );
    }
}
