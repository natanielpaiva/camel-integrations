package ms.exemplo.aluno;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Aluno {

    private Long matricula;
    private String nome;
    private Long idade;

}
