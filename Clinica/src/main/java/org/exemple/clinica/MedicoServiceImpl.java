package org.exemple.clinica;

import jakarta.jws.WebMethod;
import org.exemple.clinica.dto.MedicoDTO;
import org.exemple.clinica.interfaces.MedicoService;
import jakarta.jws.WebService;


import java.util.List;

@WebService
public class MedicoServiceImpl implements MedicoService {

    private MedicoService medicoService;

    @Override
    @WebMethod
    public void cadastrarMedico(MedicoDTO medicoDTO) {
        medicoService.cadastrarMedico(medicoDTO);
    }

    @Override
    @WebMethod
    public List<MedicoDTO> listarMedicos() {
        return medicoService.listarMedicos();
    }

    @Override
    @WebMethod
    public void atualizarMedico(Long id, MedicoDTO medicoDTO) {
        medicoService.atualizarMedico(id, medicoDTO);
    }

    @Override
    @WebMethod
    public void desativarMedico(Long id) {
        medicoService.desativarMedico(id);
    }

}
