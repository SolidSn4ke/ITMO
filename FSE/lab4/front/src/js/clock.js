function updateClock(){
    try{
        let date = new Date();
        let month = date.getMonth() + 1;
        document.getElementById('date').innerHTML = (date.getDate() + "." + month + "." + date.getFullYear());
        let m = date.getMinutes();
        if (m < 10){
            m = '0' + m;
        }
        let s = date.getSeconds();
        if (s < 10){
            s = '0' + s;
        }
        document.getElementById('time').innerHTML = (date.getHours() + ":" + m + ":" + s);
    } catch(e){}
}

setInterval(updateClock, 1000);