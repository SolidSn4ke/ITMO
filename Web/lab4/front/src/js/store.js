import {createStore} from '@reduxjs/toolkit'
import {dataSlice} from "./dataSlice";


export const store = createStore(dataSlice.reducer)
