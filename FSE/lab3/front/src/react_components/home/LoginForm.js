import axios from 'axios';
import React, {useEffect, useState} from "react";
import {useNavigate} from 'react-router-dom'

const LoginForm = () => {
    const navigate = useNavigate();
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');

    useEffect(() => {
        document.cookie.split(';').forEach(s => {
            if (s.trim().match(/^token=.{36}$/)){
                navigate("/main")
            }
        })
    })

    useEffect(() => {
        document.getElementById('login').value = ''
    }, [])

    const action = async (e) => {
        if (e.nativeEvent.submitter.name === 'login') {
            return tryLogin(e);
        } else return trySignIn(e);

    }

    const tryLogin = async (e) => {
        e.preventDefault();

        const data = {
            login: login,
            password: password
        }

        if (login === '' || password === ''){
            setMessage("Заполните все поля")
            return
        }

        try {
            const response = await axios.post("http://localhost:8080/web4/rest-server/user/login", data, {withCredentials: true})
            if (response.status === 202) {
                setMessage(response.data)
            } else {
                navigate("/main")
            }
        } catch (error) {
            console.log('Error:', error)
        }
    }

    const trySignIn = async (e) => {
        e.preventDefault();

        const data = {
            login: login,
            password: password
        }

        if (login === '' || password === ''){
            setMessage("Заполните все поля")
            return
        }

        try {
            const response = await axios.post("http://localhost:8080/web4/rest-server/user/sign-in", data, {withCredentials: true})
            if (response.status === 202) {
                setMessage(response.data)
            } else {
                navigate("/main");
            }
        } catch (error) {
            console.log('Error:', error)
        }
    }

    return (
        <div id={"loginInputs"} onSubmit={action}>
            <form id={"loginForm"} method={"POST"}>
                <label>Логин</label><br/>
                <input type={"text"} id={"login"} value={login} onChange={(e) => setLogin(e.target.value)}/><br/>
                <label>Пароль</label><br/>
                <input type={"password"} id={"password"} value={password}
                       onChange={(e) => setPassword(e.target.value)}/><br/>
                <input name={'login'} type={"submit"} id={"formLogin"} value={"Войти"}/>
                <input name={'sign in'} type={"submit"} id={"formSignIn"} value={"Зарегистрироваться"}/>
            </form>
            <p style={{color: "red"}}>{message}</p>
        </div>)
}

export default LoginForm;