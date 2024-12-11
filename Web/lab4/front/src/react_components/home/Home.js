import React from "react";
import logo from '../../media/logo.svg';
import '../../css/Home.css';
import '../../js/clock'
import LoginForm from "./LoginForm";

class Home extends React.Component {
    render() {
        return (
            <div id="container" className="App">
                <header id="header">
                    Кузьмин Артемий Андреевич P3214 321414
                </header>
                <div className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <div id="clock">
                        <div id="date"></div>
                        <div id="time"></div>
                    </div>
                    <LoginForm/>
                </div>
            </div>
        );
    }
}

export default Home;
