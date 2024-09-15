package br.ufscar.dc.dsw.EstagioT2.repository;

import br.ufscar.dc.dsw.EstagioT2.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    // Aqui você pode adicionar outros métodos personalizados, se necessário
}
