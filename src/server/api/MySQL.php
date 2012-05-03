<?php
class MySQL
{
    var $db;

    function __construct()
    {
        $this->connect();
        $this->selectdb();
    }

    function __destruct()
    {
        mysql_close($this->link);
    } 


    function connect()
    {
        $this->link = mysql_connect('localhost', 'root', 'root');
        if (!$this->link) {
            echo "cant connect";
        }
    }

    function selectdb()
    {
        $this->selected = mysql_select_db('ihatovgram', $this->link);
        if (!$this->selected) {
            echo "cant select";
        }
    }

    function createUser($fb_token, $user_name)
    {
        $sql = sprintf("INSERT INTO user(fb_token, user_name) VALUES('%s', '%s)", $fb_token, $user_name);
        $result = mysql_query($sql);
        if (!$result) {
            return false;
        } else {
            return true;
        }
    }

    function findAllUser()
    {
        $sql = sprintf("SELECT * FROM user");
        return mysql_query($sql);
    }

    function getIDbyToken($fb_token)
    {
        $sql = sprintf("SELECT id FROM user WHERE fb_token = '%s'", $fb_token);
        $result = mysql_query($sql);
        $row = mysql_fetch_assoc($result);
        return $row['id'];
    }

    function getMaxImageID()
    {
        $sql = "SELECT MAX(id) as max FROM image";
        $result = mysql_query($sql);
        $row = mysql_fetch_assoc($result);
        return $row['max'];
    }

    function insertImage($user_id, $comment)
    {
        $sql = sprintf("INSERT INTO image(user_id, comment, create_date) VALUES('%d', '%s', NOW())", $user_id, $comment);
        $result_flag = mysql_query($sql);
        if (!$result_flag) {
            return false;
        } else {
            return true;
        }
    }

    function getTimeline()
    {
        $sql = "select user.id as user_id, image.id as image_id, user.user_name as user_name, image.comment, image.create_date from user INNER JOIN image ON user.id = image.user_id order by image.create_date desc limit 5";
        return mysql_query($sql);

    }

    function getUserTimeline($user_id)
    {
        $sql = sprintf("select user.id as user_id, image.id as image_id, user.user_name as user_name, image.comment, image.create_date from user INNER JOIN image ON user.id = image.user_id where user.id = %s order by image.create_date desc limit 5", $user_id);
        return mysql_query($sql);
    }
}
