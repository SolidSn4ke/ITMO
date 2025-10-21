import {createStore} from '@reduxjs/toolkit'
import {workerSlice} from "./workerSlice";


export const store = createStore(workerSlice.reducer)

export type RootState = ReturnType<typeof store.getState>;

export type AppDispatch = typeof store.dispatch;

