<?php
    session_start();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>loginpage</title>
    <link rel="stylesheet" href="../css/default_php.css">
</head>
<body>
    <?php
        
        $servername = "localhost";
        $username = "root";
        $password = "";
        $dbname = "usms";

        // Create connection
        $conn = new mysqli($servername, $username, $password, $dbname);

        // Check connection
        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }
        echo "Connected successfully";


        if(isset($_POST["submit"]))
        {
            $uName=$_POST["uName"];
            $Password=$_POST["Password"];
            $sql="SELECT uName,conPassword FROM student_register WHERE uName='$uName' && conPassword='$Password'";
            $result=$conn->query($sql);
            if($result->num_rows==1)
            {
                $_SESSION["userName"]=$uName;
                header('Location: ../index.php?username=' . $uName);
                exit();
            }
            else
            {
                echo "<p class='invalid'>!Invalid Username Or Password</p>";

                echo '<p>If you have account:<a href="../loginpage.html"> GO to Login Page</a></p>';
                echo'<p>If you new User:<a href="../Student Record.html"> GO to Registration Page</a> </p>';
            }
        }
        else
        {
            echo "<p>Please fill the details</p>";
        }

        $conn->close();
    ?>
    
</body>
</html>