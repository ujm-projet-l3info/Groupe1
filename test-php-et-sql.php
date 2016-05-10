<?php

/** Test de connexion PHP <-> (My|Pg)SQL
 * Licence : CeCILL-B, http://www.cecill.info
 * Basé sur le script proposé par Oliver Baty <http://ardamis.com/2008/05/26/a-php-script-for-testing-a-mysql-database-connection/>
 * Modifié par Al <les.pages.perso.chez(chez)free.fr> pour l'adpatation à l'infrastructure des PP de Free
 * Styles CSS basés sur le projet Better Web Readability Project CSS Library <http://code.google.com/p/better-web-readability-project/>
 * */
 
header('Cache-Control:private, no-store, must-revalidate'); header('Content-Language: fr'); header('Vary: Accept-Encoding'); header('X-Frame-Options: SAMEORIGIN'); header('X-Robots-Tag: noindex,nofollow,noarchive'); header('X-XSS-Protection: 1; mode=block'); header('X-UA-Compatible: IE=edge, chrome=1'); header('Content-Type: text/html; charset=utf-8'); ob_start('ob_gzhandler');
?>
<!DOCTYPE html><html lang="fr">
<head><meta charset="utf-8"><title>Test de fonctionnement PHP et SQL</title><!-- Début du controle et suppression des styles de PHP Info --><style type="text/css">#phpinfo {}#phpinfo pre {}#phpinfo a:link {}#phpinfo a:hover {}#phpinfo table {}#phpinfo .center {}#phpinfo .center table {}#phpinfo .center th {}#phpinfo td, th {}#phpinfo h1 {}#phpinfo h2 {}#phpinfo .p {}#phpinfo .e {}#phpinfo .h {}#phpinfo .v {}#phpinfo .vr {}#phpinfo img {}#phpinfo hr {}</style><!-- Fin du controle et suppression des styles de PHP Info --><style media="all" type="text/css">html, body, div, span, object, iframe, h1, h2, h3, h4, h5, h6, p, blockquote, pre, a, abbr, acronym, address, code, del, dfn, em, img, q, dl, dt, dd, ol, ul, li, fieldset, form, label, legend, table, caption, tbody, tfoot, thead, tr, th, td { margin: 0; padding: 0; border: 0; font-weight: inherit; font-style: inherit; font-size: 100%; font-family: inherit; vertical-align: baseline; } table { border-collapse: separate; border-spacing: 0; } caption, th, td { text-align: left; font-weight: normal; } table, td, th { vertical-align: middle; } blockquote:before, blockquote:after, q:before, q:after { content: ""; } blockquote, q { quotes: '' ''; } a img { border: none; } body { margin: 10px; } body { font:1em/1.625em 'lucida grande','lucida sans unicode', sans-serif; background-color:#FFF; font-size-adjust:none; font-style:normal; font-variant:normal; font-weight:normal; } p { padding:0 0 0.8125em 0; color:#111; font-weight:300;} /* p + p { text-indent:1.625em;} */ p.first:first-letter{ float:left;font-family: baskerville,'palatino linotype',serif;font-size:3em;font-weight:700;line-height:1em;margin-bottom:-0.2em;padding:0.2em 0.1em 0 0; } p img { float: left; margin: 0.5em 0.8125em 0.8125em 0; padding: 0; } p img.right { float: right; margin: 0.5em 0 0.8125em 0.8125em } h1,h2{ font-weight:normal; color: #333; font-family:Georgia, serif; } h3,h4,h5,h6 { font-weight: normal; color: #333; font-family:Georgia, serif; } h1 { font-size: 2.125em; margin-bottom: 0.765em; } h2 { font-size: 1.9em; margin-bottom: 0.855em; } h3 { font-size: 1.7em; margin-bottom: 0.956em; } h4 { font-size: 1.4em; margin-bottom: 1.161em; } h5,h6 { font-size: 1.313em; margin-bottom: 1.238em; } input { font:1em/1.625em 'lucida grande','lucida sans unicode', sans-serif; border:none; color: #EEE; margin-bottom: 0.765em; text-decoration: none; padding:0.8125em;background: #333;} ul{list-style-position:outside;} li ul, li ol { margin:0 1.625em; } ul, ol { margin: 0 0 1.625em 0; margin-left:1.625em;} dl { margin: 0 0 1.625em 0; } dl dt { font-weight: bold; } dl dd { margin-left: 1.625em; } a { color:#005AF2; text-decoration:none; } a:hover { text-decoration: underline; } table { margin-bottom:1.625em; border-collapse: collapse; } th { font-weight:bold; } tr,th,td { margin:0; padding:0 1.625em 0 1em; height:26px; } tfoot { font-style: italic; } caption { text-align:center; font-family:Georgia, serif; } abbr, acronym { border-bottom:1px dotted #000; } address { margin-top:1.625em; font-style: italic; } del {color:#000;} blockquote { padding:1em 1em 1.625em 1em; font-family:georgia,serif;font-style: italic; } blockquote:before { content:'\201C';font-size:3em;margin-left:-.625em; font-family:georgia,serif;color:#aaa;line-height:0;}/* From Tripoli */ blockquote > p {padding:0; margin:0; } strong { font-weight: bold; } em, dfn { font-style: italic; } dfn { font-weight: bold; } pre, code { margin: 1.625em 0; white-space:pre;white-space:pre-wrap;word-wrap:break-word } pre, code, tt { font-family: monospace; line-height: 1.5; white-space:pre;white-space:pre-wrap;word-wrap:break-word} tt { display: block; margin: 1.625em 0; } hr { margin-bottom:1.625em; } .oldbook { font-family:'Warnock Pro','Goudy Old Style','Book Antiqua','Palatino',Georgia,serif; } .note { font-family:Georgia, 'Times New Roman', Times, serif; font-style:italic; font-size:0.9em; margin:0.1em; color:#333; } .mono { font-family:'Courier New', Courier, monospace; white-space:pre;white-space:pre-wrap;word-wrap:break-word}</style><!--[if lt IE 9]><script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]--></head>
<body style="margin-left:auto;margin-right:auto;max-width:70%">
<h1>Test de fonctionnement PHP et SQL</h1>
<p>Grâce à ce script, il vous est possible de vérifier le bon fonctionnement de PHP et des bases SQL depuis votre compte. Si des erreurs sont affichées, vérifiez que vos mots de passe SQL ne sont pas plus long que 10 caractères (vous pouvez les modifier via l'option correspondante dans <a href="https://subscribe.free.fr/login/">votre interface de gestion</a> ou depuis <a href="http://sql.free.fr/phpPgAdmin/">phpPgAdmin</a> si vous utilisez les deux bases MySQL et PgSQL sur votre compte, qu'ils ne comportent pas de caractères accentués et que le nom de vos bases est correctement renseignés.</p>
<p>Si vos bases SQL semble indisponibles ou crashée, rendez-vous sur les interfaces de gestion de bases SQL de Free&nbsp;: <a href="http://sql.free.fr/phpMyAdmin/">phpMyAdmin</a> et <a href="http://sql.free.fr/phpPgAdmin/">phpPgAdmin</a> pour en vérifier l'état et rapporter un éventuel problème sur <a href="http://les.pages.perso.chez.free.fr/index.php/post/2008/09/25/le-groupe-usenet-pfspp">le forum Usenet des Pages Perso de Free</a>.</p>
<p>N'oubliez pas de supprimer ce script de votre espace Web une fois les opérations de contrôle terminées.</p>
<hr/>
<h2>Test de PHP</h2>
<p><strong>L'exécuteur PHP semble fonctionner correctement.</strong></p>
<p>PHP Info disponible en bas de page.</p>
<pre style="border: 1px dotted #666666;padding:10px;">
<strong>Signature du serveur&nbsp;:</strong> <?php echo $_SERVER['SERVER_SIGNATURE']; ?>
<strong>Adresse IP du serveur&nbsp;:</strong> <?php echo $_SERVER['SERVER_ADDR']; ?><br/>
<strong>Date et heure&nbsp;:</strong> <?php echo date('l jS F Y H:i:s'); ?><br/>
<strong>Version de PHP&nbsp;:</strong> <?php echo phpversion(); ?><br/>
<strong>Votre adresse IP&nbsp;:</strong> <?php echo $_SERVER['REMOTE_ADDR']; ?>
</pre>
<hr/>
<h2>Test de connexion à MySQL</h2>
<p><strong>Cette section permet de vérifier la connexion entre vos scripts PHP et les serveurs MySQL de Free. Les erreurs mentionnées ici ne vous concernent que si votre site repose sur une base MySQL. Dans le cas contraire, vous pouvez ignorer les erreurs de cette section.</strong></p>
<?php 
// Précisez les valeurs correspondante à votre base MySQL
 
    $hostname = "assandi-baco.top";
    $username = "parkme";
    $password = "parkme";
    $database = "parkme"; // Si utilisation d'un point dans le login, le remplacer par "_" http://mon.login.free.fr -> mon_login
 
    $link = mysql_connect("$hostname", "$username", "$password");
        if (!$link) {
            echo "<p><strong>Impossible de se connecter au serveur MySQL " . $hostname . ".</strong></p>";
            echo "<pre style='border: 1px dotted #666666;padding:10px;'><code>";
            echo mysql_error();
            echo "</code></pre>\r\n";
        }else{
            echo "<p>Connexion réussie au serveur MySQL </strong>" . $hostname . "</strong> :</p>";
            echo "<pre style='border: 1px dotted #666666;padding:10px;'><code>";
            printf("Version du client MySQL : %s\r\n", mysql_get_client_info());
            printf("Hôte MySQL : %s\r\n", mysql_get_host_info());
            printf("Version du serveur MySQL : %s\r\n", mysql_get_server_info());
            printf("Version du protocole : %s\r\n", mysql_get_proto_info());
            echo "</code></pre>\r\n";
        }
    if ($database) {
    $dbcheck = mysql_select_db("$database");
        if (!$dbcheck) {
            echo "<pre style='border: 1px dotted #666666;padding:10px;'><code>";
                echo mysql_error();
            echo "</code></pre>\r\n";
        }else{
            echo "<p>Connexion réussie à la base de données MySQL <strong>" . $database . "</strong>.</p>";
            
            $sql = "SHOW TABLES FROM `$database`";
            $result = mysql_query($sql);
            if (mysql_num_rows($result) > 0) {
                echo "<p>Tables actuellement dans la base MySQL :</p>\r\n";
                echo "<pre style='border: 1px dotted #666666;padding:10px;'><code>";
                while ($row = mysql_fetch_row($result)) {
                    echo "{$row[0]}\r\n";
                }
                echo "</code></pre>\r\n";
            } else {
                echo "<p>La base MySQL <strong>" . $database . "</strong> ne contient aucune tables.</p>";
            echo "<pre style='border: 1px dotted #666666;padding:10px;'><code>";
                echo mysql_error();
            echo "</code></pre>\r\n";
}
mysql_close($link);
        }
    }
?>
<hr/>
<h2>Test de connexion à PgSQL</h2>
<p><strong>Cette section permet de vérifier la connexion entre vos scripts PHP et les serveurs PostgreSQL (PgSQL) de Free. Les erreurs mentionnées ici ne vous concernent que si votre site repose sur une base PostgreSQL (PgSQL). Dans le cas contraire, vous pouvez ignorer les erreurs de cette section.</strong></p>
<?php 
// Précisez les valeurs correspondante à votre base de données PostgreSQL
 
    $pg_hostname = "sql.free.fr";
    $pg_username = "monlogin";
    $pg_password = "mot_de_passe_sql";
    $pg_database = "monlogin";
    
    $pg_link = pg_connect("host=$pg_hostname port=5432 user=$pg_username password=$pg_password dbname=$pg_database");
        if (!$pg_link) {
            echo "<p><strong>Impossible de se connecter au serveur PostgreSQL " . $pg_hostname . ".</strong></p>";
            echo "<pre style='border: 1px dotted #666666;padding:10px;'><code>";
            echo pg_last_error();
            echo "</code></pre>\r\n";
        }else{
            echo "<p>Connexion réussie au serveur PostgreSQL <strong>" . $pg_hostname . "</strong>:</p>";
            echo "<pre style='border: 1px dotted #666666;padding:10px;'><code>";
            echo "Version du serveur PostgreSQL : ", pg_parameter_status(server_version);
            echo "</code></pre>\r\n";
            echo "<p>Connexion réussie à la base de données PostgreSQL <strong>" . $pg_database . "</strong>.</p>";
            
            $sql = "SELECT pg_tables.tablename,columns.column_name FROM pg_tables,information_schema.columns columns WHERE pg_tables.tablename=columns.table_name AND pg_tables.schemaname='public'  ORDER by pg_tables.tablename;";
            $result = pg_query($pg_link, "$sql");
            if ($result > 0) {
                echo "<p>Tables actuellement dans la base PostgreSQL :</p>\r\n";
                echo "<pre style='border: 1px dotted #666666;padding:10px;'><code>";
                while ($row = pg_fetch_row($result)) {
                    echo "{$row[0]}\r\n";
                }
                echo "</code></pre>\r\n";
            } else { 
                echo "<p>La base " . $pg_database . " ne contient aucune tables.</p>";
            echo "<pre style='border: 1px dotted #666666;padding:10px;'><code>";
                echo pg_last_error();
            echo "</code></pre>\r\n";
}
pg_close($pg_link);
}
?>
<hr/>
<h2>PHP Info</h2>
<!-- Début du controle et suppression des styles de PHP Info -->
<div id="phpinfo" style="border: 1px dotted #666666;padding:10px;font-size:80%;">
<?php
ob_start();
phpinfo();
$pinfo = ob_get_contents();
ob_end_clean();
 
$pinfo = preg_replace( '%^.*<body>(.*)</body>.*$%ms','$1',$pinfo);
echo $pinfo;
?>
</div>
<!-- Fin du controle et suppression des styles de PHP Info -->
</body></html><?php ob_end_flush(); ?>
