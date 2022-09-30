package ms.exemplo.camel.orch;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

@Service("alunoService")
public class AlunoService {

    private final Map<Integer, Aluno> alunos = new TreeMap<>();

    public AlunoService() {
        alunos.put(1, new Aluno(1, "Nataniel", 18L));
        alunos.put(2, new Aluno(2, "Nataniel Paiva", 18L));
        alunos.put(3, new Aluno(3, "Nataniel Amorim", 18L));
    }

    public Aluno buscaAluno(Integer id) {
        return alunos.get(id);
    }

    public Collection<Aluno> buscaTodos() {
        return alunos.values();
    }

    public void atualiza(Aluno user) {
        alunos.put(user.getMatricula(), user);
    }

}
