function updateClock(){
    let date = new Date();
    document.getElementById('date').innerHTML = (date.getDate() + "." + date.getMonth() + "." + date.getFullYear());
    let m = date.getMinutes();
    if (m < 10){
        document.getElementById('time').innerHTML = (date.getHours() + ":" + '0' + m);
    } else {
        document.getElementById('time').innerHTML = (date.getHours() + ":" + m);
    }
}

setInterval(updateClock, 9000);