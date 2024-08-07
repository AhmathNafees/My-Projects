
addEventListener("change",changeDpt)
function changeDpt()
{
    const facultyName=faculty.options[faculty.selectedIndex].value
    const departmentName =department.options[department.selectedIndex].value
    if(facultyName=="Faculty of Applied Sciences")
    {
        const head_tag=document.getElementById("head")
        head_tag.innerHTML="Welcome " + facultyName
        const department = document.getElementById("department")
        department.innerHTML=`<option value="Maths">Maths</option>
                              <option value="Science">Science</option>`
    }
    console.log(facultyName)
    console.log(departmentName)
}
