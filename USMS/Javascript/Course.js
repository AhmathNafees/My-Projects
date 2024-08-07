const facultyDep={
    "Faculty of Technology" :["Bioprocess Technology","Food Technology","Electrical and Electronic Technology","Material Technology","Information Communication Technology"],
    "Faculty of Applied Sciences" :["Biological Sciences","Chemical Sciences","Computing","Health Promotion","Physical Sciences"],
    "Agriculture" :["Agricultural Engineering and Soil Science","Agricultural Systems","Animal and Food Sciences","Plant Sciences"],
    "Management Studies" :["Accountancy and Finance","Business Management","Human Resource Management","Information Systems","Tourism and Hospitality Management","Marketing Management"],
    "Medicine and Allied Sciences" :["Anatomy","Biochemistry","Community Medicine"],
    "Social Sciences and Humanities" :["Archaeology and Heritage Management","English Language Teaching","Social Sciences","Languages","Humanities","Environmental Management","Economics","Information Technology"]
}

function changeDepartment(value)
{
    if (value == 0)
    {
        
        document.getElementById("department").innerHTML="<option></option>"

    }
    else
    {
        let options =""
        for(departmentID in facultyDep[value])
        {
            // console.log("value" + value)
            options += "<option>" + facultyDep[value][departmentID] + "</option>"
            // console.log("options" + options,facultyDep[value])
        }
        document.getElementById("department").innerHTML=options
    }
}

const form=document.querySelector("#courseForm")
const reg_No = document.querySelector("#regNo")
const mobile_No = document.querySelector("#mobileNo")


// validation for regno and mobile no

form.addEventListener("submit",(e)=>
{
    
    // validateInputs();
    if(!validateInputs())
    {
        e.preventDefault();
    }
})

function validateInputs()
{
    const reg_No_val=reg_No.value.trim();
    const mobile_No_val=mobile_No.value.trim();
  
    let success = true;

    if(!validateRegNo(reg_No_val))
    {
        success=false;
        setError(reg_No,"Please Enter a Correct Format")
    }
    else
    {
        setSuccess(reg_No)
    }

   
    if(!validatePhoneNo(mobile_No_val))
    {
        success=false;
        setError(mobile_No,"Phone Number is not Valid")
    }
    else
    {
        setSuccess(mobile_No)
    }


    return success;

}

function setError(element,message)
{
    const inputGroup = element.parentElement;
    const errorElement = inputGroup.querySelector(".error");

    errorElement.innerText = message;
    inputGroup.classList.add("error")
    inputGroup.classList.remove("success")
}
function setSuccess(element)
{
    const inputGroup = element.parentElement;
    const errorElement = inputGroup.querySelector(".error");

    errorElement.innerText = "";
    inputGroup.classList.add("success")
    inputGroup.classList.remove("error")
}
const validateRegNo=(reg_No)=> 
{
    return String(reg_No)
        .toLowerCase()
        .match(/^[A-Za-z]{3}\/\d{4}\/\d{4}\/\d{3}$/);
}

const validatePhoneNo=(mobile_No)=> 
{
    return String(mobile_No)
        .match(/^\d{9,10}$/);
}
