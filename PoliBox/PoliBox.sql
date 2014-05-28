-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generato il: Mag 28, 2014 alle 09:50
-- Versione del server: 5.5.27
-- Versione PHP: 5.4.7

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `PoliBox`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `Condivisioni`
--

CREATE TABLE IF NOT EXISTS `Condivisioni` (
  `c_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `owner_id` bigint(20) NOT NULL,
  `u_id` bigint(20) NOT NULL,
  `dir_path` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `state` int(11) NOT NULL,
  `read_only` tinyint(1) NOT NULL,
  PRIMARY KEY (`c_id`),
  KEY `fk01_condivisioni_utenti` (`owner_id`),
  KEY `fk02_condivisioni_utenti` (`u_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Struttura della tabella `Dispositivi`
--

CREATE TABLE IF NOT EXISTS `Dispositivi` (
  `d_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `u_id` bigint(20) NOT NULL,
  `last_sync` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`d_id`),
  KEY `fk01_dispositivi_utenti` (`u_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Struttura della tabella `Utenti`
--

CREATE TABLE IF NOT EXISTS `Utenti` (
  `u_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `cognome` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `home_dir` varchar(256) NOT NULL,
  PRIMARY KEY (`u_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dump dei dati per la tabella `Utenti`
--

INSERT INTO `Utenti` (`u_id`, `nome`, `cognome`, `email`, `password`, `home_dir`) VALUES
(1, 'Andrea', 'Rigoni', 'andrear2.ar@gmail.com', 'andrea', '');

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `Condivisioni`
--
ALTER TABLE `Condivisioni`
  ADD CONSTRAINT `fk02_Condivisioni_Utenti` FOREIGN KEY (`u_id`) REFERENCES `Utenti` (`u_id`),
  ADD CONSTRAINT `fk01_Condivisioni_Utenti` FOREIGN KEY (`owner_id`) REFERENCES `Utenti` (`u_id`);

--
-- Limiti per la tabella `Dispositivi`
--
ALTER TABLE `Dispositivi`
  ADD CONSTRAINT `fk01_Dispositivi_Utenti` FOREIGN KEY (`u_id`) REFERENCES `Utenti` (`u_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
