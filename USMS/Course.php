<?php
    session_start();
    if(!isset($_SESSION["userName"]))
    {
        header("Location:loginpage.php");
        // exit;
        // session_destroy();
    }
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/default.css" type="text/css">
    <link rel="stylesheet" href="css/Course.css" type="text/css">
    <title>Course
    </title>
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
            <h1>Course Registration</h1>
        </div>
    </div>
    <div class="container">
        
        
        <h2>Application for Registration of Course Units</h2>
        
        <form action="./PHP/Coursephp.php" method="post" enctype="multipart/form-data" id="courseForm">
            <div class="Stu_details">
                <label for="faculty">Select Your Faculty</label>
                <Select class="Select-box" name="faculty" id="faculty" onchange=changeDepartment(this.value) required>
                    <option value="" disabled selected>Select Faculty</option>
                    <option value="Faculty of Technology" >Faculty of Technology</option>
                    <option value="Faculty of Applied Sciences" >Faculty of Applied Sciences</option>
                    <option value="Agriculture" >Agriculture</option>
                    <option value="Management Studies" >Management Studies</option>
                    <option value="Medicine and Allied Sciences" >Medicine & Allied Sciences</option>
                    <option value="Social Sciences and Humanities" >Social Sciences & Humanities</option>
                </Select>
            </div>
            <div class="Stu_details">
                <label for="Name of the Student">1. Name of the Student:</label>
                <input type="text" name="sName" id="sName" required>
            </div>
            <div class="Stu_details input-group" >
                <label for="regNo">2. Registration No:</label>
                <input type="text" name="regNo" id="regNo" required>
                <div class="error"></div>
            </div>
            <div class="Stu_details">
                <label for="indexNo">3. Index No:</label>
                <input type="tel" name="indexNo" id="indexNo" required>
            </div>
            <div class="Stu_details input-group" >
                <label for="mobileNo">4. Mobile No:</label>
                <input type="tel" name="mobileNo" id="mobileNo" required>
                <div class="error"></div>
            </div>
            
            <div class="radi">
                <label for="year">5. Year:</label>
                <input type="radio" name="year" value="1st" id="1styear" required>
                <label for="1styear">1st Year</label>
                <input type="radio" name="year" value="2nd" id="2ndyear" required>
                <label for="2ndyear">2nd Year</label>
                <input type="radio" name="year" value="3rd" id="3rdyear" required>
                <label for="3rdyear">3rd Year</label>
                <input type="radio" name="year" value="4th" id="4thyear" required>
                <label for="4thyear">4th Year</label>
                
            </div>
            <div class="radi">
                <label for="semester">6. Semester:</label>
                <input type="radio" name="sem" id="1stSemester" value="1stSemester" required >
                <label for="1stSemester">1st Semester</label>
                <input type="radio" name="sem" id="2ndSemester" value="2ndSemester" required >
                <label for="2ndSemester">2nd Semester</label>
                
            </div>
            <div class="Stu_details">
                <label for="department">7.Name of the Degree Program:</label>
                <Select class="Select-box" name="department" id="department" required>
                    <option value="" disabled selected>Select Department</option>
                    <!-- <option value="Bioprocess Technology">Bioprocess Technology</option>
                    <option value="Food Technology">Food Technology</option>
                    <option value="Electrical and Electronic Technology">Electrical and Electronic Technology</option>
                    <option value="Material Technology">Material Technology</option>
                    <option value="Information Communication Technology">Information Communication Technology</option> -->
                </Select>
            </div>
            <p>I wish to get registered for the undermentioned course units. I declare that I have fulfilled all the
                prerequisites to follow these course units.
            </p>
            <div class="Stu_details">
                <label for="date">Date</label>
                <input type="date" name="date" id="date" required> 
                <label for="signature1" title="Used scan Signature by img">Signature of the students</label>
                <input type="file" name="signature1" id="signature" title="Used scan Signature by img" required>
            </div>
            <br>
            <div class="formdiv">
                <table>
                    <caption>Course Register Table</caption>
                    <tr >
                        <th class="num">NO.</th>
                        <th>Course Code</th>
                        <th>Course Title</th>
                        <th>Status(C/O)</th>
                        <th>Credit Value</th>
                        <!-- <th>Signature of Course Coordinator</th> -->
                    </tr>
                    <?php for ($i = 0; $i < 15; $i++): ?>
                    <tr>
                        <td class="num"><?php echo str_pad($i + 1, 2, '0', STR_PAD_LEFT); ?></td>
                        <td><input type="text" name="cCode[]"></td>
                        <td><input type="text" name="cTitle[]"></td>
                        <td><input type="text" name="cStatus[]"></td>
                        <td><input type="tel" name="cValue[]"></td>
                    </tr>
                    <?php endfor; ?>

                    <tr class="even_row">
                        <th colspan="4">Total Credit Value</th>
                        <td><input type="tel" name="tcValue" required></td>
                    </tr>
                </table>
            </div>
            <p class="note">Note: Students should register for the courses within first two weeks of classes. If the students
                want to add/drop course units after first two weeks, he/she should submit a request to the Head
                of the Department in writing and get approval within one month from the date of registration.
                <br>Status: <b>C</b> - Compulsory <b>O</b> - Optional
            </p>
            
            <div class="btn">
                <button type="submit" name="submit">Submit</button>
                <button type="reset" name="reset">Reset</button>
            </div>
        </form>
        
    </div>
    <script src="/Javascript/Course.js"></script>
    
</body>
</html>