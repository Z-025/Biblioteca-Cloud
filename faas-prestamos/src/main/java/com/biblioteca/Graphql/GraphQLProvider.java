package com.biblioteca.Graphql;

import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

public final class GraphQLProvider {

    private GraphQLProvider() {
    }

    public static GraphQL buildGraphQL() {
        
        QueryResolver queryResolver = new QueryResolver();

        // Estructura del Préstamo
        GraphQLObjectType prestamoType = GraphQLObjectType.newObject()
            .name("Prestamo")
            .field(field -> field.name("id_prestamo").type(Scalars.GraphQLInt))
            .field(field -> field.name("titulo").type(Scalars.GraphQLString))
            .field(field -> field.name("nombre").type(Scalars.GraphQLString))
            .field(field -> field.name("fecha_prestamo").type(Scalars.GraphQLString))
            .build();

        // Query con filtro por ID
        GraphQLObjectType queryType = GraphQLObjectType.newObject()
            .name("Query")
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("prestamoPorId") // Nuevo nombre de la consulta
                .type(prestamoType)    // Devuelve UN solo préstamo
                .argument(GraphQLArgument.newArgument()
                    .name("id")
                    .type(Scalars.GraphQLInt)) // Pide el ID obligatorio
                .dataFetcher(environment -> {
                    int idBuscado = environment.getArgument("id");
                    return queryResolver.getPrestamoPorId(idBuscado);
                }))
            .build();

        GraphQLSchema schema = GraphQLSchema.newSchema()
            .query(queryType)
            .build();

        return GraphQL.newGraphQL(schema).build();
    }
}