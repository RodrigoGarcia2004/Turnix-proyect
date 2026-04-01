-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 15-03-2026 a las 21:07:54
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `proyecto_sge`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `categoria` varchar(50) NOT NULL,
  `stock` int(11) NOT NULL,
  `estado` varchar(20) NOT NULL,
  `fecha_creacion` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id`, `nombre`, `descripcion`, `precio`, `categoria`, `stock`, `estado`, `fecha_creacion`) VALUES
(2, 'macarrones', 'con tomate', 1.00, 'deporte', 1, 'disponible', '2026-03-14 22:18:22'),
(3, 'macarrones', '222', 2.00, 'juguetes', 2, 'agotado', '2026-03-14 22:18:33'),
(4, 'macarrones', 'con tomate', 1.00, 'deporte', 1, 'disponible', '2026-03-14 22:18:40'),
(5, 'aaa', 'aaa', 2.00, 'juguetes', 2, 'disponible', '2026-03-14 22:48:56'),
(6, 'asafa', 'safasfasfas', 2.00, 'deporte', 222, 'disponible', '2026-03-14 22:54:11'),
(7, 'sfsafas', 'fasfa', 99999999.99, 'juguetes', 12121, 'disponible', '2026-03-15 13:00:37');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `telefono` varchar(20) NOT NULL,
  `genero` varchar(20) NOT NULL,
  `rol` varchar(20) NOT NULL,
  `fecha_registro` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `username`, `email`, `password`, `telefono`, `genero`, `rol`, `fecha_registro`) VALUES
(5, 'prueba', 'prueba@gmail.com', '$2y$10$ie4ed3oFTJPilpolAI5pouk3W91IhnLWToczjtyOd/eZJY26EkID2', '65656565', 'hombre', 'usuario', '2026-03-12'),
(6, 'prueba', 'darkthesitos@gmail.com', '$2y$10$uE//i6NYYBe49x/q..Tra.wZeOgHR8oKqVVfyDnIvTzDSlDonE/dy', '65656565', 'hombre', 'admin', '2026-03-12'),
(7, 'prueba', 'darkthesitos@gmail.com', '$2y$10$.5K8ePOGuydYUBp6EIN.h.3wMzm2qXnV419esISSVlbgMTKBlDzOO', '65656565', 'hombre', 'admin', '2026-03-12'),
(8, 'prueba', 'feggfe@gmail.com', '$2y$10$4GmmFrcN6AtyIr/i8SBs5e.DHY4kpz33OK/CwAiFzC8si5BMhW48m', '65656565', 'hombre', 'usuario', '2026-03-12'),
(9, 'prueba', 'darkthesitos@gmail.com', '$2y$10$3hRT5BtznAY5tvCLDOOdBea8zSKh6A2YluXuWOLAitiCQkash0kLG', '65656565', 'hombre', 'usuario', '2026-03-12'),
(10, 'prueba', 'darkthesitos@gmail.com', '$2y$10$BHOOi.WSus.ubXUk3nqzQuSu3EO5fInZblstU27ACmDzHs5FG50La', '65656565', 'hombre', 'usuario', '2026-03-12'),
(11, 'rgarciaheredia', 'darkthesitos@gmail.com', '$2y$10$x0DD31pH1X3Yt4MG6OqewO6bRy/wG6qOQDk80g/nsGfEOzTsR0WAO', '65656565', 'hombre', 'admin', '2026-03-14'),
(12, 'rgarciaheredia2', 'darkthesitos2@gmail.com', '$2y$10$wia6/bntfx8QaAnJ4ru6DuebpkXH51RcxpKGk/RP9ld34UoemqMbq', '2222222222', 'mujer', 'admin', '2026-03-14');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
