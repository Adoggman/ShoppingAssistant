<?php
 
/*
 * Following code will list all the items
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// get all items from items table
$result = mysql_query("SELECT * FROM items order by name") or die(mysql_error());
 
// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // items node
    $response["items"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $item = array();
        $item["id"] = $row["id"];
        $item["name"] = $row["name"];
        $item["description"] = $row["description"];
        $item["cost"] = $row["cost"];
        $item["location"] = $result["location"];
        $item["discount"] = $result["discount"];
        
 
        // push single item into final response array
        array_push($response["items"], $item);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no items found
    $response["success"] = 0;
    $response["message"] = "No items found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>