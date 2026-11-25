package br.edu.eseg.plantas.repository;

import br.edu.eseg.plantas.model.HistoricoStatusPlanta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoricoStatusPlantaRepository extends JpaRepository<HistoricoStatusPlanta, Long> {
    List<HistoricoStatusPlanta> findByPlantaIdOrderByAlteradoEmDesc(Long plantaId);
    List<HistoricoStatusPlanta> findAllByOrderByAlteradoEmDesc();
}
