<?php
    session_start();
    include("PHP/config.php");
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/default.css" type="text/css">
    <link rel="stylesheet" href="css/loginpage.css" type="text/css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="/css/default_php.css">
    <title>Login</title>
    
</head>
<body>
    <div class="menu">
        <a href="index.php">Home</a>
        <a href="Student Record.html">Student Record</a>
        <a href="Course.php">Course</a>
        <a href="/Faculty/Faculty.html">Faculty</a>
        <a href="https://e-learn.rjt.ac.lk/" target="_blank">LMS</a>
        <!-- <a href="Security and Privacy.html">Privacy</a> -->
        <div class="head">
            <h1>University Student Management System</h1>
        </div>
        
    </div>
    
    <div class="Login">
        <h1>User Login</h1>
        <form action="loginpage.php" method="post" enctype="multipart/form-data" autocomplete="off">
            <div>
                <label for="uName">User Name
                    <i class='bx bxs-user'></i>
                </label>
                <input type="text" name="ulName" id="uName" placeholder="Enter Your LMS User Name" required>
                
            </div>
            <div>
                <label for="Password">Password
                    <i class='bx bxs-lock-alt'></i>
                </label>
                <input type="password" name="Password" id="Password" class="pwd" required>
            </div>
            <br>
            <div class="submit">
                <button type="submit" name="submit">Login</button>
            </div>
        </form>
    </div>
    <div class="bottom_login">
        <p>If you doesn't Signup <br>First Please Signup</p> <br>
        <a class="Sign_up" href="Student Record.html">Signup</a> <br>
        <button type="button" class="forgot_btn" onclick="sendForgotPasswordEmail()">Forgot Password</button> <br>
    </div>
    
    <?php
    
        if(isset($_POST["submit"]))
        {
            $ulName=$_POST["ulName"];
            $Password=$_POST["Password"];
            $sql="SELECT uName,conPassword FROM student_register WHERE uName='$ulName' && conPassword='$Password'";
            $result=$conn->query($sql);
            if($result->num_rows==1)
            {
                $_SESSION['userName'] = $ulName;
                header('Location:index.php');
                // exit;
            }
            else
            {
                echo "<p class='invalid'>!Invalid Username Or Password</p>";

            }
        }
        else
        {
            echo "<p class='invalid'> Please fill the details <br></p>";
        }

    ?>
    <script>
        function sendForgotPasswordEmail() {
            const email = "usmsadmin@gmail.com"; //Admin Gmail
            const subject = "Forgot password";
            const body = "I request to forgot password , Enter Your Username[] ";

            window.location.href = `mailto:${email}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;
        }
    </script>
</body>
</html>