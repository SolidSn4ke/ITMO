import React from "react";
import {updateX, updateY, updateR} from "../../js/dataSlice";
import {useDispatch, useSelector} from "react-redux";
import axios from "axios";

const InputsForm = () => {
    const x = useSelector((state) => state.x)
    const y = useSelector((state) => state.y)
    const r = useSelector((state) => state.r)
    const dispatch = useDispatch();

    const trySendData = async (e) => {
        e.preventDefault()
        let valid = true;

        if (!x.match(/^(?:-?[0-2][.,]\d+|-4[.,]\d+|(?:-[1-5]|[0-3])([.,]0+)?)$/)) {
            valid = false
            document.getElementById("xError").innerHTML = "Выберите значение для x";
        } else {
            document.getElementById("xError").innerHTML = "";
        }

        if (!y.match(/^(?:-?[0-4][.,]\d+|(?:-[1-5]|[0-5])([.,]0+)?)$/)) {
            valid = false
            document.getElementById("yError").innerHTML = "y - значение должно быть от -5 до 5";
        } else {
            document.getElementById("yError").innerHTML = "";
        }

        if (!r.match(/^(?:-[1-5]|[0-3])$/)) {
            valid = false
            document.getElementById("rError").innerHTML = "Выберите значение для R";
        } else {
            document.getElementById("rError").innerHTML = "";
        }

        if (valid) {
            const data = {
                x: x,
                y: y,
                r: r
            }

            try {
                const response = await axios.post("http://localhost:8080/web4/rest-server/result/add-to-db", data, {withCredentials: true})
                if (response.status !== 201) {
                    document.getElementById("formError").innerHTML = response.data
                } else {
                    document.getElementById("formError").innerHTML = ''
                }
            } catch (e) {
                console.log(e)
            }
        }
    }
    
    return (
        <div id={"leftInputs"} className={"column"}>
            <form onSubmit={trySendData}>
                <div style={{color: "#61dafb"}}>
                    <b>x - координата точки по оси ox</b>
                    <fieldset>
                        <input type={"radio"} value={"-5"} id={"x1"} name={"x"} className={"xButton"}
                               onChange={(e) => dispatch(updateX(e.target.value))}/>
                        <label htmlFor={"x1"}>-5</label><br/>
                        <input type={"radio"} value={"-4"} name={"x"} id={"x2"} className={"xButton"}
                               onChange={(e) => dispatch(updateX(e.target.value))}/>
                        <label htmlFor={"x2"}>-4</label><br/>
                        <input type={"radio"} value={"-3"} name={"x"} id={"x3"} className={"xButton"}
                               onChange={(e) => dispatch(updateX(e.target.value))}/>
                        <label htmlFor={"x3"}>-3</label><br/>
                        <input type={"radio"} value={"-2"} name={"x"} id={"x4"} className={"xButton"}
                               onChange={(e) => dispatch(updateX(e.target.value))}/>
                        <label htmlFor={"x4"}>-2</label><br/>
                        <input type={"radio"} value={"-1"} name={"x"} id={"x5"} className={"xButton"}
                               onChange={(e) => dispatch(updateX(e.target.value))}/>
                        <label htmlFor={"x5"}>-1</label><br/>
                        <input type={"radio"} value={"0"} name={"x"} id={"x6"} className={"xButton"}
                               onChange={(e) => dispatch(updateX(e.target.value))}/>
                        <label htmlFor={"x6"}>0</label><br/>
                        <input type={"radio"} value={"1"} name={"x"} id={"x7"} className={"xButton"}
                               onChange={(e) => dispatch(updateX(e.target.value))}/>
                        <label htmlFor={"x7"}>1</label><br/>
                        <input type={"radio"} value={"2"} name={"x"} id={"x8"} className={"xButton"}
                               onChange={(e) => dispatch(updateX(e.target.value))}/>
                        <label htmlFor={"x8"}>2</label><br/>
                        <input type={"radio"} value={"3"} name={"x"} id={"x9"} className={"xButton"}
                               onChange={(e) => dispatch(updateX(e.target.value))}/>
                        <label htmlFor={"x9"}>3</label><br/>
                    </fieldset>
                    <label id={"xError"} className={"errorLabel"}></label>
                </div>
                <p style={{color: "#61dafb"}}>
                    <b>y - координата точки по оси oy (от -5 до 5)</b><br/>
                    <input id={"yInput"} type={"text"} value={y}
                           onChange={(e) => dispatch(updateY(e.target.value))}/><br/>
                    <label htmlFor="yInput" className="errorLabel" id="yError"/>
                </p>
                <div style={{color: "#61dafb"}}>
                    <b>R - величина R на графике</b>
                    <fieldset>
                        <input type={"radio"} value={"-5"} className={"rButton"} name={"r"} id={"r1"}
                               onChange={(e) => dispatch(updateR(e.target.value))}/>
                        <label htmlFor={"r1"}>-5</label><br/>
                        <input type={"radio"} value={"-4"} className={"rButton"} name={"r"} id={"r2"}
                               onChange={(e) => dispatch(updateR(e.target.value))}/>
                        <label htmlFor={"r2"}>-4</label><br/>
                        <input type={"radio"} value={"-3"} className={"rButton"} name={"r"} id={"r3"}
                               onChange={(e) => dispatch(updateR(e.target.value))}/>
                        <label htmlFor={"r3"}>-3</label><br/>
                        <input type={"radio"} value={"-2"} className={"rButton"} name={"r"} id={"r4"}
                               onChange={(e) => dispatch(updateR(e.target.value))}/>
                        <label htmlFor={"r4"}>-2</label><br/>
                        <input type={"radio"} value={"-1"} className={"rButton"} name={"r"} id={"r5"}
                               onChange={(e) => dispatch(updateR(e.target.value))}/>
                        <label htmlFor={"r5"}>-1</label><br/>
                        <input type={"radio"} value={"0"} className={"rButton"} name={"r"} id={"r6"}
                               onChange={(e) => dispatch(updateR(e.target.value))}/>
                        <label htmlFor={"r6"}>0</label><br/>
                        <input type={"radio"} value={"1"} className={"rButton"} name={"r"} id={"r7"}
                               onChange={(e) => dispatch(updateR(e.target.value))}/>
                        <label htmlFor={"r7"}>1</label><br/>
                        <input type={"radio"} value={"2"} className={"rButton"} name={"r"} id={"r8"}
                               onChange={(e) => dispatch(updateR(e.target.value))}/>
                        <label htmlFor={"r8"}>2</label><br/>
                        <input type={"radio"} value={"3"} className={"rButton"} name={"r"} id={"r9"}
                               onChange={(e) => dispatch(updateR(e.target.value))}/>
                        <label htmlFor={"r9"}>3</label><br/>
                    </fieldset>
                    <label id={"rError"} className={"errorLabel"}></label>
                </div>
                <p>
                    <input type={"submit"} id={"submitForm"}/>
                </p>
            </form>
            <label id={"formError"} className={"errorLabel"}></label>
        </div>
    )
}

export default InputsForm;
