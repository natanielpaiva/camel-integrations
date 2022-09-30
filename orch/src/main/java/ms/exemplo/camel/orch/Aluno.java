package ms.exemplo.camel.orch;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Aluno {

    private Integer matricula;
    private String nome;
    private Long idade;

}