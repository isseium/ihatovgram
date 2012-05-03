<?php
    $link = mysql_connect('localhost', 'root', 'root');
    if (!$link) {
        echo "cant connect";
    }

    $db_selected = mysql_select_db('ihatovgram', $link);
    if (!$db_selected) {
        echo "cant select";
    }

    $sql = sprintf("INSERT INTO user(fb_token, user_name) VALUES('%s', '%s)", $_POST['fb_token'], $_POST['user_name']);

    $result_flag = mysql_query($sql);

    $sql = "SELECT * FROM user";

    $result = mysql_query($sql);

    while ($row = mysql_fetch_assoc($result)) {
        echo "fb_token: " . $row['fb_token'] . "<br>";
        echo "name: " . $row['user_name'] . "<br>";
    }

    mysql_close($link);

