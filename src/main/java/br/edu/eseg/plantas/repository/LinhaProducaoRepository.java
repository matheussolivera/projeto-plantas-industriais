package br.edu.eseg.plantas.repository;

import br.edu.eseg.plantas.model.LinhaProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LinhaProducaoRepository extends JpaRepository<LinhaProducao, Long> {
    List<LinhaProducao> findByPlantaIdOrderByCodigoLinhaAsc(Long plantaId);
    long countByPlantaIdAndAtivaTrue(Long plantaId);
    boolean existsByPlantaIdAndCodigoLinhaIgnoreCase(Long plantaId, String codigoLinha);
}
