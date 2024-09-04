package br.ufscar.dc.dsw.EstagioT2;

import br.ufscar.dc.dsw.EstagioT2.domain.Empresa;
import br.ufscar.dc.dsw.EstagioT2.domain.Profissional;
import br.ufscar.dc.dsw.EstagioT2.domain.Usuario;
import br.ufscar.dc.dsw.EstagioT2.repository.EmpresaRepository;
import br.ufscar.dc.dsw.EstagioT2.repository.ProfissionalRepository;
import br.ufscar.dc.dsw.EstagioT2.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EstagioT2Application implements CommandLineRunner {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private ProfissionalRepository profissionalRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(EstagioT2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Verifica se o banco de dados já contém usuários
		if (usuarioRepository.count() == 0) {
			// Criando usuário do tipo Empresa
			Usuario usuarioEmpresa = new Usuario();
			usuarioEmpresa.setEmail("empresa1@example.com");
			usuarioEmpresa.setSenha(passwordEncoder.encode("empresa123"));
			usuarioEmpresa.setTipo(Usuario.TipoUsuario.empresa);
			usuarioRepository.save(usuarioEmpresa);
			Empresa empresa = new Empresa();
			empresa.setNome("Empresa Exemplo 1");
			empresa.setCnpj("12.345.678/0001-99");
			empresa.setDescricao("Descrição da Empresa Exemplo 1");
			empresa.setCidade("Cidade Exemplo 1");
			empresa.setUsuario(usuarioEmpresa);
			empresaRepository.save(empresa);

			Usuario usuarioEmpresa2 = new Usuario();
			usuarioEmpresa2.setEmail("empresa2@example.com");
			usuarioEmpresa2.setSenha(passwordEncoder.encode("empresa123"));
			usuarioEmpresa2.setTipo(Usuario.TipoUsuario.empresa);
			usuarioRepository.save(usuarioEmpresa2);
			Empresa empresa2 = new Empresa();
			empresa2.setNome("Empresa Exemplo 2");
			empresa2.setCnpj("12.345.678/0002-99");
			empresa2.setDescricao("Descrição da Empresa Exemplo 2");
			empresa2.setCidade("Cidade Exemplo 2");
			empresa2.setUsuario(usuarioEmpresa2);
			empresaRepository.save(empresa2);

			Usuario usuarioEmpresa3 = new Usuario();
			usuarioEmpresa3.setEmail("empresa3@example.com");
			usuarioEmpresa3.setSenha(passwordEncoder.encode("empresa123"));
			usuarioEmpresa3.setTipo(Usuario.TipoUsuario.empresa);
			usuarioRepository.save(usuarioEmpresa3);
			Empresa empresa3 = new Empresa();
			empresa3.setNome("Empresa Exemplo 3");
			empresa3.setCnpj("12.345.678/0003-99");
			empresa3.setDescricao("Descrição da Empresa Exemplo 3");
			empresa3.setCidade("Cidade Exemplo 3");
			empresa3.setUsuario(usuarioEmpresa3);
			empresaRepository.save(empresa3);

			



			// Criando usuário do tipo Profissional
			Usuario usuarioProfissional = new Usuario();
			usuarioProfissional.setEmail("profissional@example.com");
			usuarioProfissional.setSenha(passwordEncoder.encode("profissional123"));
			usuarioProfissional.setTipo(Usuario.TipoUsuario.profissional);
			usuarioRepository.save(usuarioProfissional);

			Profissional profissional = new Profissional();
			profissional.setNome("Profissional Exemplo");
			profissional.setCpf("123.456.789-00");
			profissional.setTelefone("11 98765-4321");
			profissional.setUsuario(usuarioProfissional);
			profissionalRepository.save(profissional);

			Usuario usuarioAdmin = new Usuario();
			usuarioAdmin.setEmail("admin@example.com");
			usuarioAdmin.setSenha(passwordEncoder.encode("admin123"));
			usuarioAdmin.setTipo(Usuario.TipoUsuario.admin);
			usuarioRepository.save(usuarioAdmin);

			System.out.println("Usuários de teste criados com sucesso!");
		} else {
			System.out.println("Usuários já existem no banco de dados. Nenhum dado foi criado.");
		}
	}
}
