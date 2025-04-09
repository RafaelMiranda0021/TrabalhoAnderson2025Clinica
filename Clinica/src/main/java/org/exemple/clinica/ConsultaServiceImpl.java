package org.exemple.clinica;

import jakarta.jws.WebMethod;
import org.exemple.clinica.dto.ConsultaDTO;
import org.exemple.clinica.interfaces.ConsultaService;
import jakarta.jws.WebService;


@WebService
public class ConsultaServiceImpl implements ConsultaService {
    private ConsultaService consultaService;

    @Override
    @WebMethod
    public void agendarConsulta(ConsultaDTO consultaDTO) {
        consultaService.agendarConsulta(consultaDTO);
    }

    @Override
    @WebMethod
    public void cancelarConsulta(Long id, String motivo) {
        consultaService.cancelarConsulta(id, motivo);
    }
}
