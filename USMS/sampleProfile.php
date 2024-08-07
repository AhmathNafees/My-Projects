<?php
    include("PHP/config.php");
    session_start();
    if(!isset($_SESSION["userName"]))
    {
        header("Location:loginpage.php");
        
    }
    else
    {
        $username=$_SESSION["userName"];
    }
?>
<?php
    $sql = "SELECT * FROM student_register WHERE uName='$username'";
    $result = $conn->query($sql);
    if($result->num_rows>0)
    {
        // echo "Have value";
        while($row=$result->fetch_assoc())
        {
            // echo "{$row["REG_NO"]}";
            $reg_no = $row['REG_NO'];
            $sName = $row['sName'];
            $fName = $row['fName'];
            $mName = $row['mName'];
            $gender = $row['gender'];
            $DOB = $row['DOB'];
            $mNumber = $row['mNumber'];
            $email = $row['email'];
            $address = $row['address'];
            $faculty = $row['faculty'];
            $department = $row['department'];
            // $uName = $row['uName'];
            $cPassword = $row['conPassword'];
        }
    }
    else
    {
        echo "row is empty";
    }
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile</title>
    <link rel="stylesheet" href="/css/Profile.css" type="text/css">
</head>
<body>
<div class="menu">
        <a href="index.php">Home</a>
        <a href="Student Record.html">Student Record</a>
        <a href="Course.php">Course</a>
        <a href="/Faculty/Faculty.html">Faculty</a>
        <a href="https://e-learn.rjt.ac.lk/" target="_blank">LMS</a>
        <!-- <a href="Security and Privacy.html">Privacy</a> -->
        <div class="header">
            <h1>My Profile</h1>
        </div>
        
    </div>
    <!-- <div class="header">
        <h1>My Profile</h1>
    </div> -->
    <div class="container">
        
        <div class="firstgroup">
            <div class="group">
                <p>1. Registration No :&nbsp;<?php echo "$reg_no"; ?>
                <button class="btne_s" id="editButton">Edit</button> 
                <form action="sampleProfile.php" method="post">
                    <div class="edit-form" id="editForm">
                        <label for="reg_noInput">New Registration No :</label>
                        <input class="btne_s" type="text" id="reg_noInput" name="REG_NO" ><br>
                        <button class="btne_s" id="submitButton" name="REG_NOsubmit">Submit</button>
                    </div>
                </form>
                <?php
                    if(isset($_POST["REG_NOsubmit"]))
                    {
                        // $faculty=$_POST["faculty"];
                        // $sName=$_POST["sName"];
                        $regNo =$_POST["REG_NO"];
                        // $indexNo =$_POST["indexNo"];
                        // $mobileNo=$_POST["mobileNo"];
                        // $year=$_POST["year"];
                        // $sem=$_POST["sem"];
                        // $department=$_POST["department"];
                        // $date=$_POST["date"];
                        // $signature1=$_FILES["signature1"];
                        // $tcValue=$_POST["tcValue"];
                        
                       
                        $sql = "UPDATE student_register SET REG_NO ='$regNo' WHERE uName ='$username'";
                        $stmt = $conn->prepare($sql);
            
                        if($conn-> query($sql))
                        {
                            echo "Your Profile is updated";
                            // echo"<p>Welcome, </p>" .$sName ;
                            // echo"<p>Your account successfully registered</p> ";
                            // header("location:../index.php");
            
                        }
                        else
                        {
                            echo"<p> Some Error try again </p>";
            
                        }
            
                    }
    
                ?>
                
                </p>
    
            </div>
            <div class="group">
                <p>2. Student Name &nbsp;:&nbsp;<?php echo "$sName"; ?>
                    <button class="btne_s" id="editButton" onclick="editsName()" >Edit</button> 
                    <form action="sampleProfile.php" method="post">
                        <div class="edit-form" id="editsName">
                            <label for="sNameInput">New Student Name :</label>
                            <input class="btne_s" type="text" id="sName" name="sName" ><br>
                            <button class="btne_s" id="submitButton" name="sNamesubmit">Submit</button>
                        </div>
                    </form>
                    <?php
                        if(isset($_POST["sNamesubmit"]))
                        {
                            // $faculty=$_POST["faculty"];
                            $sName=$_POST["sName"];
                            // $regNo =$_POST["REG_NO"];
                            // $indexNo =$_POST["indexNo"];
                            // $mobileNo=$_POST["mobileNo"];
                            // $year=$_POST["year"];
                            // $sem=$_POST["sem"];
                            // $department=$_POST["department"];
                            // $date=$_POST["date"];
                            // $signature1=$_FILES["signature1"];
                            // $tcValue=$_POST["tcValue"];
                            
                        
                            $sql = "UPDATE student_register SET sName ='$sName' WHERE uName ='$username'";
                            $stmt = $conn->prepare($sql);
                
                            if($conn-> query($sql))
                            {
                                echo "Your Profile is updated";
                                // echo"<p>Welcome, </p>" .$sName ;
                                // echo"<p>Your account successfully registered</p> ";
                                // header("location:../index.php");
                
                            }
                            else
                            {
                                echo"<p> Some Error try again </p>";
                
                            }
                
                        }
                        
                    ?>
                </p>
                
            </div>
            <div class="group">
                <p>3. Date of Birth &nbsp;&nbsp;&nbsp;:&nbsp;<?php echo "$DOB"; ?>
                    <button class="btne_s" id="editButton" onclick="editdate()" >Edit</button> 
                    <form action="sampleProfile.php" method="post">
                        <div class="edit-form" id="editdate">
                            <label for="dateInput">New Date of Birth :</label>
                            <input class="btne_s" type="date" id="date" name="DOB" ><br>
                            <button class="btne_s" id="submitButton" name="datesubmit">Submit</button>
                        </div>
                    </form>
                    <?php
                        if(isset($_POST["datesubmit"]))
                        {
                            // $faculty=$_POST["faculty"];
                            // $sName=$_POST["sName"];
                            // $regNo =$_POST["REG_NO"];
                            // $indexNo =$_POST["indexNo"];
                            // $mobileNo=$_POST["mobileNo"];
                            // $year=$_POST["year"];
                            // $sem=$_POST["sem"];
                            // $department=$_POST["department"];
                            $date=$_POST["DOB"];
                            // $signature1=$_FILES["signature1"];
                            // $tcValue=$_POST["tcValue"];
                            
                        
                            $sql = "UPDATE student_register SET DOB ='$date' WHERE uName ='$username'";
                            $stmt = $conn->prepare($sql);
                
                            if($conn-> query($sql))
                            {
                                echo "Your Profile is updated";
                                // echo"<p>Welcome, </p>" .$sName ;
                                // echo"<p>Your account successfully registered</p> ";
                                // header("location:../index.php");
                
                            }
                            else
                            {
                                echo"<p> Some Error try again </p>";
                
                            }
                
                        }
                    ?>
                </p>
                
            </div>
            <div class="group">
                <p>4. Mobile Number :&nbsp;<?php echo "$mNumber"; ?>
                <button class="btne_s" id="editButton" onclick="editmNumber()">Edit</button> 
                    <form action="sampleProfile.php" method="post">
                        <div class="edit-form" id="editmNumber">
                            <label for="mNumberInput">New Mobile Number :</label>
                            <input class="btne_s" type="text" id="mNumberInput" name="mNumber" ><br>
                            <button class="btne_s" id="submitButton" name="submitmNumber">Submit</button>
                        </div>
                    </form>
                    <?php
                        if(isset($_POST["submitmNumber"]))
                        {
                            // $faculty=$_POST["faculty"];
                            // $sName=$_POST["sName"];
                            // $regNo =$_POST["REG_NO"];
                            // $indexNo =$_POST["indexNo"];
                            $mobileNo=$_POST["mNumber"];
                            // $year=$_POST["year"];
                            // $sem=$_POST["sem"];
                            // $department=$_POST["department"];
                            // $date=$_POST["date"];
                            // $signature1=$_FILES["signature1"];
                            // $tcValue=$_POST["tcValue"];
                            
                        
                            $sql = "UPDATE student_register SET mNumber ='$mobileNo' WHERE uName ='$username'";
                            $stmt = $conn->prepare($sql);
                
                            if($conn-> query($sql))
                            {
                                echo "Your Profile is updated";
                                // echo"<p>Welcome, </p>" .$sName ;
                                // echo"<p>Your account successfully registered</p> ";
                                // header("location:../index.php");
                
                            }
                            else
                            {
                                echo"<p> Some Error try again </p>";
                
                            }
                
                        }
                    ?>
                </p>
        </div>
            
        <div class="firstgroup">
            <div class="group">
                <p>5. Email &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:
                    &nbsp;<?php echo "$email"; ?>
                    <button class="btne_s" id="editButton" onclick="editemail()">Edit</button> 
                    <form action="sampleProfile.php" method="post">
                        <div class="edit-form" id="editemail">
                            <label for="emailInput">New Email :</label>
                            <input class="btne_s" type="text" id="emailInput" name="email" ><br>
                            <button class="btne_s" id="submitButton" name="submitemail">Submit</button>
                        </div>
                    </form>
                    <?php
                        if(isset($_POST["submitemail"]))
                        {
                            // $faculty=$_POST["faculty"];
                            // $sName=$_POST["sName"];
                            // $regNo =$_POST["REG_NO"];
                            // $indexNo =$_POST["indexNo"];
                            // $mobileNo=$_POST["mobileNo"];
                            // $year=$_POST["year"];
                            // $sem=$_POST["sem"];
                            // $department=$_POST["department"];
                            // $date=$_POST["date"];
                            // $signature1=$_FILES["signature1"];
                            // $tcValue=$_POST["tcValue"];
                            $email=$_POST['email'];
                            
                        
                            $sql = "UPDATE student_register SET email ='$email' WHERE uName ='$username'";
                            $stmt = $conn->prepare($sql);
                
                            if($conn-> query($sql))
                            {
                                echo "Your Profile is updated";
                                // echo"<p>Welcome, </p>" .$sName ;
                                // echo"<p>Your account successfully registered</p> ";
                                // header("location:../index.php");
                
                            }
                            else
                            {
                                echo"<p> Some Error try again </p>";
                
                            }
                
                        }
                    ?>
                </p>
                
            </div>
            <div class="group">
                <p>6. Address &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;<?php echo "$address"; ?>
                <button class="btne_s" id="editButton" onclick="editaddress()">Edit</button> 
                    <form action="sampleProfile.php" method="post">
                        <div class="edit-form" id="editaddress">
                            <label for="addressInput">New Address :</label>
                            <input class="btne_s" type="text" id="address" name="address" ><br>
                            <button class="btne_s" id="submitButton" name="submitaddress">Submit</button>
                        </div>
                    </form>
                    <?php
                        if(isset($_POST["submitaddress"]))
                        {
                            // $faculty=$_POST["faculty"];
                            // $sName=$_POST["sName"];
                            // $regNo =$_POST["REG_NO"];
                            // $indexNo =$_POST["indexNo"];
                            // $mobileNo=$_POST["mobileNo"];
                            // $year=$_POST["year"];
                            // $sem=$_POST["sem"];
                            // $department=$_POST["department"];
                            // $date=$_POST["date"];
                            // $signature1=$_FILES["signature1"];
                            // $tcValue=$_POST["tcValue"];
                            $address=$_POST['address'];
                            
                        
                            $sql = "UPDATE student_register SET address ='$address' WHERE uName ='$username'";
                            $stmt = $conn->prepare($sql);
                
                            if($conn-> query($sql))
                            {
                                echo "Your Profile is updated";
                                // echo"<p>Welcome, </p>" .$sName ;
                                // echo"<p>Your account successfully registered</p> ";
                                // header("location:../index.php");
                
                            }
                            else
                            {
                                echo"<p> Some Error try again </p>";
                
                            }
                
                        }
                    ?>
                </p>
                
            </div>
            <!-- <div class="group">
                <p>7. User Name &nbsp;&nbsp;&nbsp;&nbsp;:</p>
                
            </div> -->
            <div class="group">
                <p>7. Faculty &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;<?php echo "$faculty"; ?>
                <button class="btne_s" id="editButton" onclick="editfaculty()">Edit</button> 
                    <form action="sampleProfile.php" method="post">
                        <div class="edit-form" id="editfaculty">
                            <label for="facultyInput">New Faculty :</label>
                            <input class="btne_s" type="text" id="facultyInput" name="faculty" ><br>
                            <button class="btne_s" id="submitButton" name="submitfaculty">Submit</button>
                        </div>
                    </form>
                    <?php
                        if(isset($_POST["submitfaculty"]))
                        {
                            $faculty=$_POST["faculty"];
                            // $sName=$_POST["sName"];
                            // $regNo =$_POST["REG_NO"];
                            // $indexNo =$_POST["indexNo"];
                            // $mobileNo=$_POST["mobileNo"];
                            // $year=$_POST["year"];
                            // $sem=$_POST["sem"];
                            // $department=$_POST["department"];
                            // $date=$_POST["date"];
                            // $signature1=$_FILES["signature1"];
                            // $tcValue=$_POST["tcValue"];
                            
                        
                            $sql = "UPDATE student_register SET faculty ='$faculty' WHERE uName ='$username'";
                            $stmt = $conn->prepare($sql);
                
                            if($conn-> query($sql))
                            {
                                echo "Your Profile is updated";
                                // echo"<p>Welcome, </p>" .$sName ;
                                // echo"<p>Your account successfully registered</p> ";
                                // header("location:../index.php");
                
                            }
                            else
                            {
                                echo"<p> Some Error try again </p>";
                
                            }
                
                        }
                    ?>
                </p>
                
            </div>
            <div class="group">
                <p>8. Department &nbsp;&nbsp;&nbsp;:&nbsp;<?php echo "$department"; ?>
                <button class="btne_s" id="editButton" onclick="editdepartment()">Edit</button> 
                    <form action="sampleProfile.php" method="post">
                        <div class="edit-form" id="editdepartment">
                            <label for="departmentInput">New Registration No :</label>
                            <input class="btne_s" type="text" id="departmentInput" name="department" ><br>
                            <button class="btne_s" id="submitButton" name="submitdepartment">Submit</button>
                        </div>
                    </form>
                    <?php
                        if(isset($_POST["submitdepartment"]))
                        {
                            // $faculty=$_POST["faculty"];
                            // $sName=$_POST["sName"];
                            // $regNo =$_POST["REG_NO"];
                            // $indexNo =$_POST["indexNo"];
                            // $mobileNo=$_POST["mobileNo"];
                            // $year=$_POST["year"];
                            // $sem=$_POST["sem"];
                            $department=$_POST["department"];
                            // $date=$_POST["date"];
                            // $signature1=$_FILES["signature1"];
                            // $tcValue=$_POST["tcValue"];
                            
                        
                            $sql = "UPDATE student_register SET department ='$department' WHERE uName ='$username'";
                            $stmt = $conn->prepare($sql);
                
                            if($conn-> query($sql))
                            {
                                echo "Your Profile is updated";
                                // echo"<p>Welcome, </p>" .$sName ;
                                // echo"<p>Your account successfully registered</p> ";
                                // header("location:../index.php");
                
                            }
                            else
                            {
                                echo"<p> Some Error try again </p>";
                
                            }
                
                        }
                    ?>
                </p>
                
            </div>
            <div class="group">
                <p>9. Password &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;<?php echo "$cPassword"; ?>
                <button class="btne_s" id="editButton" onclick="editconPassword()">Edit</button> 
                    <form action="sampleProfile.php" method="post">
                        <div class="edit-form" id="editconPassword">
                            <label for="conPasswordInput">New Registration No :</label>
                            <input class="btne_s" type="text" id="conPasswordInput" name="conPassword" ><br>
                            <button class="btne_s" id="submitButton" name="submitconPassword">Submit</button>
                        </div>
                    </form>
                    <?php
                        if(isset($_POST["submitconPassword"]))
                        {
                            // $faculty=$_POST["faculty"];
                            // $sName=$_POST["sName"];
                            // $regNo =$_POST["REG_NO"];
                            // $indexNo =$_POST["indexNo"];
                            // $mobileNo=$_POST["mobileNo"];
                            // $year=$_POST["year"];
                            // $sem=$_POST["sem"];
                            // $department=$_POST["department"];
                            // $date=$_POST["date"];
                            // $signature1=$_FILES["signature1"];
                            // $tcValue=$_POST["tcValue"];
                            $cPassword=$_POST['conPassword'];
                            
                        
                            $sql = "UPDATE student_register SET conPassword ='$cPassword' WHERE uName ='$username'";
                            $stmt = $conn->prepare($sql);
                
                            if($conn-> query($sql))
                            {
                                echo "Your Profile is updated";
                                // echo"<p>Welcome, </p>" .$sName ;
                                // echo"<p>Your account successfully registered</p> ";
                                // header("location:../index.php");
                
                            }
                            else
                            {
                                echo"<p> Some Error try again </p>";
                
                            }
                
                        }
                    ?>
                </p>
                
            </div>
        </div>
    </div>
    <script>
        document.getElementById('editButton').addEventListener('click', function() {
            document.getElementById('editForm').style.display = 'block';
        });
        function editsName()
        {
            document.getElementById('editsName').style.display = 'block';
        }
        function editdate()
        {
            document.getElementById('editdate').style.display = 'block';
        }
        function editmNumber()
        {
            document.getElementById('editmNumber').style.display = 'block';
        }
        function editemail()
        {
            document.getElementById('editemail').style.display = 'block';
        }
        function editaddress()
        {
            document.getElementById('editaddress').style.display = 'block';
        }
        function editfaculty()
        {
            document.getElementById('editfaculty').style.display = 'block';
        }
        function editdepartment()
        {
            document.getElementById('editdepartment').style.display = 'block';
        }
        function editconPassword()
        {
            document.getElementById('editconPassword').style.display = 'block';
        }


        // document.getElementById('submitButton').addEventListener('click', function() {
        //     var reg_no = document.getElementById('reg_noInput').value;
        //     var email = document.getElementById('emailInput').value;
            
        //     document.getElementById('reg_no').innerText = name;
        //     document.getElementById('studentEmail').innerText = email;

        //     document.getElementById('editForm').style.display = 'none';
        // });
    </script>
</body>
</html>