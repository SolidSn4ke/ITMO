import React from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom'
import ReactDOM from 'react-dom/client';
import './css/index.css';
import reportWebVitals from './js/reportWebVitals';
import Home from "./react_components/home/Home";
import Main from "./react_components/main/Main"
import {store} from './js/store'
import { Provider } from 'react-redux'

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
    <React.StrictMode>
        <BrowserRouter>
            <Provider store={store}>
                <Routes>
                    <Route path={"/front"} element={<Home/>}/>
                    <Route path={"/main"} element={<Main/>}/>
                </Routes>
            </Provider>
        </BrowserRouter>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
