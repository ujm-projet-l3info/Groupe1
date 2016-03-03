-- phpMyAdmin SQL Dump
-- version 3.3.7deb11
-- http://www.phpmyadmin.net
--
-- Serveur: localhost
-- Généré le : Mar 01 Mars 2016 à 19:34
-- Version du serveur: 5.1.73
-- Version de PHP: 5.3.3-7+squeeze29

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `parkme`
--

-- --------------------------------------------------------

--
-- Structure de la table `Membre_parking`
--
-- Création: Mar 01 Mars 2016 à 19:27
--

DROP TABLE IF EXISTS `Membre_parking`;
CREATE TABLE IF NOT EXISTS `Membre_parking` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `pseudo` varchar(50) NOT NULL,
  `pass` varchar(50) NOT NULL,
  `mail` varchar(100) NOT NULL,
  `note` int(5) NOT NULL,
  `commentaire` varchar(10000) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pseudo` (`pseudo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='tables d''inscription pour les utilisations de l''application ' AUTO_INCREMENT=1 ;

--
-- Contenu de la table `Membre_parking`
--

