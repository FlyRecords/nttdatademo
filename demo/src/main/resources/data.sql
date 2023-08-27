CREATE TABLE usuario (
  id INT GENERATED BY DEFAULT AS IDENTITY,
  name VARCHAR(45),
  uuidusuario VARCHAR(255),
  email VARCHAR(45),
  password VARCHAR(45),
  token VARCHAR(36),
  isactive BIT NOT NULL,
  created DATETIME,
  modified DATETIME NULL,
  last_login DATETIME NULL,
  CONSTRAINT pk_Usuarios PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS phone (
  id INT GENERATED BY DEFAULT AS IDENTITY,
  number VARCHAR(7),
  cityCode VARCHAR(2),
  contrycode VARCHAR(2),
  idusuario INT,
  CONSTRAINT pk_phones PRIMARY KEY (id),
  CONSTRAINT fk_phones_Usuarios
    FOREIGN KEY (idusuario)
    REFERENCES usuario (id)
);

