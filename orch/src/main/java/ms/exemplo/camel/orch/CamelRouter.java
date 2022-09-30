package ms.exemplo.camel.orch;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestParamType.body;
import static org.apache.camel.model.rest.RestParamType.path;

/**
 * Um exemplo simples de aplicao rest usando Camel
 */
@Component
public class CamelRouter extends RouteBuilder {

    @Autowired
    private Environment env;

    @Value("${camel.servlet.mapping.context-path}")
    private String contextPath;

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .enableCORS(true)
                .port(env.getProperty("server.port", "8080"))
                .contextPath(contextPath.substring(0, contextPath.length() - 2))
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Orch Camel Exemplo")
                .apiProperty("api.version", "1.0.0");

        rest("/alunos").description("Aluno REST service")
                .consumes("application/json")
                .produces("application/json")

                .get().description("Busca todos os alunos").outType(Aluno[].class)
                .responseMessage().code(200).message("Todos os alunos retornados com sucesso!").endResponseMessage()
                .to("bean:alunoService?method=buscaTodos")

                .get("/{matricula}").description("Busca Aluno por matricula")
                .outType(Aluno.class)
                .param().name("matricula").type(path).description("matricula do aluno").dataType("integer").endParam()
                .responseMessage().code(200).message("User successfully returned").endResponseMessage()
                .to("bean:alunoService?method=buscaAluno(${header.matricula})")

                .put("/{matricula}").description("Matricula Aluno").type(Aluno.class)
                .param().name("matricula").type(path).description("A matricula do aluno a ser atualizado")
                .dataType("integer").endParam()
                .param().name("body").type(body).description("O aluno a ser atualizado").endParam()
                .responseMessage().code(204).message("Aluno Atualizado com sucesso!").endResponseMessage()
                .to("direct:atualiza-aluno");

        from("direct:atualiza-aluno")
                .to("bean:alunoService?method=atualiza")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(204))
                .setBody(constant(""));

        rest("/alunos/v2").description("Aluno REST service")
                .consumes("application/json")
                .produces("application/json")
                .get().description("Busca todos os alunos").outType(Aluno.class)
                .responseMessage().code(200).message("Todos os alunos retornados com sucesso!").endResponseMessage()
                .to("direct:busca-aluno-legado");

        from("direct:busca-aluno-legado")
                .routeId("buscaAlunoLegado")
                .removeHeaders("*")
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("http://localhost:9090/aluno")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .log("dado do que vem do legado ${body}")
                .unmarshal().json(JsonLibrary.Jackson)
                .end();


    }

}