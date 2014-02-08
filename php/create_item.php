<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (count($_POST) === 7 && isset($_POST['name']) && isset($_POST['cost']) && isset($_POST['description']) && isset($_POST['location']) && isset($_POST['discount']) && isset($_POST['startdate']) && isset($_POST['enddate'])) {
 
    array_map('mysql_real_escape_string', $_POST);
    extract($_POST);
     
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO items (name, description, cost, location, discount, startdate, enddate) VALUES('$name', '$description', $cost, '$location', '$discount', '$startdate', '$enddate')");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Item successfully created.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
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