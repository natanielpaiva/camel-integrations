package ms.exemplo.aluno;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("aluno")
@Slf4j
public class AlunoController {

    @GetMapping
    public Aluno buscarAluno(){
        log.info("entrou no endpoint do legado");
        return Aluno.builder()
                .matricula(121654L)
                .nome("Nataniel Paiva")
                .idade(18L)
                .build();
    }

    @GetMapping("vazio")
    public Aluno[] retornaVazio(){
        log.info("entrou no endpoint do legado");
        return null;
    }


}
