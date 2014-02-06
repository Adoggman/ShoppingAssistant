<?php
 
/*
 * Following code will get single item details
 * A item is identified by item id (id)
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// check for post data
if (isset($_GET["email"]) && isset($_GET["password"])) {
    $email = $_GET['email'];
	$password = $_GET['password'];
 
    // get a item from items table
    $result = mysql_query("SELECT * FROM users WHERE email = '$email' AND password='$password'");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $item = array();
            $item["id"] = $result["id"];
            $item["name"] = $result["name"];
            $item["email"] = $result["email"];
            $item["admin"] = $result["admin"];
			$item["password"] = $result["password"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["user"] = array();
 
            array_push($response["user"], $item);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no item found
            $response["success"] = 0;
            $response["message"] = "No item found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no item found
        $response["success"] = 0;
        $response["message"] = "No item found";
 
        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>