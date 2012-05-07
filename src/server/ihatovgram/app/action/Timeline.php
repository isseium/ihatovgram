<?php
class Ihatovgram_Form_Timeline extends Ihatovgram_ActionForm
{
    protected $form = array(
        'fb_token' => array(
            'name' => 'Facebook Token',
            'type' => VAR_TYPE_STRING
        )
    );
}

class Ihatovgram_Action_Timeline extends Ihatovgram_ActionClass
{
    public function prepare()
    {
        return null;
    }

    public function perform()
    {
        $db =& $this->backend->getDB();
        $fb_token = $this->af->get('fb_token');
        if($fb_token != '') {
            $sql = "SELECT id FROM user WHERE fb_token = '$fb_token'";
            $result =& $db->query($sql);
            $user = $result->fetchRow(DB_FETCHMODE_ASSOC);
            if(isset($user)) {
                $sql = sprintf("select user.id as user_id, image.id as image_id, user.user_name as user_name, image.comment, image.create_date from user INNER JOIN image ON user.id = image.user_id where user.id = %s order by image.create_date desc limit 5", $user['id']);
            } else {
                return null;
            }
        } else {
            $sql = "select user.id as user_id, image.id as image_id, user.user_name as user_name, image.comment, image.create_date from user INNER JOIN image ON user.id = image.user_id order by image.create_date desc limit 5";
        }
        $result =& $db->query($sql);
        while ($row = $result->fetchRow(DB_FETCHMODE_ASSOC)) {
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
    }
}
