<?php
 
/*
 * Following code will update a product information
 * A product is identified by product id (id)
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['id']) && isset($_POST['name']) && isset($_POST['cost']) && isset($_POST['description']) && isset($_POST['location']) && isset($_POST['discount']) && isset($_POST['startdate']) && isset($_POST['enddate'])) {
 
    $id = $_POST['id'];
	$name = $_POST['name'];
    $description = $_POST['description'];
    $cost = $_POST['cost'];
	$location = $_POST['location'];
	$discount = $_POST['discount'];
	$startdate = $_POST['startdate'];
	$enddate = $_POST['enddate'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched id
    $result = mysql_query("UPDATE items SET name = '$name', cost = '$cost', description = '$description', discount = '$discount', startdate = '$startdate', enddate = '$enddate' WHERE id = $id");
 
    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Product successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
 
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>