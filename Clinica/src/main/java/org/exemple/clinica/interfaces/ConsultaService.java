package org.exemple.clinica.interfaces;

import org.exemple.clinica.domain.Consulta;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import org.exemple.clinica.dto.ConsultaDTO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebService
public interface ConsultaService {
    @WebMethod
    void agendarConsulta(ConsultaDTO consultaDTO);

    @WebMethod
    void cancelarConsulta(Long id, String motivo);
}
