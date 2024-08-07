const container =document.querySelector(".Login")
const btns =document.querySelectorAll(".btn")
// console.log(btns)
const imgList=["Rajarata-university","fot2","Applied 1","FoT-RUSL-2-650x400","Managment 2","Rajarata_university","FoT-RUSL-4-650x400","RUSLBG","FoT-RUSL-3-650x400"]

let index = 0
btns.forEach((button)=>
{
    button.addEventListener("click",()=>
    {
        if(button.classList.contains("btn-left"))
        {
            index--;
            if(index<0)
            {
                index=imgList.length-1;
            }
            container.style.background = `url("../images/${imgList[index]}.jpg") center/cover`
        }
        else
        {
            index++;
            if(index===imgList.length)
            {
                index=0;
            }
            container.style.background = `url("../images/${imgList[index]}.jpg") center/cover`
        }
    })
})


// Profile button
function goToPage() {
    window.location.href = '../sampleProfile.php';
}
