<?php
$hostname="shuttletrackerdb.db.3579559.hostedresource.com";
$username="shuttletrackerdb@72.167.233.37";
$password="<password>";
$dbname="shuttletrackerdb";
$usertable="shuttle1";

mysql_connect($hostname,$username, $password) or die ("<html><script language='JavaScript'>alert('Unable to connect to database! Please try again later.'),history.go(-1)</script></html>");
mysql_select_db($dbname);

# Check If Record Exists

$query = "SELECT * FROM $usertable ORDER BY MessageID DESC LIMIT 1";

$result = mysql_query($query);

if($result)
{
while($row = mysql_fetch_array($result))
{
$MessageID = $row["MessageID"];
$Date = $row["Date"];
$Xcoord = $row["Xcoord"];
$Ycoord = $row["Ycoord"];
$BusDriverUsername = $row["BusDriverUsername"];


echo "".$MessageID ."," .$Date ."," .$Xcoord ."," .$Ycoord ."," .$BusDriverUsername ."<br>";
}
}
?>
