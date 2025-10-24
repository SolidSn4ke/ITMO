import React, {useEffect} from 'react';
import '../css/App.css';
import Table from "./Table"
import QuickView from "./QuickView";
import WorkerBuilder from "./WorkerBuilder";
import {useAppDispatch, useAppSelector} from "../ts/redux/hooks";
import axios from "axios";
import {updateItems} from "../ts/redux/workerSlice";
import {toast, ToastContainer} from "react-toastify";

export function showErrorNotification(message: string) {
    toast.error(message, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true
    });
}

export function showInfoNotification(message: string) {
    toast.info(message, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true
    });
}

function Main() {
    const viewMode = useAppSelector((state) => state.viewMode)
    const items = useAppSelector((state) => state.items)
    const workerView = useAppSelector((state) => state.workerView)
    const updatable = useAppSelector((state) => state.updatable)
    const searchValue = useAppSelector((state) => state.searchValue)
    const dispatch = useAppDispatch()


    useEffect(() => {
        if (updatable) {
            const getWorkers = async () => {
                try {
                    let response = await axios.post("http://localhost:8080/back-1.0-SNAPSHOT/rest-server/actions/view-workers", searchValue, {
                        withCredentials: true,
                        headers: {"Content-Type": "text/plain"}
                    })
                    if (response.status === 200) {
                        dispatch(updateItems(response.data))
                    }
                } catch (e) {
                    console.log(e)
                }
            }

            const interval = window.setInterval(getWorkers, 5000)
            return () => {
                window.clearInterval(interval)
            }
        }
    })

    return (
        <div className="Main">
            <header className="App-header"></header>
            <ToastContainer position="top-right" autoClose={5000} hideProgressBar={false}
                            newestOnTop={false} closeOnClick rtl={false} pauseOnFocusLoss draggable pauseOnHover/>
            <div className="row">
                <div className="left-column">
                    <Table items={items} controls={true}></Table>
                </div>
                {viewMode ? <QuickView worker={workerView}/> : <WorkerBuilder workerTemplate={workerView}/>}
            </div>
        </div>
    );
}

export default Main;
