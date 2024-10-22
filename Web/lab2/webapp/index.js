const yInput = document.getElementById('y')
const RInput = document.getElementById('R')
const XInput = document.getElementById('x')
const confirmButton = document.getElementById('confirm')
const xButtons = document.getElementsByClassName("x_button");
const svg = document.querySelector('svg');

for(let i = 0; i < xButtons.length; i++){
    xButtons[i].onclick = function(){
        for(let i = 0; i < xButtons.length; i++){
        xButtons[i].style.border = "1px solid black"
    }
    xButtons[i].style.border = "2px solid green"
    XInput.value = xButtons[i].value
    }
}

function dataTransfer() {
    flag = !(XInput.value === "")
    if (flag === false) {
        document.getElementById('x_error').innerText = 'Выберите значение для x'
    } else {
        if(XInput.value <= 2 && XInput.value >= -2){
            document.getElementById('x_error').innerText = ''
        } else {
            document.getElementById('x_error').innerText = 'Введено неверное значение'
            flag = false           
        }
    }

    var fields = [yInput, RInput]
    var regexes = [
        /^(?:[-]?[0-2][.,]\d+|[34][.,]\d+|(?:-[1-3]|[0-5])([.,]0+)?)$/,       //  -3 <= y <= 5
        /^(?:[1-3][.,]\d+|[1-4]([,.]0+)?)$/                                 //   1 <= R <= 4       
    ]

    for (var i = 0; i < fields.length; i++) {
        if (validate(fields[i], regexes[i]) === true) {
            findLable(fields[i]).innerText = ''
        } else {
            findLable(fields[i]).innerText = 'Введено неверное значение'
            flag = false
        }
    }

    if(flag === false){
        event.preventDefault()
    }
}

function validate(node, regex) {
    return regex.test(node.value)
}

function findLable(node) {
    var nodeId = node.id;
    labels = document.getElementsByTagName('label');
    for (var i = 0; i < labels.length; i++) {
        if (labels[i].htmlFor == nodeId)
            return labels[i];
    }
}

function updateTable(res, x, y, z) {
    values = [res, x, y, z]
    var body = document.getElementById('results').getElementsByTagName('tbody')[0]
    var row = body.insertRow()
    var cell = row.insertCell()
    cell.innerText = body.rows.length
    for (var key in values) {
        cell = row.insertCell()
        cell.innerText = values[key]
    }
}

svg.addEventListener('click', ({clientX, clientY}) => {
  let point = svg.createSVGPoint();
  point.x = clientX;
  point.y = clientY;
  point = point.matrixTransform(svg.getScreenCTM().inverse());
  if(validate(RInput, /^(?:[1-3][.,]\d+|[1-4]([,.]0+)?)$/) === true){
    if(point.x < 150){
        XInput.value = (-1.25 + point.x/150 * 1.25) * RInput.value
    } else {
        XInput.value = ((point.x - 150)/120) * RInput.value
    }
    if(point.y < 150){
        yInput.value = (1.25 - point.y / 120) * RInput.value
    } else {
        yInput.value = (-(point.y - 150)/120) * RInput.value
    }
  } else {
    findLable(RInput).innerText = 'Введено неверное значение'
  }
  confirmButton.click()
})

function addDot(x, y, r){
    var dot = document.createElementNS("http://www.w3.org/2000/svg", "circle");
    if(x >= 0){
        dot.setAttribute('cx', x * 120 / r + 150)
    } else {
        dot.setAttribute('cx', 120 * (1.25 + x / r))
    }
    if(y >= 0){
        dot.setAttribute('cy', (y / r - 1.25) * -120)
    } else {
        dot.setAttribute('cy', -120 * y / r + 150)
    }
    dot.setAttribute('r', '2')
    dot.setAttribute('fill', '#fc00c6')
    dot.setAttribute("class", "tmpDot")
    svg.appendChild(dot)
}