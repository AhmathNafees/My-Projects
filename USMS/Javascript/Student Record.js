const form=document.querySelector("#form")
const reg_No = document.querySelector("#regno")
const mobile_No = document.querySelector("#mNumber")
const user_Name = document.querySelector("#uName")
const create_Pwd = document.querySelector("#cPassword")
const confirm_Pwd = document.querySelector("#conPassword")



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
    const user_Name_val=user_Name.value.trim();
    const create_Pwd_val=create_Pwd.value.trim();
    const confirm_Pwd_val=confirm_Pwd.value.trim();
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

    if(!validateUserName(user_Name_val))
    {
        success=false;
        setError(user_Name,"Please Enter a Correct Format")
    }
    else
    {
        setSuccess(user_Name)
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

    if(create_Pwd_val.length < 8)
    {
        success=false;
        setError(create_Pwd,"Password must be atleast 8 character")
    }
    else
    {
        setSuccess(create_Pwd)
    }

    if(create_Pwd_val!==confirm_Pwd_val)
    {
        success=false;
        setError(confirm_Pwd,"Password does not match")
    }
    else
    {
        setSuccess(confirm_Pwd)
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

const validateUserName=(user_Name)=> 
{
    return String(user_Name)
        .toLowerCase()
        .match(/^[A-Za-z]{3}\d{7}$/);
}

const validatePhoneNo=(mobile_No)=> 
{
    return String(mobile_No)
        .match(/^\d{9,10}$/);
}


// Select Department
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