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
        echo "hogehoge";
    }
}

