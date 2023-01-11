console.log("test")
let divContents = document.getElementById('parentDiv');
let divContentsC = document.getElementsByClassName('divClass');
divContentsC.children[0].innerHTML = "Hello World";
divContentsC.children[0].style.color = "red";
divContentsC.children[0].style.fontSize = "30px";
divContentsC.children[0].style.display = "none";

let bulbimage = document.getElementById("bulb");
bulbimage.src = "pic_bulbon.gif";


let btn = document.getElementById("button");
btn.addEventListener("click", myfunction);
function myfunction(){
  alert("@@@");
}

document.getElementById("button").onclick = myfunction();
