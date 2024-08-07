<?php
    session_start();
    session_destroy();
    header("Location:index.php?mes=you are logout");
?>