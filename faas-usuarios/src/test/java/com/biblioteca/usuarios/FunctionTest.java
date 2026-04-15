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
    public void testObtenerUsuariosREST() throws Exception {
        @SuppressWarnings("unchecked")
        final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);

        final Optional<String> queryBody = Optional.empty();
        doReturn(queryBody).when(req).getBody();

        doAnswer(new Answer<HttpResponseMessage.Builder>() {
            @Override
            public HttpResponseMessage.Builder answer(InvocationOnMock invocation) {
                HttpStatus status = (HttpStatus) invocation.getArguments()[0];
                return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
            }
        }).when(req).createResponseBuilder(any(HttpStatus.class));

        final ExecutionContext context = mock(ExecutionContext.class);
        doReturn(Logger.getGlobal()).when(context).getLogger();

        final HttpResponseMessage ret = new UsuariosRest().run(req, context);

        assertEquals(HttpStatus.OK, ret.getStatus());
    }
}