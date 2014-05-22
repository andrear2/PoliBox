-- phpMyAdmin SQL Dump
-- version 4.2.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Mag 19, 2014 alle 13:39
-- Versione del server: 5.5.27
-- PHP Version: 5.4.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
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
-- Struttura della tabella `Utenti`
--

CREATE TABLE IF NOT EXISTS `Utenti` (
`id` bigint(20) NOT NULL,
  `nome` varchar(20) NOT NULL,
  `cognome` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(20) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dump dei dati per la tabella `Utenti`
--

INSERT INTO `Utenti` (`id`, `nome`, `cognome`, `email`, `password`) VALUES
(1, 'Andrea', 'Rigoni', 'andrear2.ar@gmail.com', 'andrea');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Utenti`
--
ALTER TABLE `Utenti`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Utenti`
--
ALTER TABLE `Utenti`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
