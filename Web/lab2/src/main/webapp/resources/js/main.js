const rInput = document.getElementById("form:rInput");
const xInput = document.getElementById("form:xInput");
const yInput = document.getElementById("form:yInput");
const yError = document.getElementById("yError");
const rButtons = document.getElementsByClassName("rButton");
const svg = document.querySelector('svg');
var dots = [];


for (let i = 0; i < rButtons.length; i++) {
    rButtons[i].onclick = function () {
        for (let i = 0; i < rButtons.length; i++) {
            rButtons[i].style.border = "1px solid black"
        }
        rButtons[i].style.border = "2px solid green"
        rInput.value = rButtons[i].value
        updateSvgDotsRadius();
    }
}

function validateAll() {
    let flag = !(rInput.value === "")
    if (flag === false) {
        document.getElementById('rError').innerText = 'Выберите значение для r'
    } else {
        if (rInput.value >= 1 && rInput.value <= 3) {
            document.getElementById('rError').innerText = ''
        } else {
            document.getElementById('rError').innerText = 'Введено неверное значение'
            flag = false
        }
    }

    xInput.value = ice.ace.instance("slider").getValue();

    let regex = /^(?:-?[0-2][.,]\d+|-?[1-3]([.,]0+)?|0([.,]\d+)?)$/

    if (validate(yInput, regex) === true) {
        yError.innerText = "";
    } else {
        yError.innerText = "Значение должно быть от -3 до 3";
    }

    if (flag === false) {
        event.preventDefault();
    } else {
        dots.push(new Dot(xInput.value, yInput.value));
        drawDot(dots[dots.length - 1]);
    }
}

function validate(node, regex) {
    return regex.test(node.value)
}

function findLabel(node) {
    let nodeId = node.id;
    let labels = document.getElementsByTagName('label');
    for (let i = 0; i < labels.length; i++) {
        if (labels[i].htmlFor === nodeId)
            return labels[i];
    }
}

svg.addEventListener('click', ({clientX, clientY}) => {
    let point = svg.createSVGPoint();
    point.x = clientX;
    point.y = clientY;
    point = point.matrixTransform(svg.getScreenCTM().inverse());
    if (validate(rInput, /^(?:[1-2][.,]\d+|[1-3]([,.]0+)?)$/) === true) {
        if (point.x < 150) {
            ice.ace.instance("slider").setValue((-1.25 + point.x / 150 * 1.25) * rInput.value);
        } else {
            ice.ace.instance("slider").setValue(((point.x - 150) / 120) * rInput.value);
        }
        if (point.y < 150) {
            yInput.value = (1.25 - point.y / 120) * rInput.value;
        } else {
            yInput.value = (-(point.y - 150) / 120) * rInput.value;
        }
    } else {
        findLabel(rInput).innerText = 'Введено неверное значение'
    }
    document.getElementById("form:confirmButton").click()
})


function drawDot(dot) {
    let svgDot = document.createElementNS("http://www.w3.org/2000/svg", "circle");
    if (dot.x >= 0) {
        svgDot.setAttribute('cx', dot.x * 120 / rInput.value + 150)
    } else {
        svgDot.setAttribute('cx', 120 * (1.25 + dot.x / rInput.value))
    }
    if (dot.y >= 0) {
        svgDot.setAttribute('cy', (dot.y / rInput.value - 1.25) * -120)
    } else {
        svgDot.setAttribute('cy', -120 * dot.y / rInput.value + 150)
    }
    svgDot.setAttribute('r', '2')
    if (checkAreaHit(dot.x, dot.y) === true) {
        svgDot.setAttribute('fill', '#001762')
    } else {
        svgDot.setAttribute('fill', '#ff00ea')
    }
    svgDot.setAttribute("class", "dot")
    svg.appendChild(svgDot);
}

function addAllSvgDots() {
    dots.forEach(dot => {
        drawDot(dot)
    })
}

function deleteAllSvgDots() {
    let svgDots = document.getElementsByClassName('dot');
    while (svgDots[0]) {
        svgDots[0].parentNode.removeChild(svgDots[0]);
    }
}

function updateSvgDotsRadius() {
    deleteAllSvgDots();
    addAllSvgDots();
}

function clearDots() {
    dots = [];
}

function updateDots(){
    let xs = document.getElementsByClassName('xResult');
    let ys = document.getElementsByClassName('yResult')
    for (let i = 0; i < xs.length; i++){
        dots.push(new Dot(xs[i].innerText, ys[i].innerText));
    }
}

function checkAreaHit(x, y) {
    if (x >= 0) {
        if (y >= 0) {
            return Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow(rInput.value, 2);
        } else return false;
    } else {
        if (y >= 0) {
            return Math.abs(x) <= rInput.value && y <= rInput.value / 2;
        } else {
            return y >= -1 * x - rInput.value;
        }
    }
}

function getRandomArbitrary(min, max) {
    return Math.random() * (max - min) + min;
}

document.getElementById('luckyButton').addEventListener('click', () => {
    ice.ace.instance("slider").setValue(getRandomArbitrary(-4, 4));
    yInput.value = getRandomArbitrary(-3, 3);
    rButtons[Math.floor(Math.random() * 5)].click();
    document.getElementById('form:confirmButton').click();
})

class Dot {
    constructor(x, y) {
        this.x = x
        this.y = y
    }
}
