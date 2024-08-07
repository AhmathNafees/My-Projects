<?php
    session_start();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/default.css" type="text/css">
    <link rel="stylesheet" href="css/index.css" type="text/css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="path/to/font-awesome/css/font-awesome.min.css">
    <title>Home</title>
</head>
<body>
    <div class="menu">
        <a href="index.php">Home</a>
        <a href="Student Record.html">Student Record</a>
        <a href="Course.php">Course</a>
        <a href="/Faculty/Faculty.html">Faculty</a>
        <a href="https://e-learn.rjt.ac.lk/" target="_blank">LMS</a>
        <?php
            
            if(isset($_SESSION["userName"]))
            {
                echo '<a href="logout.php" class="logout">logout</a>';
            }
            else
            {
                echo '<a href="loginpage.php" class="logout">login</a>';
            }
        ?>
        
        <!-- <a href="Security and Privacy.html">Privacy</a> -->
        <div class="head">
            <h1>University Student Management System</h1>
        </div>
        <div class="">
            <a class="profile" onclick="goToPage()"></a>
        </div>
        
    </div>
    <div class="Login">
        <a class="btn btn-left"><i class="fa fa-chevron-circle-left" aria-hidden="true"></i></a>
        <a class="btn btn-right"><i class="fa fa-chevron-circle-right" aria-hidden="true"></i></a>
        <p>Rajarata University of Sri-Lanka</p>
        <!-- <img src="images/RUSLBG.jpg" alt="RUSL Background"> -->
        <!-- <div class="div_a">
            <a class="login" href="loginpage.php">Login</a>
            <a class="sign" href="Student Record.html">Sign up.</a>
        </div> -->
    </div>
    <div class="About">
        <p class="About_uni">About our University</p>
        <!-- <h2>University History</h2> -->
        <p class="paragraph">Rajarata University of Sri Lanka, located in the historic city of Mihintale, 
            which is situated 14 kilometers away from the east of Anuradhapura, was established on 31<sup>st</sup>  
            of January, 1996. It envisages to highlight the city, Mihintale, which marks the inception of 
            the Sri Lankan social development, as one of the most prominent centre of the present academic 
            arena in the Sri Lankan history. Through this, it is expected to produce virtuous, intellectual
            and competent citizens for the needs of the 21<sup>st</sup>century.
        </p>
    </div>
    <div class="img_map">
        <img src="images/Rajarata_university.jpg" alt="Rajarata_university of Sri-Lanka" title="Rajarata_university
        of Sri-Lanka">
        <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3947.4146303212847!2d80.50076197406031!3d8.36080869909227!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3afc8a63370239cb%3A0xfde408ba97b0d4d3!2sRajarata%20University%20of%20Sri%20Lanka!5e0!3m2!1sen!2slk!4v1707415380846!5m2!1sen!2slk"
        width="50%" height="450" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade">
        </iframe>

    </div>
    <footer class="footer">
        <div class="foot_container">
            <div class="foot_col">
                <h4>About the University</h4>
                <ul>
                    <li>
                        <br>Rajarata University of Sri Lanka (Sinhala: රජරට විශ්වවිද්‍යාලය, Tamil: ரஜரட்ட பல்கலைக்கழகம்,
                        abbreviated RUSL) is a public University located in the historic city of Mihintale, near 
                        Anuradhapura, Sri Lanka. The Rajarata University of Sri Lanka was established as the eleventh
                         University in Sri Lanka and was opened on January 31st, 1996
                    </li>
                    <div class="Social_links">
                        <li><a href="https://www.facebook.com/RajarataUniversity/?ref=pages_you_manage" class="fa fa-facebook"></a></li>
                        <li><a href="https://twitter.com/RUofSL" class="fa fa-twitter"></a></li>
                        <li><a href="https://www.linkedin.com/school/rajarata-university-of-sri-lanka/" class="fa fa-linkedin"></a></li>
                        <li><a href="https://www.youtube.com/user/rutarusl" class="fa fa-youtube"></a></li>
                    </div>
                    
                </ul>
            </div>
            <div class="foot_col" id="foot_col3">
                <h4>Address</h4>
                <ul>
                    <li><br><span class="material-symbols-outlined">
                        home_pin
                        </span>Rajarata University of Sri Lanka, Mihintale - 50300, Sri Lanka</li>
                    <li><span class="material-symbols-outlined">
                        call
                        </span><a href="tel:+94(25)2266643">+94(25)2266643</a></li>
                    <li><span class="material-symbols-outlined">
                        mail
                        </span><a href="info@rjt.ac.lk">info@rjt.ac.lk</a></li>
                </ul>
            </div>
            <div class="foot_col" id="foot_col3">
                <h4>Quick Links</h4>
                <ul>
                    <li><br><span class="material-symbols-outlined">
                        dialogs
                        </span><a href="../index.php">Home</a></li>
                    <li><span class="material-symbols-outlined">
                        dialogs
                        </span><a href="../loginpage.php">Login</a></li>
                    <li><span class="material-symbols-outlined">
                        dialogs
                        </span><a href="../Student Record.html">SignUp/Student Record</a></li>
                    <li><span class="material-symbols-outlined">
                        dialogs
                        </span><a href="../Course.php">Course Register</a></li>
                    <li><span class="material-symbols-outlined">
                        dialogs
                        </span><a href="https://e-learn.rjt.ac.lk/" target="_blank">LMS</a></li>
                </ul>
            </div>
        </div>
        <center class="Copyright">Copyright &copy; 2024 | Designed By BRAVE LIONS</center>
    </footer>
    
    <script src="/Javascript/index.js"></script>
</body>
</html>