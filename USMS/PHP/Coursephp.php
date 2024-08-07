<?php
    include("config.php");
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Course_php</title>
</head>
<body>
    <?php
        
        if(isset($_POST["submit"]))
        {
            $faculty=$_POST["faculty"];
            $sName=$_POST["sName"];
            $regNo =$_POST["regNo"];
            $indexNo =$_POST["indexNo"];
            $mobileNo=$_POST["mobileNo"];
            $year=$_POST["year"];
            $sem=$_POST["sem"];
            $department=$_POST["department"];
            $date=$_POST["date"];
            $signature1=$_FILES["signature1"];
            $tcValue=$_POST["tcValue"];
            
           
            $sql="INSERT INTO course_register (faculty,sName,regNo,indexNo,mobileNo,year,sem,department,date,signature1,tcValue) 
            VALUES('$faculty','$sName','$regNo','$indexNo','$mobileNo','$year','$sem','$department','$date','$signature1','$tcValue')";
            $stmt = $conn->prepare($sql);

            // for ($i = 0; $i < 15; $i++) {
            //     if (!empty($_POST['cCode'][$i])) {
            //         $course_code = $_POST['cCode'][$i];
            //         $course_title = $_POST['cTitle'][$i];
            //         $course_status = $_POST['cStatus'][$i];
            //         $credit_value = $_POST['cValue'][$i];
    
            //         $stmt = $conn->prepare("INSERT INTO course_entrol (student_id,cCode,cTitle,cStatus,cValue) VALUES (?, ?, ?, ?, ?)");
            //         $stmt->bind_param("isssi", $student_id, $course_code, $course_title, $course_status, $credit_value);
            //         $stmt->execute();
            //     }
            // }
            // $stmt->close();

            // $cCode=$_POST["cCode"];
            // $cTitle=$_POST["cTitle"];
            // $cStatus=$_POST["cStatus"];
            // $cValue=$_POST["s_cValue"];

            // $student_id = $conn->insert_id;
            // foreach($cCode as $cCodes)
            // {
                
            //     $sql_s="INSERT INTO course_entrol (student_id,cCode,cTitle,cStatus,s_cValue)
            //     VALUES('$student_id','$s_cCode','$s_cTitle','$s_cStatus','$s_cValue')";
            //     $stmt2 = $conn->prepare($sql_s);
            //     $conn-> query($sql_s);
            // }
           
               
            //     $stmt = $conn->prepare($sql);

            if($conn-> query($sql))
            {
                // echo"<p>Welcome, </p>" .$sName ;
                // echo"<p>Your account successfully registered</p> ";
                header("location:../index.php");

            }
            else
            {
                echo"<p> Some Error try again </p>";

            }
            // $conn->rollback();
            // $conn->close();

        }

    ?>
    
</body>
</html>