package br.com.fiap.orbit;

import br.com.fiap.orbit.service.RecomendacaoService;
import br.com.fiap.orbit.service.MissaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
class OrbitApplicationTests {

    @Autowired
    private RecomendacaoService recomendacaoService;

    @Autowired
    private MissaoService missaoService;

    @Test
    void contextLoads() {
        assertThat(recomendacaoService).isNotNull();
        assertThat(missaoService).isNotNull();
    }

    @Test
    void listarMissoesRetornaResultados() {
        var resultado = missaoService.listarTodos(org.springframework.data.domain.Pageable.ofSize(10));
        assertThat(resultado).isNotNull();
    }
}
