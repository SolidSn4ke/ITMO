import React, {useEffect, useState} from 'react'
import "../../css/Main.css"
import Controls from "./Controls";
import ResultTable from "./ResultTable";
import axios from "axios";
import SVGGraph from "./SVGGraph";
import {useNavigate} from 'react-router-dom'
import {useDispatch, useSelector} from "react-redux";
import {updateItems} from "../../js/dataSlice";
import InputsForm from "./InputsForm";


const Main = () => {
    const [screenWidth, setScreenWidth] = useState(window.innerWidth);
    const items = useSelector((state) => state.items)
    const r = useSelector((state) => state.r)
    const dispatch = useDispatch();
    const navigate = useNavigate();

    window.onresize = () => {
        setScreenWidth(window.innerWidth)
    }

    useEffect(() => {
        let flag = false
        document.cookie.split(';').forEach(s => {
            if (s.trim().match(/^token=.{36}$/)) {
                flag = true
            }
        })
        if (!flag) navigate("/front")
    })

    const fetchData = async () => {
        try {
            const response = await axios.post("http://localhost:8080/web4/rest-server/result/view-all");
            if (response.status === 202) {
                //setItems(response.data)
                dispatch(updateItems(response.data))
            }
        } catch (error) {
            console.log(error)
        }
    }

    useEffect(() => {
        const interval = setInterval(() => {
            fetchData();
        }, 5000)
        return () => clearInterval(interval)
    })

    const svgClick = async ({clientX, clientY}) => {
        if (r === '') {
            document.getElementById("svgError").innerHTML = "Задайте значение R, чтобы отправить точку нажатием на график"
            return
        }
        let svg = document.getElementById("graph")
        let point = svg.createSVGPoint();
        point.x = clientX;
        point.y = clientY;
        point = point.matrixTransform(svg.getScreenCTM().inverse());
        let data = {
            x: 0, y: 0, r: r
        }
        if (point.x < 150) {
            data.x = (-1.25 + point.x / 150 * 1.25) * Math.abs(parseInt(r))
        } else {
            data.x = ((point.x - 150) / 120) * Math.abs(parseInt(r))
        }
        if (point.y < 150) {
            data.y = (1.25 - point.y / 120) * Math.abs(parseInt(r))
        } else {
            data.y = (-(point.y - 150) / 120) * Math.abs(parseInt(r))
        }

        try {
            let response = await axios.post("http://localhost:8080/web4/rest-server/result/add-to-db", data, {withCredentials: true})
            if (response.status !== 201) {
                document.getElementById("formError").innerHTML = response.data
            } else {
                document.getElementById("formError").innerHTML = ''
            }
        } catch (e) {
            console.log(e)
        }
    }

    if (screenWidth >= 1025) {
        return (
            <div id="container">
                <div id={"wrapper"} className={"row"}>
                    <InputsForm/>
                    <SVGGraph items={items} r={r} svgClick={svgClick}/>
                    <div className="column">
                        <ResultTable items={items}/>
                        <Controls/>
                    </div>
                </div>
            </div>
        )
    }

    if (screenWidth >= 860 && screenWidth < 1025) {
        return (
            <div id="container">
                <div id={"wrapper"} className={"row"}>
                    <InputsForm/>
                    <SVGGraph items={items} r={r} svgClick={svgClick}/>
                </div>
                <div className={"row"}>
                    <div className="column">
                        <ResultTable items={items}/>
                        <Controls/>
                    </div>
                </div>
            </div>
        )
    }

    if (screenWidth < 860) {
        return (
            <div id="container">
                <div id={"wrapper"} className={"row"}>
                    <InputsForm/>
                </div>
                <div className={"row"}>
                    <SVGGraph items={items} r={r} svgClick={svgClick}/>
                </div>
                <div className={"row"}>
                    <div className="column">
                        <ResultTable items={items}/>
                        <Controls/>
                    </div>
                </div>
            </div>
        )
    }
}

export default Main;