import React from "react";

function SVGDot(dot) {
    return (
        <circle cx={dot.x} cy={dot.y} r={2} fill={dot.color}></circle>
    )
}

export default SVGDot;