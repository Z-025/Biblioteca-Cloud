CREATE TABLE USUARIOS (
    id_usuario NUMBER PRIMARY KEY,
    nombre VARCHAR2(100),
    email VARCHAR2(100)
);

CREATE TABLE LIBROS (
    id_libro NUMBER PRIMARY KEY,
    titulo VARCHAR2(200),
    estado VARCHAR2(20)
);

CREATE TABLE PRESTAMOS (
    id_prestamo NUMBER PRIMARY KEY,
    id_usuario NUMBER,
    id_libro NUMBER,
    fecha_prestamo DATE,
    FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id_usuario),
    FOREIGN KEY (id_libro) REFERENCES LIBROS(id_libro)
);

INSERT INTO USUARIOS VALUES (1, 'Pamela Azua', 'pamela.azua@duocuc.cl');
INSERT INTO LIBROS VALUES (101, 'Cloud Native Development', 'Disponible');
INSERT INTO PRESTAMOS VALUES (501, 1, 101, SYSDATE);

COMMIT;