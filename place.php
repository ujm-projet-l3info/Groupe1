<?php
    mysql_connect("assandi-baco.top","parkme","parkme");
    mysql_select_db("parkme");

$q=mysql_query("SELECT * FROM places");
        while($e=mysql_fetch_assoc($q))
        $output[]=$e;

        print(json_encode($output));

        mysql_close();
        ?>