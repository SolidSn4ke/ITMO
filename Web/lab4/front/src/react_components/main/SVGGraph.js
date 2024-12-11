import React from "react";
import SVGDot from "./SVGDot";
import {convertX, convertY, getDotColor} from "../../js/dot";

function SVGGraph({items, r, svgClick}) {
    if (r === "") {
        return (
            <div id="centerGraphic" className="column">
                <svg xmlns="http://www.w3.org/2000/svg" width="300" height="300" id="graph" onClick={svgClick}>
                    <rect x="150" y="150" width="60" height="120" fill="#61dafb"/>
                    <polygon points="150,30 150,150 270,150" fill="#61dafb"/>
                    <path d="M90 150
                    A 60 60, 0, 0, 1, 150 90
                    L 150 150 Z"
                          fill="#61dafb" stroke="none"/>
                    <line x1="150" y1="0" x2="150" y2="300" stroke="white"/>
                    <line x1="300" y1="150" x2="290" y2="155" stroke="white"/>
                    <line x1="300" y1="150" x2="290" y2="145" stroke="white"/>
                    <line x1="0" y1="150" x2="300" y2="150" stroke="white"/>
                    <line x1="150" y1="0" x2="145" y2="10" stroke="white"/>
                    <line x1="150" y1="0" x2="155" y2="10" stroke="white"/>
                    <line x1="210" y1="145" x2="210" y2="155" stroke="white"/>
                    <line x1="270" y1="145" x2="270" y2="155" stroke="white"/>
                    <line x1="90" y1="145" x2="90" y2="155" stroke="white"/>
                    <line x1="30" y1="145" x2="30" y2="155" stroke="white"/>
                    <line x1="145" y1="270" x2="155" y2="270" stroke="white"/>
                    <line x1="145" y1="210" x2="155" y2="210" stroke="white"/>
                    <line x1="145" y1="90" x2="155" y2="90" stroke="white"/>
                    <line x1="145" y1="30" x2="155" y2="30" stroke="white"/>
                    <text x="280" y="145" fill="white">x</text>
                    <text x="155" y="10" fill="white">y</text>
                </svg>
                <p>
                    <label id={"svgError"} className={"errorLabel"} htmlFor={"graph"}/>
                </p>
            </div>
        )
    }
    r = parseInt(r)
    if (r > 0) {
        return (
            <div id="centerGraphic" className="column">
                <svg xmlns="http://www.w3.org/2000/svg" width="300" height="300" id="graph" onClick={svgClick}>
                    <rect x="150" y="150" width="60" height="120" fill="#61dafb"/>
                    <polygon points="150,30 150,150 270,150" fill="#61dafb"/>
                    <path d="M90 150
                    A 60 60, 0, 0, 1, 150 90
                    L 150 150 Z"
                          fill="#61dafb" stroke="none"/>
                    <line x1="150" y1="0" x2="150" y2="300" stroke="white"/>
                    <line x1="300" y1="150" x2="290" y2="155" stroke="white"/>
                    <line x1="300" y1="150" x2="290" y2="145" stroke="white"/>
                    <line x1="0" y1="150" x2="300" y2="150" stroke="white"/>
                    <line x1="150" y1="0" x2="145" y2="10" stroke="white"/>
                    <line x1="150" y1="0" x2="155" y2="10" stroke="white"/>
                    <line x1="210" y1="145" x2="210" y2="155" stroke="white"/>
                    <line x1="270" y1="145" x2="270" y2="155" stroke="white"/>
                    <line x1="90" y1="145" x2="90" y2="155" stroke="white"/>
                    <line x1="30" y1="145" x2="30" y2="155" stroke="white"/>
                    <line x1="145" y1="270" x2="155" y2="270" stroke="white"/>
                    <line x1="145" y1="210" x2="155" y2="210" stroke="white"/>
                    <line x1="145" y1="90" x2="155" y2="90" stroke="white"/>
                    <line x1="145" y1="30" x2="155" y2="30" stroke="white"/>
                    <text x="280" y="145" fill="white">x</text>
                    <text x="155" y="10" fill="white">y</text>
                    {items.map((item) => {
                        return (
                            <SVGDot key={crypto.randomUUID()} x={convertX(item.x, r)} y={convertY(item.y, r)}
                                    color={getDotColor(item.x, item.y, r)}/>
                        )
                    })}
                </svg>
                <p>
                    <label id={"svgError"} className={"errorLabel"} htmlFor={"graph"}/>
                </p>
            </div>
        )
    }
    if (r < 0) {
        return (
            <div id="centerGraphic" className="column">
                <svg xmlns="http://www.w3.org/2000/svg" width="300" height="300" id="graph" onClick={svgClick}>
                    <rect x="90" y="30" width="60" height="120" fill="#61dafb"/>
                    <polygon points="30,150 150,150 150,270" fill="#61dafb"/>
                    <path d="M210 150
                    A 60 60, 0, 0, 1, 150 210
                    L 150 150 Z"
                          fill="#61dafb" stroke="none"/>
                    <line x1="150" y1="0" x2="150" y2="300" stroke="white"/>
                    <line x1="300" y1="150" x2="290" y2="155" stroke="white"/>
                    <line x1="300" y1="150" x2="290" y2="145" stroke="white"/>
                    <line x1="0" y1="150" x2="300" y2="150" stroke="white"/>
                    <line x1="150" y1="0" x2="145" y2="10" stroke="white"/>
                    <line x1="150" y1="0" x2="155" y2="10" stroke="white"/>
                    <line x1="210" y1="145" x2="210" y2="155" stroke="white"/>
                    <line x1="270" y1="145" x2="270" y2="155" stroke="white"/>
                    <line x1="90" y1="145" x2="90" y2="155" stroke="white"/>
                    <line x1="30" y1="145" x2="30" y2="155" stroke="white"/>
                    <line x1="145" y1="270" x2="155" y2="270" stroke="white"/>
                    <line x1="145" y1="210" x2="155" y2="210" stroke="white"/>
                    <line x1="145" y1="90" x2="155" y2="90" stroke="white"/>
                    <line x1="145" y1="30" x2="155" y2="30" stroke="white"/>
                    <text x="280" y="145" fill="white">x</text>
                    <text x="155" y="10" fill="white">y</text>
                    {items.map((item) => {
                        return (
                            <SVGDot key={crypto.randomUUID()} x={convertX(item.x, r)} y={convertY(item.y, r)}
                                    color={getDotColor(item.x, item.y, r)}/>
                        )
                    })}
                </svg>
                <p>
                    <label id={"svgError"} className={"errorLabel"} htmlFor={"graph"}/>
                </p>
            </div>
        )
    }
}

export default SVGGraph;