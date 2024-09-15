package br.ufscar.dc.dsw.EstagioT2.controller;

import br.ufscar.dc.dsw.EstagioT2.domain.Empresa;
import br.ufscar.dc.dsw.EstagioT2.domain.Profissional;
import br.ufscar.dc.dsw.EstagioT2.domain.Usuario;
import br.ufscar.dc.dsw.EstagioT2.domain.Vaga;
import br.ufscar.dc.dsw.EstagioT2.dto.EmpresaDTO;
import br.ufscar.dc.dsw.EstagioT2.dto.ProfissionalDTO;
import br.ufscar.dc.dsw.EstagioT2.service.EmpresaService;
import br.ufscar.dc.dsw.EstagioT2.service.ProfissionalService;
import br.ufscar.dc.dsw.EstagioT2.service.UsuarioService;
import br.ufscar.dc.dsw.EstagioT2.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiRestController {

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VagaService vagaService;


    // Endpoint para retornar todos os profissionais
    @GetMapping("/profissionais")
    public ResponseEntity<List<Profissional>> listarTodosProfissionais() {
        List<Profissional> profissionais = profissionalService.listarTodos();

        // Verifica se há profissionais e retorna a lista
        if (profissionais.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna status 204 (No Content) se não houver profissionais
        }

        // Retorna a lista de profissionais com status 200 (OK)
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/profissionais/{id}")
    public ResponseEntity<Profissional> buscarProfissionalPorId(@PathVariable("id") Long id) {
        Optional<Profissional> profissional = Optional.ofNullable(profissionalService.buscarPorId(id));

        if (profissional.isPresent()) {
            return ResponseEntity.ok(profissional.get());
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 se o profissional não for encontrado
        }
    }

    @DeleteMapping("/profissionais/{id}")
    public ResponseEntity<Void> deletarProfissional(@PathVariable("id") Long id) {
        Optional<Profissional> profissional = Optional.ofNullable(profissionalService.buscarPorId(id));

        if (profissional.isPresent()) {
            profissionalService.excluir(id);
            return ResponseEntity.noContent().build(); // Retorna 204 (No Content) se deletado com sucesso
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 se o profissional não for encontrado
        }
    }

    @PostMapping("/profissionais")
    public ResponseEntity<Profissional> criarProfissional(@RequestBody ProfissionalDTO profissionalDTO) {
        try {
            // Verifica se o email já está em uso
            Optional<Usuario> usuarioExistente = usuarioService.buscarPorEmail(profissionalDTO.getEmail());
            if (usuarioExistente.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Retorna 409 se o email já estiver em uso
            }

            // Verifica se o CPF já está em uso
            Optional<Profissional> profissionalExistente = Optional.ofNullable(profissionalService.buscarPorCpf(profissionalDTO.getCpf()));
            if (profissionalExistente.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Retorna 409 se o CPF já estiver em uso
            }

            // Criação do profissional e do usuário a partir dos dados do DTO
            Usuario usuario = new Usuario();
            usuario.setEmail(profissionalDTO.getEmail());
            usuario.setSenha(passwordEncoder.encode(profissionalDTO.getSenha()));
            usuario.setTipo(Usuario.TipoUsuario.profissional);

            Profissional profissional = new Profissional();
            profissional.setNome(profissionalDTO.getNome());
            profissional.setCpf(profissionalDTO.getCpf());
            profissional.setTelefone(profissionalDTO.getTelefone());
            profissional.setSexo(profissionalDTO.getSexo());
            profissional.setDataNascimento(profissionalDTO.getDataNascimento());
            profissional.setUsuario(usuario); // Associação do profissional com o usuário

            // Verifica se todos os campos obrigatórios foram fornecidos
            if (profissionalDTO.getNome() == null || profissionalDTO.getCpf() == null || profissionalDTO.getTelefone() == null
                    || profissionalDTO.getSexo() == null || profissionalDTO.getDataNascimento() == null
                    || profissionalDTO.getEmail() == null || profissionalDTO.getSenha() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Retorna 400 se faltar algum campo obrigatório
            }

            // Salva o usuário e o profissional no banco de dados
            usuarioService.salvar(usuario);
            profissionalService.salvar(profissional);

            return ResponseEntity.status(HttpStatus.CREATED).body(profissional); // Retorna 201 e o profissional criado
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorna 500 se houver erro
        }
    }

    @PutMapping("/profissionais/{id}")
    public ResponseEntity<String> atualizarProfissionalParcial(@PathVariable Long id, @RequestBody ProfissionalDTO profissionalDTO) {
        try {
            // Busca o profissional pelo ID
            Optional<Profissional> optionalProfissional = Optional.ofNullable(profissionalService.buscarPorId(id));
            if (!optionalProfissional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profissional não encontrado.");
            }

            // Atualiza as informações do profissional
            Profissional profissional = optionalProfissional.get();

            // Atualiza os campos somente se forem fornecidos
            if (profissionalDTO.getNome() != null) {
                profissional.setNome(profissionalDTO.getNome());
            }
            if (profissionalDTO.getCpf() != null) {
                profissional.setCpf(profissionalDTO.getCpf());
            }
            if (profissionalDTO.getTelefone() != null) {
                profissional.setTelefone(profissionalDTO.getTelefone());
            }
            if (profissionalDTO.getSexo() != null) {
                profissional.setSexo(profissionalDTO.getSexo());
            }
            if (profissionalDTO.getDataNascimento() != null) {
                profissional.setDataNascimento(profissionalDTO.getDataNascimento());
            }

            // Atualiza também o usuário (email e senha) se forem fornecidos
            Usuario usuario = profissional.getUsuario();
            if (profissionalDTO.getEmail() != null) {
                usuario.setEmail(profissionalDTO.getEmail());
            }
            if (profissionalDTO.getSenha() != null && !profissionalDTO.getSenha().isEmpty()) {
                usuario.setSenha(passwordEncoder.encode(profissionalDTO.getSenha()));
            }

            // Salva as alterações
            usuarioService.salvar(usuario);
            profissionalService.salvar(profissional);

            return ResponseEntity.ok("Profissional atualizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o profissional.");
        }
    }





    @GetMapping("/empresas")
    public ResponseEntity<List<Empresa>> listarTodasEmpresas() {
        List<Empresa> empresas = empresaService.listarTodas();

        if (empresas.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 se não houver empresas
        }

        return ResponseEntity.ok(empresas); // Retorna 200 com a lista de empresas
    }

    @GetMapping("/empresas/{id}")
    public ResponseEntity<Empresa> buscarEmpresaPorId(@PathVariable("id") Long id) {
        Optional<Empresa> empresa = Optional.ofNullable(empresaService.buscarPorId(id));

        if (empresa.isPresent()) {
            return ResponseEntity.ok(empresa.get());
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 se a empresa não for encontrada
        }
    }

    @DeleteMapping("/empresas/{id}")
    public ResponseEntity<Void> deletarEmpresa(@PathVariable("id") Long id) {
        Optional<Empresa> empresa = Optional.ofNullable(empresaService.buscarPorId(id));

        if (empresa.isPresent()) {
            empresaService.excluir(id);
            return ResponseEntity.noContent().build(); // Retorna 204 se deletado com sucesso
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 se a empresa não for encontrada
        }
    }

    @PostMapping("/empresas")
    public ResponseEntity<String> criarEmpresa(@RequestBody EmpresaDTO empresaDTO) {
        try {
            // Verifica se o CNPJ já está em uso
            if (empresaService.buscarPorCnpj(empresaDTO.getCnpj()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("CNPJ já está em uso.");
            }

            // Verifica se o email já está em uso por algum usuário
            if (usuarioService.buscarPorEmail(empresaDTO.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já está em uso.");
            }

            // Criação do objeto Empresa e Usuario
            Empresa novaEmpresa = new Empresa();
            novaEmpresa.setNome(empresaDTO.getNome());
            novaEmpresa.setCnpj(empresaDTO.getCnpj());
            novaEmpresa.setDescricao(empresaDTO.getDescricao());
            novaEmpresa.setCidade(empresaDTO.getCidade());

            Usuario novoUsuario = new Usuario();
            novoUsuario.setEmail(empresaDTO.getEmail());
            novoUsuario.setSenha(passwordEncoder.encode(empresaDTO.getSenha())); // Criptografa a senha
            novoUsuario.setTipo(Usuario.TipoUsuario.empresa); // Define o tipo como empresa

            // Associa o usuário à empresa
            novaEmpresa.setUsuario(novoUsuario);

            // Salva o usuário e a empresa no banco de dados
            usuarioService.salvar(novoUsuario);
            empresaService.salvar(novaEmpresa);

            return ResponseEntity.status(HttpStatus.CREATED).body("Empresa criada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar empresa.");
        }
    }


    @PutMapping("/empresas/{id}")
    public ResponseEntity<String> atualizarEmpresa(@PathVariable Long id, @RequestBody EmpresaDTO empresaDTO) {
        try {
            // Busca a empresa existente pelo ID
            Empresa empresaExistente = empresaService.buscarPorId(id);
            if (empresaExistente == null) {
                return ResponseEntity.notFound().build(); // Retorna 404 se a empresa não for encontrada
            }

            // Verifica se o CNPJ está sendo atualizado e se já está em uso por outra empresa
            if (empresaDTO.getCnpj() != null && !empresaDTO.getCnpj().equals(empresaExistente.getCnpj())) {
                if (empresaService.buscarPorCnpj(empresaDTO.getCnpj()) != null) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("CNPJ já está em uso.");
                }
                empresaExistente.setCnpj(empresaDTO.getCnpj()); // Atualiza o CNPJ
            }

            // Atualiza os campos enviados (se forem diferentes e não nulos)
            if (empresaDTO.getNome() != null) {
                empresaExistente.setNome(empresaDTO.getNome());
            }
            if (empresaDTO.getDescricao() != null) {
                empresaExistente.setDescricao(empresaDTO.getDescricao());
            }
            if (empresaDTO.getCidade() != null) {
                empresaExistente.setCidade(empresaDTO.getCidade());
            }

            // Verifica se o email está sendo atualizado e se já está em uso por outro usuário
            if (empresaDTO.getEmail() != null && !empresaDTO.getEmail().equals(empresaExistente.getUsuario().getEmail())) {
                if (usuarioService.buscarPorEmail(empresaDTO.getEmail()).isPresent()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já está em uso.");
                }
                empresaExistente.getUsuario().setEmail(empresaDTO.getEmail()); // Atualiza o email
            }

            // Atualiza a senha se for enviada (criptografada)
            if (empresaDTO.getSenha() != null && !empresaDTO.getSenha().isEmpty()) {
                empresaExistente.getUsuario().setSenha(passwordEncoder.encode(empresaDTO.getSenha())); // Criptografa e atualiza a senha
            }

            // Salva a empresa atualizada
            empresaService.salvar(empresaExistente);

            return ResponseEntity.ok("Empresa atualizada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar a empresa.");
        }
    }

    @GetMapping("/empresas/cidades/{nome}")
    public ResponseEntity<List<Empresa>> listarEmpresasPorCidade(@PathVariable("nome") String nome) {
        // Busca empresas pela cidade
        List<Empresa> empresas = empresaService.buscarPorCidade(nome);

        // Verifica se a lista está vazia
        if (empresas.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 se não houver empresas na cidade
        }

        // Retorna a lista de empresas
        return ResponseEntity.ok(empresas); // Retorna 200 com a lista de empresas da cidade
    }


    @GetMapping("/vagas")
    public ResponseEntity<List<Vaga>> listarTodasVagas() {
        List<Vaga> vagas = vagaService.listarTodas();

        if (vagas.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 se não houver vagas
        }

        return ResponseEntity.ok(vagas); // Retorna 200 com a lista de vagas
    }

    @GetMapping("/vagas/{id}")
    public ResponseEntity<Vaga> buscarVagaPorId(@PathVariable("id") Long id) {
        Optional<Vaga> vaga = Optional.ofNullable(vagaService.buscarPorId(id));

        if (vaga.isPresent()) {
            return ResponseEntity.ok(vaga.get());
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 se a vaga não for encontrada
        }
    }





    @GetMapping("/vagas/empresas/{id}")
    public ResponseEntity<List<Vaga>> listarVagasEmAbertoPorEmpresa(@PathVariable("id") Long id) {
        // Busca a empresa pelo ID
        Optional<Empresa> empresa = Optional.ofNullable(empresaService.buscarPorId(id));

        if (empresa.isPresent()) {
            // Busca todas as vagas da empresa
            List<Vaga> vagas = vagaService.buscarPorEmpresa(empresa.get());

            // Obtém a data atual
            LocalDate hoje = LocalDate.now();

            // Filtra as vagas cuja data limite de inscrição ainda não passou
            List<Vaga> vagasEmAberto = vagas.stream()
                    .filter(vaga -> {
                        // Converte java.sql.Date para LocalDate usando os métodos de java.sql.Date
                        java.sql.Date dataLimiteSql = (Date) vaga.getDataLimiteInscricao();
                        LocalDate dataLimite = dataLimiteSql.toLocalDate();  // Utiliza toLocalDate de java.sql.Date
                        return dataLimite.isAfter(hoje);
                    })
                    .collect(Collectors.toList());

            // Verifica se há vagas em aberto
            if (vagasEmAberto.isEmpty()) {
                return ResponseEntity.noContent().build(); // Retorna 204 se não houver vagas em aberto
            }

            return ResponseEntity.ok(vagasEmAberto); // Retorna 200 com a lista de vagas em aberto
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 se a empresa não for encontrada
        }
    }






}
