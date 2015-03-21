<?php
$con = mysql_connect("shuttletrackerdb.db.3579559.hostedresource.com","shuttletrackerdb@72.167.233.37","<password>");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("shuttletrackerdb", $con);

$mysqldate = date( 'Y-m-d H:m:s', $phpdate );
$phpdate = strtotime( $mysqldate );

$sql="INSERT INTO shuttle1 (Date, Xcoord, Ycoord, BusDriverUsername) VALUES (NOW(),'42626270','-72151545','Manny')";

if (!mysql_query($sql,$con))
  {
  die('Error: ' . mysql_error());
  }
echo "Sucess";

mysql_close($con)
?>
