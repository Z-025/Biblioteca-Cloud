package com.biblioteca.Graphql;

import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

public final class GraphQLProvider {

    private GraphQLProvider() {
    }

    public static GraphQL buildGraphQL() {
        
        QueryResolver queryResolver = new QueryResolver();

        GraphQLObjectType prestamoType = GraphQLObjectType.newObject()
            .name("Prestamo")
            .field(field -> field.name("id_prestamo").type(Scalars.GraphQLInt))
            .field(field -> field.name("titulo").type(Scalars.GraphQLString))
            .field(field -> field.name("nombre").type(Scalars.GraphQLString))
            .field(field -> field.name("fecha_prestamo").type(Scalars.GraphQLString))
            .build();

        GraphQLObjectType queryType = GraphQLObjectType.newObject()
            .name("Query")
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("prestamos")
                .type(GraphQLList.list(prestamoType))
                .dataFetcher(environment -> queryResolver.getPrestamos()))
            .build();

        GraphQLSchema schema = GraphQLSchema.newSchema()
            .query(queryType)
            .build();

        return GraphQL.newGraphQL(schema).build();
    }
}