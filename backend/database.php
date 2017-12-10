<?php

class Database {

	public $connection;
	public function __construct () {
	  $servername = "localhost";
		$username = "root";
		$password = "";
		$db = "itec_db";

		// $servername = "mysql.hostinger.ro"; //sample host
		// $username = "u407201591_paul";
		// $password = "cacatpisat";
 		// $db = "u407201591_paul";
		// Create connection
		$this->connection = mysqli_connect($servername, $username, $password,$db);
		// $this->connection->select_db($db);

  	}

		public function checkin($userId,$eventId){
			$sql="insert into event_participation(user_id,event_id) values($userId,$eventId)";
			$result = $this->connection->query($sql);
			return new stdClass();
		}
		public function checkout($userId,$eventId){
			$sql="delete from event_participation where user_id = $userId and event_id = $eventId";
			$result = $this->connection->query($sql);
			$obj= new stdClass();
			$obj->sql=$sql;
			$obj->error=mysqli_error($this->connection);
			return $obj;
		}
		public function getParticipants($id){
			$list = array();
			$sql="SELECT * FROM event_participation, user where event_participation.user_id=user.id and event_id = $id";
			$result = $this->connection->query($sql);
	  	$list = array();
  		if ($result->num_rows > 0) {
   			while($row = $result->fetch_assoc()) {
        		$post=new stdClass();
        		$post->username=$row['username'];
        		$post->password=$row['password'];
						$post->mail=$row['mail'];
        		$post->id=$row['id'];
        		array_push($list, $post);
    		}
		}
		return $list;
		}

		public function getLesson($id){
			$sql="select * from lesson where id=$id";
			$result = $this->connection->query($sql);
			if ($result->num_rows > 0) {
   			while($row = $result->fetch_assoc()) {
        		$post=new stdClass();
        		$post->id=$row['id'];
						$post->name=$row['name'];
        		$post->image=$row['image'];
						return $post;
					}
			}
			return new stdClass();
		}

		public function sendNotification($id,$userId,$message){
			$curl = curl_init();

curl_setopt_array($curl, array(
  CURLOPT_URL => "https://fcm.googleapis.com/fcm/send",
  CURLOPT_RETURNTRANSFER => true,
  CURLOPT_ENCODING => "",
  CURLOPT_MAXREDIRS => 10,
  CURLOPT_TIMEOUT => 30,
  CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
  CURLOPT_CUSTOMREQUEST => "POST",
  CURLOPT_POSTFIELDS => "{\n  \"to\": \"/topics/$userId\",\n  \"data\": {\n    \"message\": \"$message\",\n    \"event_id\": $id\n   }\n}",
  CURLOPT_HTTPHEADER => array(
    "authorization: key=AAAAhHqnJuY:APA91bFXguBc-o75-aHEktULgLKKVjeSNtFZ0-fWrei-CSPSdmXoNaBx1IvirUNUkvuiDNczEzJYNqUqhyuD3EgAV0Ov1EnfDv7m8QD6E4dbQXMjiknEPZhSpMMJh6iZeXNeBVumZrb-",
    "cache-control: no-cache",
    "content-type: application/json",
    "postman-token: 7b850a53-8b5f-6b78-4dc3-77072abd0f6d"
  ),
));

$response = curl_exec($curl);
$err = curl_error($curl);
return $response;
		}
		public function confirmTimeChange($id,$userId,$eventId,$new_time){
			$result=new stdClass();
			$result->gcm_result=$this->sendNotification($eventId,$userId,"Your request for changing time was accepted!");
			$sql="delete from event_change_time where id = $id";
			$this->connection->query($sql);
			$sql2="update event set time= $new_time where id = $eventId";
			$this->connection->query($sql2);
		return $result;
		}

		public function getRequestedEvent($requestId){
			$sql ="select * from event_request where id = $requestId";
			$result = $this->connection->query($sql);
			if ($result->num_rows > 0) {
   			while($row = $result->fetch_assoc()) {
        		$post=new stdClass();
        		$post->id=$row['id'];
						$post->name=$row['name'];
						$post->time=$row['time'];
						$post->section=$row['section'];
						$post->lessonId=$row['lesson_id'];
						$post->teacher=$row['teacher'];
						$post->address=$row['address'];
        		$post->userId=$row['user_id'];
						return $post;
					}
			}
		}

		public function confirmEvent($requestId){
			$event=$this->getRequestedEvent($requestId);
			$sql="insert into event(name,time,section,lesson_id,teacher,address) values('$event->name',$event->time,'$event->section',$event->lessonId,'$event->teacher','$event->address')";
			$result = $this->connection->query($sql);
			$eventId=mysqli_insert_id($this->connection);

			$result=new stdClass();
			$result->gcm_result=$this->sendNotification($eventId,$event->userId,"Your event was added!");
			$sql="delete from event_request where id = $requestId";
			$this->connection->query($sql);
			return $eventId;
		}
		public function getEvents($section){
			$sql = "select * from event where section = '$section'";
			$result = $this->connection->query($sql);
	  	$list = array();
  		if ($result->num_rows > 0) {
   			while($row = $result->fetch_assoc()) {
        		$post=new stdClass();
        		$post->id=$row['id'];
        		$post->time=$row['time'];
						$post->name=$row['name'];
						$post->section=$row['section'];
						$post->teacher=$row['teacher'];
						$post->address=$row['address'];

						$post->participants=$this->getParticipants($post->id);
						$post->lesson_obj=$this->getLesson($row['lesson_id']);

        		array_push($list, $post);
    		}
			}
			return $list;
		}
  	public function getUsers(){
  		$sql = "select * from user";
  		$result = $this->connection->query($sql);
	  	$list = array();
  		if ($result->num_rows > 0) {
   			while($row = $result->fetch_assoc()) {
        		$post=new stdClass();
        		$post->id=$row['id'];
        		$post->username=$row['username'];
        		$post->password=$row['password'];
        		array_push($list, $post);
    		}
		}
		return $list;
  	}


}

?>
