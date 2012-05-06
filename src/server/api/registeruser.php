<?php
    require_once("MySQL.php");
    $db = new MySQL();

    $result = $db->createUser($_POST["fb_token"], $_POST["comment"]);

    $users = $db->findAllUser();
    while ($row = mysql_fetch_assoc($users)) {
        echo "fb_token: " . $row['fb_token'] . "<br>";
        echo "name: " . $row['user_name'] . "<br>";
    }
