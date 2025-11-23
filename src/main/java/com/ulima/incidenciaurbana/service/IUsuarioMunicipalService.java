package com.ulima.incidenciaurbana.service;

import com.ulima.incidenciaurbana.dto.CrearUsuarioMunicipalDTO;
import com.ulima.incidenciaurbana.dto.UsuarioMunicipalResponseDTO;
import java.util.List;

public interface IUsuarioMunicipalService {

    UsuarioMunicipalResponseDTO crearUsuarioMunicipal(CrearUsuarioMunicipalDTO request);

    List<UsuarioMunicipalResponseDTO> listarUsuariosMunicipales();
        

}