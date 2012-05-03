<?php
require_once("MySQL.php");
$db = new MySQL();
if (isset($_GET['fb_token'])) {
    $user_id = $db->getIDbyToken($_GET['fb_token']);
    $all = $db->getUserTimeline($user_id);;
} else {
    $all = $db->getTimeline();
}
while ($row = mysql_fetch_assoc($all)) {
    $image['user_id'] = $row['user_id'];
    $image['image_id'] =$row['image_id'];
    $image['user_name'] = $row['user_name'];
    $image['comment'] = $row['comment'];
    $image['create_date'] = $row['create_date'];
    $image['url'] = "http://s2k1ta98.org/server/img/" . $row['image_id'] . ".jpg";
    $timeline[] = $image;
}
$data['timeline'] = $timeline;
echo json_encode($data);
