package com.biblioteca.Graphql;

import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLList; 
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

public final class GraphQLProvider {

    private GraphQLProvider() {
    }

    public static GraphQL buildGraphQL() {
        
        QueryResolver queryResolver = new QueryResolver();
        
        GraphQLObjectType usuarioType = GraphQLObjectType.newObject()
            .name("Usuario")
            .field(field -> field.name("id_usuario").type(Scalars.GraphQLInt))
            .field(field -> field.name("nombre").type(Scalars.GraphQLString))
            .field(field -> field.name("email").type(Scalars.GraphQLString))
            .build();

        GraphQLObjectType queryType = GraphQLObjectType.newObject()
            .name("Query")
            
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("usuarios")
                .type(GraphQLList.list(usuarioType))
                .dataFetcher(environment -> queryResolver.getUsuarios()))
                
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("usuarioPorId") 
                .type(usuarioType)   
                .argument(GraphQLArgument.newArgument()
                    .name("id")
                    .type(Scalars.GraphQLInt))
                .dataFetcher(environment -> {
                    int idBuscado = environment.getArgument("id");
                    return queryResolver.getUsuarioPorId(idBuscado);
                }))
            .build();

        GraphQLSchema schema = GraphQLSchema.newSchema()
            .query(queryType)
            .build();

        return GraphQL.newGraphQL(schema).build();
    }
}