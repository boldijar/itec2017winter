<?php
require_once 'vendor/autoload.php';
include 'post.php';
include 'database.php';
include 'newpostsavailable.php';
use Silex\Application;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
$app = new Silex\Application();
$database = new Database;

//
// $app->get('/users', function ($name) use ($app) {
//     return "muie";
// });


$app->get('/users', function () use ($app) {
	global $database;
	$results = $database->getUsers();
	return json_encode($results);
});

$app->get('/events', function (Request $request) use ($app) {
	global $database;
	$params = $request->query->all();
	$section=$params['section'];
	$results = $database->getEvents($section);
	return json_encode($results);
});

$app->get('/confirm_time_change', function (Request $request) use ($app) {
	global $database;
	$params = $request->query->all();
	$id=$params['id'];
	$userId=$params['user_id'];
	$eventId=$params['event_id'];
	$newTime=$params['new_time'];
	return json_encode($database->confirmTimeChange($id,$userId,$eventId,$newTime));
});

$app->get('/confirm_event', function (Request $request) use ($app) {
	global $database;
	$params = $request->query->all();
	$id=$params['id'];
	return json_encode($database->confirmEvent($id));
});

$app->get('/checkin', function (Request $request) use ($app) {
	global $database;
	$params = $request->query->all();
	$userId=$params['user_id'];
	$eventId=$params['event_id'];
	return json_encode($database->checkin($userId,$eventId));
});

$app->get('/checkout', function (Request $request) use ($app) {
	global $database;
	$params = $request->query->all();
	$userId=$params['user_id'];
	$eventId=$params['event_id'];
	return json_encode($database->checkout($userId,$eventId));
});
$app->run();

?>
