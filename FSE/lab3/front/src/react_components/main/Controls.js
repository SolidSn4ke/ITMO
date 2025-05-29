import React from "react";
import axios from "axios";
import {useNavigate} from 'react-router-dom'
import Cookies from "js-cookie";

const Controls = () => {
    const navigate = useNavigate();

    const tryClear = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/web4/rest-server/result/clear", {}, {withCredentials: true});

            if (response.status !== 200) {
                document.getElementById("formError").innerHTML = response.data;
            }
        } catch (e) {
            console.log("Error: " + e)
        }
    }

    const toHomePage = () => {
        Cookies.remove("token")
        navigate("/front/")
    }

    return (
        <div id="controls">
            <form onSubmit={tryClear}>
                <input type={"submit"} id={"clear"} value={"Очистить"} style={{margin: "5px"}}/>
            </form>
            <input type={"button"} id={"navi"} value={"Выйти"} onClick={() => toHomePage()}/>
        </div>
    )
}

export default Controls;