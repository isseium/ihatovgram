<?php /* Smarty version 2.6.26, created on 2012-05-06 00:06:03
         compiled from createuser.tpl */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('block', 'form', 'createuser.tpl', 2, false),array('function', 'form_input', 'createuser.tpl', 3, false),array('function', 'form_submit', 'createuser.tpl', 8, false),)), $this); ?>
<H2>ユーザ登録</h2>
<?php $this->_tag_stack[] = array('form', array('name' => 'create_user','ethna_action' => 'createuser')); $_block_repeat=true;smarty_block_form($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?><br>
<?php echo smarty_function_form_input(array('name' => 'type','value' => 'createuser'), $this);?>

fb_token:<br>
<?php echo smarty_function_form_input(array('name' => 'fb_token'), $this);?>
<br>
user_name:<br>
<?php echo smarty_function_form_input(array('name' => 'user_name'), $this);?>
<br>
<?php echo smarty_function_form_submit(array(), $this);?>

<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_form($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>