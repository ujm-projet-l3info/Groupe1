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

CREATE TABLE IF NOT EXISTS auth_table (
  user_id int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  username varchar(20) NOT NULL DEFAULT '',
  password varchar(32) NOT NULL DEFAULT '',
  PRIMARY KEY  (user_id),
  UNIQUE KEY username (username)
) TYPE=InnoDB ;

-- 
-- Insertion Table `auth_table`
-- 

INSERT INTO auth_table (username, password) VALUES ('test', MD5('pass'));
--
-- Contenu de la table `Membre_parking`
--

