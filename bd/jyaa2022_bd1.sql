-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: laboratorio2020_database:3306
-- Tiempo de generación: 01-04-2023 a las 19:17:27
-- Versión del servidor: 8.0.31
-- Versión de PHP: 8.1.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `jyaa2022_bd1`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Bolson`
--

CREATE TABLE `Bolson` (
  `cod_bolson` bigint NOT NULL,
  `cantidad` int DEFAULT NULL,
  `cod_fp` bigint DEFAULT NULL,
  `cod_ronda` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Bolson`
--

INSERT INTO `Bolson` (`cod_bolson`, `cantidad`, `cod_fp`, `cod_ronda`) VALUES
(1, 100, 2, 1),
(2, 50, 1, 1),
(3, 10, 1, 2),
(4, 24, 52, 3),
(52, 15, 52, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Bolson_SEQ`
--

CREATE TABLE `Bolson_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Bolson_SEQ`
--

INSERT INTO `Bolson_SEQ` (`next_val`) VALUES
(151);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `FamiliaProductora`
--

CREATE TABLE `FamiliaProductora` (
  `cod_fp` bigint NOT NULL,
  `fecha_afi` date DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `FamiliaProductora`
--

INSERT INTO `FamiliaProductora` (`cod_fp`, `fecha_afi`, `nombre`) VALUES
(1, '2023-01-01', 'Gomez'),
(2, '2023-01-01', 'Gonzalez'),
(3, '2023-04-01', 'Iruvia'),
(52, '2023-01-01', 'Peperoni');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `FamiliaProductora_SEQ`
--

CREATE TABLE `FamiliaProductora_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `FamiliaProductora_SEQ`
--

INSERT INTO `FamiliaProductora_SEQ` (`next_val`) VALUES
(201);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Parcela`
--

CREATE TABLE `Parcela` (
  `cod_parcela` bigint NOT NULL,
  `cantidad_surcos` int DEFAULT NULL,
  `cosecha` bit(1) DEFAULT NULL,
  `cubierta` bit(1) DEFAULT NULL,
  `verdura_cod_verdura` bigint DEFAULT NULL,
  `cod_visita` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Parcela`
--

INSERT INTO `Parcela` (`cod_parcela`, `cantidad_surcos`, `cosecha`, `cubierta`, `verdura_cod_verdura`, `cod_visita`) VALUES
(1, 0, b'0', b'0', 1, 1),
(2, 5, b'1', b'1', 1, 1),
(52, 0, b'0', b'0', 102, 1),
(102, 2, b'1', b'1', 102, 1),
(152, 0, b'0', b'0', 2, 2),
(202, 20, b'1', b'0', 2, 2),
(252, 0, b'0', b'0', 1, 52),
(302, 1, b'0', b'0', 1, 52),
(352, 0, b'0', b'0', 2, 52),
(402, 1, b'0', b'0', 2, 52),
(452, 0, b'0', b'0', 102, 52),
(502, 1, b'0', b'0', 102, 52);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Parcela_SEQ`
--

CREATE TABLE `Parcela_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Parcela_SEQ`
--

INSERT INTO `Parcela_SEQ` (`next_val`) VALUES
(601);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Quinta`
--

CREATE TABLE `Quinta` (
  `cod_quinta` bigint NOT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `geoImg` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `cod_fp` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Quinta`
--

INSERT INTO `Quinta` (`cod_quinta`, `direccion`, `geoImg`, `nombre`, `cod_fp`) VALUES
(1, 'Calle 167 2307, Los Hornos, B1910 DND, Provincia de Buenos Aires', 'https://www.google.com/maps/place/Chacra+El+Descanso/@-34.9567776,-57.9663988,13z/data=!4m5!3m4!1s0x95a2e9cf7346c109:0x94795c8c803d82fb!8m2!3d-34.9948577!4d-57.9830513?hl=es', 'El Descanso', 1),
(2, 'C. 224 2999-3199, B1900 Abasto, Provincia de Buenos Aires', 'https://www.google.com/maps/place/Finca+El+Rosedal/@-34.9714081,-58.1018397,13z/data=!4m5!3m4!1s0x0:0x6848c315ba3fa7f0!8m2!3d-34.9827001!4d-58.1194139?hl=es', 'El Rosedal', 2),
(6, '23JF+W45 Altos de San Lorenzo, Provincia de Buenos Aires', 'https://www.google.com.ar/maps/place/34%C2%B058\'03.9%22S+57%C2%B055\'37.7%22W/@-34.9677356,-57.9293387,17z/data=!3m1!4b1!4m4!3m3!8m2!3d-34.96774!4d-57.92715', 'Las auroras', 2),
(7, '22JP+69Q Los Hornos, Provincia de Buenos Aires', 'https://www.google.com.ar/maps/place/34%C2%B058\'09.9%22S+57%C2%B057\'50.5%22W/@-34.9684793,-57.961789,15.08z/data=!4m4!3m3!8m2!3d-34.9694167!4d-57.9640278', 'La meseta', 1),
(8, '3W9J+CR4 Melchor Romero, Provincia de Buenos Aires', 'https://www.google.com.ar/maps/place/34%C2%B055\'53.4%22S+58%C2%B004\'04.5%22W/@-34.9314896,-58.0701027,17z/data=!3m1!4b1!4m4!3m3!8m2!3d-34.931494!4d-58.067914', 'Cabañitas', 52),
(52, 'C. 215, Abasto, Provincia de Buenos Aires', 'https://www.google.com/maps/place/LAS+ALICIAS/@-34.9754155,-58.0941763,12.33z/data=!4m5!3m4!1s0x0:0x49f8f926ca037272!8m2!3d-34.9607558!4d-58.131601?hl=es', 'Las Alicias', 52);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Quinta_SEQ`
--

CREATE TABLE `Quinta_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Quinta_SEQ`
--

INSERT INTO `Quinta_SEQ` (`next_val`) VALUES
(201);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Ronda`
--

CREATE TABLE `Ronda` (
  `cod_ronda` bigint NOT NULL,
  `fehca_fin` date DEFAULT NULL,
  `fehca_ini` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Ronda`
--

INSERT INTO `Ronda` (`cod_ronda`, `fehca_fin`, `fehca_ini`) VALUES
(1, '2023-12-12', '2023-03-30'),
(2, '2023-02-10', '2023-02-01'),
(3, '2023-02-22', '2023-02-15'),
(4, '2023-03-10', '2023-03-01'),
(5, '2023-03-22', '2023-03-10'),
(6, '2023-01-10', '2023-01-01'),
(8, '2023-01-27', '2023-01-17');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Ronda_SEQ`
--

CREATE TABLE `Ronda_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Ronda_SEQ`
--

INSERT INTO `Ronda_SEQ` (`next_val`) VALUES
(51);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `User`
--

CREATE TABLE `User` (
  `cod_user` bigint NOT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `direccionU` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `rol` int DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `User`
--

INSERT INTO `User` (`cod_user`, `apellido`, `direccionU`, `email`, `nombre`, `password`, `rol`, `username`) VALUES
(1, 'Perez', 'calle falsa 123', 'unemail@correo.com.gob.ar', 'Juan', 'juan123', 1, 'juanPerez'),
(2, 'Ferrari', 'calle falsa 123', 'unemail@correo.com.gob.ar', 'Giuliana', 'giuli123', 0, 'giulianaFerrari');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `User_SEQ`
--

CREATE TABLE `User_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `User_SEQ`
--

INSERT INTO `User_SEQ` (`next_val`) VALUES
(101);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Verdura`
--

CREATE TABLE `Verdura` (
  `cod_verdura` bigint NOT NULL,
  `archImg` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `mes_siembra` date DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `mes_cosecha` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Verdura`
--

INSERT INTO `Verdura` (`cod_verdura`, `archImg`, `descripcion`, `mes_siembra`, `nombre`, `mes_cosecha`) VALUES
(1, 'string', 'La lechuga es un tipo de hortaliza herbácea conformada por flores amarillentas, fruto seco, con una sola semilla y con hojas grandes, radicales, blandas, de distintas formas, que la gente come en ensaladas o en guisadas.', '2023-04-01', 'Lechuga', '2023-03-01'),
(2, 'string', 'La papa o patata (Solanum tuberosum), con origen en el altiplano sur del Perú, es el tubérculo más consumido en el mundo y se encuentra entre los diez principales cultivos de la humanidad.', '2023-02-01', 'Papa', '2023-01-01'),
(3, '-', 'El tomate​ es el fruto de la planta Solanum lycopersicum, el cual tiene importancia culinaria y es utilizado como fruta.', '2023-04-12', 'Tomate', '2023-04-28'),
(4, '-', 'El Kale es un alimento con alta concentración en vitaminas y minerales. Contiene más calcio que la leche.', '2023-04-29', 'Kale', '2023-05-11'),
(5, '-', 'La zanahoria es la forma domesticada de la zanahoria silvestre, especie de la familia de las umbelíferas, también denominadas apiáceas, y considerada la más importante y de mayor consumo dentro de esta familia.', '2023-06-02', 'Zanahoria', '2023-07-13'),
(6, '-', 'La cebolla es la especie más cultivada del género Allium, el cual contiene varias especies que se denominan «cebollas» y que se cultivan como alimento.', '2023-04-15', 'Cebolla', '2023-07-14'),
(7, '-', 'El repollo es una planta comestible de la familia de las Brasicáceas, y una herbácea bienal, cultivada como anual, cuyas hojas lisas forman un característico cogollo compacto.', '2023-03-14', 'Repollo', '2023-06-02'),
(8, '-', 'El coliflor es una planta anual que se reproduce por semillas y que encuentra en su mejor momento entre los meses de septiembre y enero en el hemisferio norte, aunque se puede disponer de ella durante todo el año.', '2023-06-09', 'Coliflor', '2023-04-24'),
(102, 'string', 'La batata, boniato, patata dulce o camote es un tubérculo, es decir un engrosamiento de las raíces. Originario de Suramérica, concretamente de la zona de Perú, ahora se cultiva por todo el mundo.', '2023-12-01', 'Batata', '2023-10-01');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `VERDURA_BOLSON`
--

CREATE TABLE `VERDURA_BOLSON` (
  `cod_bolson` bigint NOT NULL,
  `cod_verdura` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `VERDURA_BOLSON`
--

INSERT INTO `VERDURA_BOLSON` (`cod_bolson`, `cod_verdura`) VALUES
(1, 1),
(1, 102),
(2, 2),
(2, 102),
(52, 2),
(52, 1),
(52, 102);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Verdura_SEQ`
--

CREATE TABLE `Verdura_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Verdura_SEQ`
--

INSERT INTO `Verdura_SEQ` (`next_val`) VALUES
(201);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Visita`
--

CREATE TABLE `Visita` (
  `cod_visita` bigint NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `fecha_visita` date DEFAULT NULL,
  `cod_quinta` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Visita`
--

INSERT INTO `Visita` (`cod_visita`, `descripcion`, `fecha_visita`, `cod_quinta`) VALUES
(1, 'Visita en la Quinta El Descanso', '2023-03-10', 1),
(2, 'Visita en la Quinta El Rosedal', '2023-03-10', 2),
(52, 'Visita en la Quinta Las Alicias', '2023-03-10', 52);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Visita_SEQ`
--

CREATE TABLE `Visita_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Visita_SEQ`
--

INSERT INTO `Visita_SEQ` (`next_val`) VALUES
(151);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `VISITA_TECNICO`
--

CREATE TABLE `VISITA_TECNICO` (
  `cod_visita` bigint NOT NULL,
  `cod_user` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `VISITA_TECNICO`
--

INSERT INTO `VISITA_TECNICO` (`cod_visita`, `cod_user`) VALUES
(1, 1),
(2, 1),
(52, 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `Bolson`
--
ALTER TABLE `Bolson`
  ADD PRIMARY KEY (`cod_bolson`),
  ADD KEY `FK5q105fbkojhc68u7j67d1bo03` (`cod_fp`),
  ADD KEY `FKfhrfklucnhw69akfa4kgn2ji` (`cod_ronda`);

--
-- Indices de la tabla `FamiliaProductora`
--
ALTER TABLE `FamiliaProductora`
  ADD PRIMARY KEY (`cod_fp`);

--
-- Indices de la tabla `Parcela`
--
ALTER TABLE `Parcela`
  ADD PRIMARY KEY (`cod_parcela`),
  ADD KEY `FK348o5ao8lcpfd2m86kuhik0i0` (`verdura_cod_verdura`),
  ADD KEY `FKtdrr67fex6y5ybl4trogn9g8x` (`cod_visita`);

--
-- Indices de la tabla `Quinta`
--
ALTER TABLE `Quinta`
  ADD PRIMARY KEY (`cod_quinta`),
  ADD KEY `FKdnwmkpx668mktdo66bmk5kb2q` (`cod_fp`);

--
-- Indices de la tabla `Ronda`
--
ALTER TABLE `Ronda`
  ADD PRIMARY KEY (`cod_ronda`);

--
-- Indices de la tabla `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`cod_user`);

--
-- Indices de la tabla `Verdura`
--
ALTER TABLE `Verdura`
  ADD PRIMARY KEY (`cod_verdura`);

--
-- Indices de la tabla `VERDURA_BOLSON`
--
ALTER TABLE `VERDURA_BOLSON`
  ADD KEY `FKhyncf0etsi811vsk7ef5a3gf8` (`cod_verdura`),
  ADD KEY `FKsb29y45td3i4vq6616l41ngr8` (`cod_bolson`);

--
-- Indices de la tabla `Visita`
--
ALTER TABLE `Visita`
  ADD PRIMARY KEY (`cod_visita`),
  ADD KEY `FKsmvivmcy9axw7rmw3egccw7e8` (`cod_quinta`);

--
-- Indices de la tabla `VISITA_TECNICO`
--
ALTER TABLE `VISITA_TECNICO`
  ADD KEY `FKk1idvwgd7l3s33knir30m3l1d` (`cod_user`),
  ADD KEY `FKbde9fg1dtak3wbsucx3hd19bf` (`cod_visita`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `Bolson`
--
ALTER TABLE `Bolson`
  ADD CONSTRAINT `FK5q105fbkojhc68u7j67d1bo03` FOREIGN KEY (`cod_fp`) REFERENCES `FamiliaProductora` (`cod_fp`),
  ADD CONSTRAINT `FKfhrfklucnhw69akfa4kgn2ji` FOREIGN KEY (`cod_ronda`) REFERENCES `Ronda` (`cod_ronda`);

--
-- Filtros para la tabla `Parcela`
--
ALTER TABLE `Parcela`
  ADD CONSTRAINT `FK348o5ao8lcpfd2m86kuhik0i0` FOREIGN KEY (`verdura_cod_verdura`) REFERENCES `Verdura` (`cod_verdura`),
  ADD CONSTRAINT `FKtdrr67fex6y5ybl4trogn9g8x` FOREIGN KEY (`cod_visita`) REFERENCES `Visita` (`cod_visita`);

--
-- Filtros para la tabla `Quinta`
--
ALTER TABLE `Quinta`
  ADD CONSTRAINT `FKdnwmkpx668mktdo66bmk5kb2q` FOREIGN KEY (`cod_fp`) REFERENCES `FamiliaProductora` (`cod_fp`);

--
-- Filtros para la tabla `VERDURA_BOLSON`
--
ALTER TABLE `VERDURA_BOLSON`
  ADD CONSTRAINT `FKhyncf0etsi811vsk7ef5a3gf8` FOREIGN KEY (`cod_verdura`) REFERENCES `Verdura` (`cod_verdura`),
  ADD CONSTRAINT `FKsb29y45td3i4vq6616l41ngr8` FOREIGN KEY (`cod_bolson`) REFERENCES `Bolson` (`cod_bolson`);

--
-- Filtros para la tabla `Visita`
--
ALTER TABLE `Visita`
  ADD CONSTRAINT `FKsmvivmcy9axw7rmw3egccw7e8` FOREIGN KEY (`cod_quinta`) REFERENCES `Quinta` (`cod_quinta`);

--
-- Filtros para la tabla `VISITA_TECNICO`
--
ALTER TABLE `VISITA_TECNICO`
  ADD CONSTRAINT `FKbde9fg1dtak3wbsucx3hd19bf` FOREIGN KEY (`cod_visita`) REFERENCES `Visita` (`cod_visita`),
  ADD CONSTRAINT `FKk1idvwgd7l3s33knir30m3l1d` FOREIGN KEY (`cod_user`) REFERENCES `User` (`cod_user`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
