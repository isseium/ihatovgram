<?php
class Ihatovgram_Form_Createuser extends Ihatovgram_ActionForm
{
    protected $form = array(
        'type' => array(
            'type' => VAR_TYPE_STRING,
            'form_type' => FORM_TYPE_HIDDEN,
            'value' => 'createuser',
            'required' => true,
        ),
        'fb_token' => array(
            'type' => VAR_TYPE_STRING,
            'form_type' => FORM_TYPE_TEXT,
            'name' => 'Facebook API TOKEN',
            'max' => 100,
            'required' => true,
        ),
        'user_name' => array(
            'type' => VAR_TYPE_STRING,
            'form_type' => FORM_TYPE_TEXT,
            'name' => 'ユーザ名',
            'max' => 50,
            'required' => true,
        ),
    );
}

class Ihatovgram_Action_Createuser extends Ihatovgram_ActionClass
{
    public function prepare()
    {
        if ($this->af->get('type') != "createuser") {
            return 'createuser';
        }
        return null;
    }

    public function perform()
    {
        $fb_token = $this->af->get('fb_token');
        $user_name = $this->af->get('user_name');
        echo $fb_token . $user_name;
        $db =& $this->backend->getDB();
        $sql = "SELECT * FROM user";
        $sql = "select user.id as user_id, image.id as image_id, user.user_name as user_name, image.comment, image.create_date from user INNER JOIN image ON user.id = image.user_id order by image.create_date desc limit 5";
        $result =& $db->query($sql);
        //var_dump($result);
        $i = 0;
        while($data[$i] = $result->fetchRow()) {
            $i++;
        }
        var_dump($data);
    }
}

