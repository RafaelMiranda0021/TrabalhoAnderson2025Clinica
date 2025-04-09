package org.exemple.clinica.interfaces;


import org.exemple.clinica.domain.Medico;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import org.exemple.clinica.dto.MedicoDTO;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

@WebService
public interface MedicoService {
    @WebMethod
    void cadastrarMedico(MedicoDTO medicoDTO);

    @WebMethod
    List<MedicoDTO> listarMedicos();

    @WebMethod
    void atualizarMedico(Long id, MedicoDTO medicoDTO);

    @WebMethod
    void desativarMedico(Long id);
}