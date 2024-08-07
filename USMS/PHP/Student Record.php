<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Record</title>
    <link rel="stylesheet" href="../css/default_php.css">
</head>
<body>
    <!-- <p>
        Welcome <?php echo $_POST["REG_NO"] ?> <br>
    </p>
    <p>HELLO</p> -->
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
            $REG_NO=$_POST["REG_NO"];
            $sName=$_POST["sName"];
            $fName=$_POST["fName"];
            $mName=$_POST["mName"];
            $gender=$_POST["gender"];
            $DOB=$_POST["DOB"];
            $mNumber=$_POST["mNumber"];
            $email=$_POST["email"];
            $address=$_POST["address"];
            $faculty=$_POST["faculty"];
            $department=$_POST["department"];
            $uName=$_POST["uName"];
            $Uni_ID=$_FILES["Uni_ID"]["name"];
            $conPassword=$_POST["conPassword"];
           
            $sql="INSERT INTO student_register (REG_NO,sName,fName,mName,gender,DOB,mNumber,email,address,faculty,department,uName,Uni_ID,conPassword) 
            VALUES('$REG_NO','$sName','$fName','$mName','$gender','$DOB','$mNumber','$email','$address','$faculty','$department','$uName','$Uni_ID','$conPassword') ";
            $stmt = $conn->prepare($sql);

            if($conn-> query($sql))
            {
                // echo"<p>Welcome, </p>" .$sName ;
                // echo"<p>Your account successfully registered</p> ";
                header("location:../loginpage.php");

            }
            else
            {
                echo"<p> Some Error try again </p>";

            }
            // try {
            //     if ($stmt->execute()) {
            //         // Redirect to login page with a success message
            //         header("Location: loginpage.html?message=Your account has been successfully registered");
            //         exit();
            //     }
            // } catch (mysqli_sql_exception $e) {
            //     if ($e->getCode() === 1062) { // 1062 is the error code for duplicate entry
            //         echo "<p>Username already taken. Please choose a different username.</p>";
            //     } else {
            //         echo "<p>Some error occurred: " . $e->getMessage() . "</p>";
            //     }
            // }
    
            // $stmt->close();

        }

        // Close the connection
        $conn->close();
    ?>

    
</body>
</html>