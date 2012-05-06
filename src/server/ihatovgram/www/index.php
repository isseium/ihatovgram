<?php

require_once dirname(__FILE__) . '/../app/Ihatovgram_Controller.php';

/**
 * If you want to enable the UrlHandler, comment in following line,
 * and then you have to modify $action_map on app/Ihatovgram_UrlHandler.php .
 *
 */
// $_SERVER['URL_HANDLER'] = 'index';

/**
 * Run application.
 */
Ihatovgram_Controller::main('Ihatovgram_Controller', 'index');

