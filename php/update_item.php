<?php
 
/*
 * Following code will update a product information
 * A product is identified by product id (id)
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (count($_POST) === 8 && isset($_POST['id']) && isset($_POST['name']) && isset($_POST['cost']) && isset($_POST['description']) && isset($_POST['location']) && isset($_POST['discount']) && isset($_POST['startdate']) && isset($_POST['enddate'])) {
 
    array_map('mysql_real_escape_string', $_POST);
    extract($_POST);
 
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