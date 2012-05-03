<?php
    var_dump($_POST["image"]);
    var_dump($_POST["fb_token"]);
    var_dump($_POST["comment"]);

    $link = mysql_connect('localhost', 'root', 'root');
    if (!$link) {
        echo "cant connect";
    }

    $db_selected = mysql_select_db('ihatovgram', $link);
    if (!$db_selected) {
        echo "cant select";
    }

    $sql = "SELECT MAX(id) as max FROM image";
    $result = mysql_query($sql);
    $row = mysql_fetch_assoc($result);

    $filename = ($row['max'] + 1) . ".jpg";;
    $upload_dir = '/var/www/server/img';
    echo $_FILES['image']['name'];
    echo "," . $_FILES['image']['tmp_name'];
    if (move_uploaded_file($_FILES['image']['tmp_name'], "$upload_dir/$filename")) {
        echo "success";
    } else {
        echo "cant upload";
    }


    $sql = sprintf("INSERT INTO image(fb_token, comment) VALUES('%s', '%s')", $_POST['fb_token'], $_POST['comment']);

    $result_flag = mysql_query($sql);

    mysql_close($link);
