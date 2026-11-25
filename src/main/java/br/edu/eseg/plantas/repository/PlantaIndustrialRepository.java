package br.edu.eseg.plantas.repository;

import br.edu.eseg.plantas.model.PlantaIndustrial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlantaIndustrialRepository extends JpaRepository<PlantaIndustrial, Long>,
        JpaSpecificationExecutor<PlantaIndustrial> {

    boolean existsByCodigoPlantaIgnoreCase(String codigoPlanta);
    boolean existsByCodigoPlantaIgnoreCaseAndIdNot(String codigoPlanta, Long id);

    boolean existsByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
