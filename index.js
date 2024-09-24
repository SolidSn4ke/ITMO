const yInput = document.getElementById('y')
const RInput = document.getElementById('R')
const confirmButton = document.getElementById('confirm')

confirmButton.onclick = dataTransfer;

function dataTransfer() {
    flag = !(document.querySelector('input[name="x"]:checked') === null)
    if (flag === false) {
        document.getElementById('x_error').innerText = 'Выберите значение для x'
    } else {
        document.getElementById('x_error').innerText = ''
    }

    var fields = [yInput, RInput]
    var regexes = [
        /^(?:[-]?[0-2][.,]\d+|-4[.,]\d+|(?:-[1-5]|[0-3])([.,]0+)?)$/,       //  -5 <= y <= 3
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

    if (flag === true) {
        let xhr = new XMLHttpRequest();
        xhr.open('GET',
            'http://localhost:8080/fcgi-bin/fcgi.jar?x=' + document.querySelector('input[name="x"]:checked').value + '&y=' + yInput.value + '&R=' + RInput.value,
            true)
        xhr.onload = function () {
            if (xhr.status === 200) {
                let res = JSON.parse(xhr.response)
                if(res['res'] === 'error'){
                    findLable(document.getElementById('results')).innerText = res['info']
                } else {
                    findLable(document.getElementById('results')).innerText = ''
                    updateTable(res)
                }
                //updateDot(res['x'], res['y'], res['r'])
                console.log(res)
            } else {
                console.log(xhr.statusText + res)
            }
        }
        xhr.send()
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

function updateTable(values) {
    var body = document.getElementById('results').getElementsByTagName('tbody')[0]
    var row = body.insertRow()
    var cell = row.insertCell()
    cell.innerText = body.rows.length
    for (var key in values) {
        cell = row.insertCell()
        cell.innerText = values[key]
    }
}

/*
function updateDot(x, y, r){
    var dot = document.getElementById('graph').getElementsByTagName('circle')
    svg.getElementsByTagName('circle').remove()
    dot.setAttribute('cx', (150 + x / 5 * 150).toString())
    dot.setAttribute('cy', (150 + y / 5 * 150).toString())
    dot.setAttribute('r', '2')
}
    */