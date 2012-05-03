<?php
    require_once("MySQL.php");
    $db = new MySQL();

    $max_id = $db->getMaxImageId();

    $filename = ($max_id + 1) . ".jpg";
    $upload_dir = '/var/www/server/img';
    echo "," . $_FILES['image']['tmp_name'];
    if (move_uploaded_file($_FILES['image']['tmp_name'], "$upload_dir/$filename")) {
        echo "success";
    } else {
        echo "cant upload";
    }
    error_log($_POST['fb_token']);
    $user_id = $db->getIDbyToken($_POST['fb_token']);
    $result = $db->insertImage($user_id, $_POST['comment']);
