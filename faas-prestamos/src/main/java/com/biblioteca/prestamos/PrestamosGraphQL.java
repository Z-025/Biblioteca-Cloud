package com.biblioteca.prestamos;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import com.google.gson.Gson;
import java.util.Map;
import java.util.Optional;

public class PrestamosGraphQL {
    private GraphQL graphQL;

    public PrestamosGraphQL() {
        String schema = "type Prestamo { id_prestamo: ID! titulo: String nombre: String usuario: String fecha_prestamo: String } type Query { prestamos: [Prestamo] }";
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schema);
        
        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring.dataFetcher("prestamos", env -> PrestamosRepository.obtenerPrestamos()))
                .build();
                
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    @FunctionName("GraphQLPrestamos")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS, route = "graphql/prestamos") HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        
        try {
            String body = request.getBody().orElse("");
            Gson gson = new Gson();
            Map requestMap = gson.fromJson(body, Map.class);
            
            ExecutionResult executionResult = graphQL.execute((String) requestMap.get("query"));
            
            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(gson.toJson(executionResult.toSpecification()))
                    .build();
        } catch (Exception e) {
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage()).build();
        }
    }
}