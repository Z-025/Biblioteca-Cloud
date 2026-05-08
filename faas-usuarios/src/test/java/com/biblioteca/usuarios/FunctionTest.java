package com.biblioteca.usuarios;

import com.microsoft.azure.functions.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.util.*;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FunctionTest {
    @Test
    public void testObtenerUsuariosGraphQL() throws Exception {
        // 1. Mock de la petición con el tipo correcto: Optional<Map<String, Object>>
        @SuppressWarnings("unchecked")
        final HttpRequestMessage<Optional<Map<String, Object>>> req = mock(HttpRequestMessage.class);

        // 2. Creamos un body de prueba que GraphQL entienda
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("query", "{ usuarios { nombre } }");
        final Optional<Map<String, Object>> queryBody = Optional.of(bodyMap);
        
        doReturn(queryBody).when(req).getBody();

        // 3. Configuración del Mock para la respuesta
        doAnswer(new Answer<HttpResponseMessage.Builder>() {
            @Override
            public HttpResponseMessage.Builder answer(InvocationOnMock invocation) {
                HttpStatus status = (HttpStatus) invocation.getArguments()[0];
                return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
            }
        }).when(req).createResponseBuilder(any(HttpStatus.class));

        final ExecutionContext context = mock(ExecutionContext.class);
        doReturn(Logger.getGlobal()).when(context).getLogger();

        // 4. Instanciamos la clase CORRECTA: UsuariosGraphQL
        final HttpResponseMessage ret = new UsuariosGraphQL().run(req, context);

        // 5. Verificamos que el estado sea 200 OK
        assertEquals(HttpStatus.OK, ret.getStatus());
    }
}