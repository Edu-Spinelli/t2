package br.ufscar.dc.dsw.EstagioT2;

import br.ufscar.dc.dsw.EstagioT2.domain.*;
import br.ufscar.dc.dsw.EstagioT2.repository.VagaRepository;
import br.ufscar.dc.dsw.EstagioT2.repository.EmpresaRepository;
import br.ufscar.dc.dsw.EstagioT2.repository.ProfissionalRepository;
import br.ufscar.dc.dsw.EstagioT2.repository.UsuarioRepository;
import br.ufscar.dc.dsw.EstagioT2.service.StatusCandidaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.math.BigDecimal;

@SpringBootApplication
@EnableWebMvc

public class EstagioT2Application implements CommandLineRunner {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private ProfissionalRepository profissionalRepository;

	@Autowired
	private VagaRepository vagaRepository;


	@Autowired
	private PasswordEncoder passwordEncoder;


	@Autowired
	private StatusCandidaturaService statusCandidaturaService;



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

			Vaga vaga = new Vaga();
			vaga.setDescricao("Vaga Exemplo 1");
			vaga.setRemuneracao(BigDecimal.valueOf(1000.00));
			vaga.setDataLimiteInscricao(java.sql.Date.valueOf("2021-12-31"));
			vaga.setCidade("Cidade Exemplo 1");
			vaga.setEmpresa(empresa);
			vagaRepository.save(vaga);

			Vaga vaga2 = new Vaga();
			vaga2.setDescricao("Vaga Exemplo 2");
			vaga2.setRemuneracao(BigDecimal.valueOf(1000.00));
			vaga2.setDataLimiteInscricao(java.sql.Date.valueOf("2021-12-31"));
			vaga2.setCidade("Cidade Exemplo 2");
			vaga2.setEmpresa(empresa2);
			vagaRepository.save(vaga2);

			Vaga vaga3 = new Vaga();
			vaga3.setDescricao("Vaga Exemplo 3");
			vaga3.setRemuneracao(BigDecimal.valueOf(1000.00));
			vaga3.setDataLimiteInscricao(java.sql.Date.valueOf("2021-12-31"));
			vaga3.setCidade("Cidade Exemplo 3");
			vaga3.setEmpresa(empresa3);
			vagaRepository.save(vaga3);






			// Criando usuário do tipo Profissional
			Usuario usuarioProfissional = new Usuario();
			usuarioProfissional.setEmail("profissional1@example.com");
			usuarioProfissional.setSenha(passwordEncoder.encode("profissional123"));
			usuarioProfissional.setTipo(Usuario.TipoUsuario.profissional);
			usuarioRepository.save(usuarioProfissional);
			Profissional profissional = new Profissional();
			profissional.setNome("Profissional Exemplo 1");
			profissional.setCpf("123.456.789-01");
			profissional.setTelefone("11 98765-4321");
			profissional.setDataNascimento(java.sql.Date.valueOf("1990-01-01"));
			profissional.setUsuario(usuarioProfissional);
			profissionalRepository.save(profissional);

			Usuario usuarioProfissional2 = new Usuario();
			usuarioProfissional2.setEmail("profissional2@example.com");
			usuarioProfissional2.setSenha(passwordEncoder.encode("profissional123"));
			usuarioProfissional2.setTipo(Usuario.TipoUsuario.profissional);
			usuarioRepository.save(usuarioProfissional2);
			Profissional profissional2 = new Profissional();
			profissional2.setNome("Profissional Exemplo 2");
			profissional2.setCpf("123.456.789-02");
			profissional2.setTelefone("11 98765-4322");
			profissional2.setDataNascimento(java.sql.Date.valueOf("1990-01-01"));
			profissional2.setUsuario(usuarioProfissional2);
			profissionalRepository.save(profissional2);

			Usuario usuarioProfissional3 = new Usuario();
			usuarioProfissional3.setEmail("profissional3@example.com");
			usuarioProfissional3.setSenha(passwordEncoder.encode("profissional123"));
			usuarioProfissional3.setTipo(Usuario.TipoUsuario.profissional);
			usuarioRepository.save(usuarioProfissional3);
			Profissional profissional3 = new Profissional();
			profissional3.setNome("Profissional Exemplo 3");
			profissional3.setCpf("123.456.789-03");
			profissional3.setTelefone("11 98765-4323");
			profissional3.setDataNascimento(java.sql.Date.valueOf("1990-01-01"));
			profissional3.setUsuario(usuarioProfissional3);
			profissionalRepository.save(profissional3);






			Usuario usuarioAdmin = new Usuario();
			usuarioAdmin.setEmail("admin@example.com");
			usuarioAdmin.setSenha(passwordEncoder.encode("admin123"));
			usuarioAdmin.setTipo(Usuario.TipoUsuario.admin);
			usuarioRepository.save(usuarioAdmin);

			statusCandidaturaService.salvar(new StatusCandidatura("ABERTO"));
			statusCandidaturaService.salvar(new StatusCandidatura("NÃO SELECIONADO"));
			statusCandidaturaService.salvar(new StatusCandidatura("SELECIONADO"));




			System.out.println("Usuários de teste criados com sucesso!");
		} else {
			System.out.println("Usuários já existem no banco de dados. Nenhum dado foi criado.");
		}
	}
}
