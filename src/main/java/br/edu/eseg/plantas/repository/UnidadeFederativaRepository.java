package br.edu.eseg.plantas.repository;

import br.edu.eseg.plantas.model.UnidadeFederativa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UnidadeFederativaRepository extends JpaRepository<UnidadeFederativa, Long> {
    Optional<UnidadeFederativa> findBySiglaIgnoreCase(String sigla);
    List<UnidadeFederativa> findAllByOrderBySiglaAsc();
}
