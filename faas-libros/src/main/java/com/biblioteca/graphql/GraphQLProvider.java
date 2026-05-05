package com.biblioteca.graphql;

import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLList; 
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

public final class GraphQLProvider {

    public static GraphQL buildGraphQL() {
        QueryResolver queryResolver = new QueryResolver();
        
        GraphQLObjectType libroType = GraphQLObjectType.newObject()
            .name("Libro")
            .field(field -> field.name("id_libro").type(Scalars.GraphQLInt))
            .field(field -> field.name("titulo").type(Scalars.GraphQLString))
            .build();

        GraphQLObjectType queryType = GraphQLObjectType.newObject()
            .name("Query")
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("libros")
                .type(GraphQLList.list(libroType))
                .dataFetcher(environment -> queryResolver.getLibros()))
            .build();

        GraphQLSchema schema = GraphQLSchema.newSchema().query(queryType).build();
        return GraphQL.newGraphQL(schema).build();
    }
}
