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
if (isset($_GET["id"])) {
    $id = mysql_real_escape_string($_GET['id']);
 
    // get a item from items table
    $result = mysql_query("SELECT * FROM items WHERE id = $id");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $item = array();
            $item["id"] = $result["id"];
            $item["name"] = $result["name"];
            $item["description"] = $result["description"];
            $item["cost"] = $result["cost"];
            $item["location"] = $result["location"];
            $item["discount"] = $result["discount"];
			$item["startdate"] = $result["startdate"];
			$item["enddate"] = $result["enddate"];
            
            // success
            $response["success"] = 1;
 
            // user node
            $response["item"] = array();
 
            array_push($response["item"], $item);
 
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