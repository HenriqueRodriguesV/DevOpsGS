package br.com.fiap.orbit;

import br.com.fiap.orbit.model.*;
import br.com.fiap.orbit.model.enums.*;
import br.com.fiap.orbit.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile({"dev", "default", "docker"})
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final VeiculoRepository veiculoRepository;
    private final PontoDeApoioRepository pontoDeApoioRepository;
    private final MissaoRepository missaoRepository;
    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) return;

        log.info("Inicializando dados de exemplo ORBIT...");

        Usuario admin = usuarioRepository.save(Usuario.builder()
                .nome("Henrique Rodrigues Vespasiano")
                .email("rm562917@fiap.com.br")
                .senha(passwordEncoder.encode("orbit2026"))
                .role("ROLE_ADMIN")
                .build());

        usuarioRepository.save(Usuario.builder()
                .nome("Gabriely Bonfim Silva")
                .email("rm566242@fiap.com.br")
                .senha(passwordEncoder.encode("orbit2026"))
                .role("ROLE_USER")
                .build());

        VeiculoTerrestre rover = (VeiculoTerrestre) veiculoRepository.save(VeiculoTerrestre.builder()
                .nome("Rover Alpha-1")
                .modelo("MARS-RTX 3000")
                .tipo(TipoVeiculo.TERRESTRE)
                .autonomiaKm(250.0)
                .nivelCombustivel(0.45)
                .capacidadeCarga(500.0)
                .ativo(true)
                .tipoTracao("6x6 Elétrico")
                .alturaMaxima(1.2)
                .build());

        VeiculoAereo drone = (VeiculoAereo) veiculoRepository.save(VeiculoAereo.builder()
                .nome("Drone Sentinel-7")
                .modelo("UAV-HEAVY-X")
                .tipo(TipoVeiculo.AEREO)
                .autonomiaKm(120.0)
                .nivelCombustivel(0.70)
                .capacidadeCarga(80.0)
                .ativo(true)
                .alcanceVertical(5000.0)
                .capacidadePassageiros(0)
                .build());

        PontoDeApoio base1 = pontoDeApoioRepository.save(PontoDeApoio.builder()
                .nome("Base Korolev")
                .descricao("Base terrestre principal com suprimentos completos")
                .tipo(TipoPontoDeApoio.BASE_TERRESTRE)
                .coordenadas(new Coordenadas(-23.5505, -46.6333))
                .capacidadeAtendimento(50)
                .ativo(true)
                .build());

        Recurso combustivel1 = Recurso.builder()
                .nome("Hidrogênio Líquido")
                .descricao("Combustível criogênico para propulsão")
                .tipo(TipoRecurso.COMBUSTIVEL)
                .quantidade(5000.0)
                .unidade("litros")
                .pontoDeApoio(base1)
                .build();

        Recurso medico1 = Recurso.builder()
                .nome("Kit Médico de Emergência")
                .descricao("Equipamento médico completo para trauma")
                .tipo(TipoRecurso.EQUIPAMENTO_MEDICO)
                .quantidade(20.0)
                .unidade("unidades")
                .pontoDeApoio(base1)
                .build();

        base1.getRecursos().addAll(List.of(combustivel1, medico1));
        pontoDeApoioRepository.save(base1);

        PontoDeApoio hub1 = pontoDeApoioRepository.save(PontoDeApoio.builder()
                .nome("Hub Logístico Delta")
                .descricao("Hub de redistribuição de recursos em zona remota")
                .tipo(TipoPontoDeApoio.HUB_LOGISTICO)
                .coordenadas(new Coordenadas(-22.9068, -43.1729))
                .capacidadeAtendimento(30)
                .ativo(true)
                .build());

        Recurso suprimento1 = Recurso.builder()
                .nome("Rações de Emergência")
                .descricao("Suprimentos alimentares para 30 dias")
                .tipo(TipoRecurso.SUPRIMENTO)
                .quantidade(300.0)
                .unidade("pacotes")
                .pontoDeApoio(hub1)
                .build();

        hub1.getRecursos().add(suprimento1);
        pontoDeApoioRepository.save(hub1);

        PontoDeApoio estacaoRemota = pontoDeApoioRepository.save(PontoDeApoio.builder()
                .nome("Estação Remota Omega")
                .descricao("Posto avançado em área de alto risco")
                .tipo(TipoPontoDeApoio.ESTACAO_REMOTA)
                .coordenadas(new Coordenadas(-15.7801, -47.9292))
                .capacidadeAtendimento(10)
                .ativo(true)
                .build());

        Recurso energia1 = Recurso.builder()
                .nome("Células de Energia Solar")
                .descricao("Painéis portáteis de alta eficiência")
                .tipo(TipoRecurso.ENERGIA)
                .quantidade(15.0)
                .unidade("unidades")
                .pontoDeApoio(estacaoRemota)
                .build();

        estacaoRemota.getRecursos().add(energia1);
        pontoDeApoioRepository.save(estacaoRemota);

        Missao missaoCritica = missaoRepository.save(Missao.builder()
                .titulo("Operação Escudo Vermelho")
                .descricao("Resgate de equipe em zona de risco extremo — tempestade de areia detectada")
                .origem(new Coordenadas(-20.0, -44.0))
                .status(StatusMissao.CRITICA)
                .dataInicio(LocalDateTime.now().minusHours(3))
                .nivelRisco(8.5)
                .veiculo(rover)
                .build());

        Pessoa op1 = pessoaRepository.save(Pessoa.builder()
                .nome("Carlos Andrade")
                .matricula("OP-001")
                .funcao("Piloto de Rover")
                .nivelCondicaoFisica(7)
                .nivelHabilidade(9)
                .missao(missaoCritica)
                .build());

        Pessoa op2 = pessoaRepository.save(Pessoa.builder()
                .nome("Fernanda Lima")
                .matricula("OP-002")
                .funcao("Engenheira de Sistemas")
                .nivelCondicaoFisica(6)
                .nivelHabilidade(8)
                .missao(missaoCritica)
                .build());

        Missao missaoEmAndamento = missaoRepository.save(Missao.builder()
                .titulo("Inspeção Drone Região Norte")
                .descricao("Reconhecimento aéreo de área para instalação de estação")
                .origem(new Coordenadas(-10.0, -50.0))
                .status(StatusMissao.EM_ANDAMENTO)
                .dataInicio(LocalDateTime.now().minusHours(1))
                .nivelRisco(3.5)
                .veiculo(drone)
                .build());

        log.info("Dados de exemplo carregados com sucesso. Acesse /swagger-ui.html para explorar a API.");
    }
}
